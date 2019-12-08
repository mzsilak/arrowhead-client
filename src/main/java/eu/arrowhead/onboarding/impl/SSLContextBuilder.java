package eu.arrowhead.onboarding.impl;

import eu.arrowhead.client.transport.ProtocolConfiguration;
import eu.arrowhead.client.transport.SecureTransport;
import eu.arrowhead.client.utils.security.KeyManagerFactoryParameters;
import eu.arrowhead.client.utils.security.SSLContextConfigurator;
import eu.arrowhead.client.utils.security.TrustManagerFactoryParameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Base64Utils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

public abstract class SSLContextBuilder<T>
{
    protected final ProtocolConfiguration protocol;
    private final Logger logger = LogManager.getLogger(SSLContextBuilder.class);
    protected SSLContext sslContext;
    protected SSLContextConfigurator configurator;
    protected TrustManagerFactory trustManagerFactory;
    protected KeyManagerFactory keyManagerFactory;
    protected char[] keyStorePassword;
    protected char[] trustStorePassword;
    protected char[] keyPassword;

    public SSLContextBuilder(final ProtocolConfiguration protocol)
    {
        this.protocol = protocol;
    }


    protected void copyFrom(final SSLContextBuilder builder)
    {
        this.sslContext = builder.sslContext;
        this.configurator = builder.configurator;
        this.trustManagerFactory = builder.trustManagerFactory;
        this.keyManagerFactory = builder.keyManagerFactory;
        this.keyStorePassword = builder.keyStorePassword;
        this.keyPassword = builder.keyPassword;
        this.trustStorePassword = builder.trustStorePassword;
        reloadSSLContext();
    }

    ProtocolConfiguration getProtocol()
    {
        return protocol;
    }

    public SSLContext getSslContext()
    {
        buildSslContext();
        return sslContext;
    }

    SSLContextConfigurator getConfigurator()
    {
        return configurator;
    }

    TrustManagerFactory getTrustManagerFactory()
    {
        return trustManagerFactory;
    }

    KeyManagerFactory getKeyManagerFactory()
    {
        return keyManagerFactory;
    }

    public T withSSLConfiguration(final SSLContextConfigurator configurator)
    {
        sslContext = null;
        this.configurator = configurator;
        return (T) this;
    }

    public T withSSLFactories(final TrustManagerFactory trustManagerFactory,
                              final KeyManagerFactory keyManagerFactory)
    {
        this.trustManagerFactory = trustManagerFactory;
        this.keyManagerFactory = keyManagerFactory;
        return (T) this;
    }

    public T withKeyStorePasswords(final String keyStorePassword, final String keyPassword)
    {
        return withKeyStorePasswords(safeToChar(keyStorePassword), safeToChar(keyPassword));
    }

    public T withKeyStorePasswords(final char[] keyStorePassword, final char[] keyPassword)
    {
        this.keyStorePassword = keyStorePassword;
        this.keyPassword = keyPassword;
        return (T) this;
    }

    public T withTrustStorePassword(final String trustStorePassword)
    {
        return withTrustStorePassword(safeToChar(trustStorePassword));
    }

    public T withTrustStorePassword(final char[] trustStorePassword)
    {
        this.trustStorePassword = trustStorePassword;
        return (T) this;
    }

    private char[] safeToChar(final String string)
    {
        if (Objects.nonNull(string)) { return string.toCharArray(); }
        else { return null; }
    }

    protected void prepareSslContext()
    {
        if (Objects.isNull(configurator))
        {
            logger.debug("Creating default SSLContextConfigurator (reading system properties)");
            configurator = new SSLContextConfigurator(true);
        }

        if (Objects.nonNull(keyStorePassword)) { configurator.setKeyStorePassword(keyStorePassword); }
        if (Objects.nonNull(keyPassword)) { configurator.setKeyPassword(keyPassword); }
        if (Objects.nonNull(trustStorePassword)) { configurator.setTrustStorePassword(trustStorePassword); }

        if (Objects.isNull(keyManagerFactory))
        {
            logger.debug("Creating KeyManagerFactory from Configurator");
            keyManagerFactory = configurator.createKeyManagerFactory();
        }

        if (Objects.isNull(trustManagerFactory))
        {
            logger.debug("Creating TrustManagerFactory from Configurator");
            trustManagerFactory = configurator.createTrustManagerFactory();
        }
    }

    protected void buildSslContext()
    {
        if (!protocol.isSecure())
        {
            return;
        }

        try
        {
            if (Objects.isNull(sslContext))
            {
                prepareSslContext();
                logger.debug("Creating new SSLContext with KeyManagerFactory {}, and TrustManagerFactory {}",
                             keyManagerFactory, trustManagerFactory);
                sslContext = configurator.createSSLContext(true, keyManagerFactory, trustManagerFactory);
            }

            final SecureTransport transport = (SecureTransport) protocol.getTransport();
            transport.setSSLContext(sslContext);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    protected PrivateKey parsePrivateKey(final String privateKey, final String keyAlgorithm) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        logger.debug("Decoding private key ...");
        final KeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64Utils.decodeFromString(privateKey));
        final KeyFactory kf = KeyFactory.getInstance(keyAlgorithm);
        return kf.generatePrivate(privateKeySpec);
    }

    protected PublicKey parsePublicKey(final String publicKey, final String keyAlgorithm) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        logger.debug("Decoding public key ...");
        final KeySpec publicKeySpec = new X509EncodedKeySpec(Base64Utils.decodeFromString(publicKey));
        final KeyFactory kf = KeyFactory.getInstance(keyAlgorithm);
        return kf.generatePublic(publicKeySpec);
    }

    protected Certificate[] parseCertificateChain(final String keyFormat, final String... certificates) throws CertificateException, IOException
    {
        logger.debug("Creating CertificateFactory of type {}", keyFormat);
        final CertificateFactory certificateFactory = CertificateFactory.getInstance(keyFormat);
        final Certificate[] chain = new Certificate[certificates.length];

        for (int i = 0; i < certificates.length; i++)
        {
            final byte[] certificateBytes = Base64Utils.decodeFromString(certificates[i]);
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(certificateBytes))
            {
                chain[i] = certificateFactory.generateCertificate(byteArrayInputStream);
            }
            catch (final CertificateException | IOException e)
            {
                logger.error("Unable to generate certificate from Base64: {}", certificates[i]);
                throw e;
            }
        }

        return chain;
    }

    protected void storeKeyEntry(final String alias, final PrivateKey privateKey, final Certificate[] chain)
            throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException
    {
        logger.debug("Saving PrivateKey and certificate chain in keyStore as '{}'", alias);
        final KeyManagerFactoryParameters keyManagerFactoryParameters = configurator.getKeyManagerFactoryParameters();
        final KeyStore keyStore = keyManagerFactoryParameters.getStore();

        keyStore.setKeyEntry(alias, privateKey, keyManagerFactoryParameters.getKeyPassword(), chain);
        keyManagerFactoryParameters.saveStore(keyStore);
    }

    protected void storeCertificateEntry(final String alias, final Certificate certificate)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException
    {
        if (certificate instanceof X509Certificate)
        {
            logger.debug("Saving trusted certificate '{}' as '{}'", ((X509Certificate) certificate).getSubjectX500Principal(), alias);
        }
        else
        {
            logger.debug("Saving trusted certificate (type {}) as '{}'", certificate.getType(), alias);
        }
        final KeyManagerFactoryParameters keyManagerFactoryParameters = configurator.getKeyManagerFactoryParameters();
        final KeyStore keyStore = keyManagerFactoryParameters.getStore();

        final TrustManagerFactoryParameters trustManagerFactoryParameters = configurator.getTrustManagerFactoryParameters();
        final KeyStore trustStore = trustManagerFactoryParameters.getStore();

        keyStore.setCertificateEntry(alias, certificate);
        trustStore.setCertificateEntry(alias, certificate);

        keyManagerFactoryParameters.saveStore(keyStore);
        trustManagerFactoryParameters.saveStore(trustStore);
    }

    protected void reloadSSLContext()
    {
        if (!protocol.isSecure())
        {
            return;
        }

        logger.info("Reloading SSLContext");

        keyManagerFactory = configurator.createKeyManagerFactory();
        trustManagerFactory = configurator.createTrustManagerFactory();
        sslContext = configurator.createSSLContext(true, keyManagerFactory, trustManagerFactory);

        final SecureTransport transport = (SecureTransport) protocol.getTransport();
        transport.setSSLContext(sslContext);
    }
}
