package eu.arrowhead.client.utils.security;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.TrustManagerFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Properties;

import static eu.arrowhead.client.utils.security.SSLContextConfigurator.*;

public class TrustManagerFactoryParameters extends AbstractFactoryParameters<TrustManagerFactory>
{
    private static final String DEFAULT_FILENAME = "truststore";

    private final Logger logger = LogManager.getLogger();

    public TrustManagerFactoryParameters()
    {
        super();
    }

    TrustManagerFactoryParameters(final boolean readSystemProperties)
    {
        super(readSystemProperties);
    }

    public TrustManagerFactoryParameters(final String storeProvider,
                                         final String storeType,
                                         final char[] storePass,
                                         final String storeFile,
                                         final String managerFactoryAlgorithm)
    {
        super(storeProvider, storeType, storePass, storeFile, managerFactoryAlgorithm);
    }

    public TrustManagerFactoryParameters(final String storeProvider,
                                         final String storeType,
                                         final char[] storePass,
                                         final byte[] storeBytes,
                                         final String managerFactoryAlgorithm)
    {
        super(storeProvider, storeType, storePass, storeBytes, managerFactoryAlgorithm);
    }

    @Override
    void loadProperties(final Properties props)
    {
        super.loadProperties(props, TRUST_STORE_PROVIDER, TRUST_STORE_TYPE, TRUST_STORE_PASSWORD, TRUST_STORE_FILE);
    }

    @Override
    TrustManagerFactory initFactory(final boolean throwException)
    {
        final TrustManagerFactory trustManagerFactory;
        final KeyStore trustStore;

        try
        {
            trustStore = loadStore();

            String tmfAlgorithm = managerFactoryAlgorithm;
            if (tmfAlgorithm == null)
            {
                tmfAlgorithm = System.getProperty(
                        TRUST_FACTORY_MANAGER_ALGORITHM,
                        TrustManagerFactory.getDefaultAlgorithm());
            }

            trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
            trustManagerFactory.init(trustStore);
            return trustManagerFactory;
        }
        catch (KeyStoreException e)
        {
            logger.log(Level.WARN, "Error initializing trust store", e);
            if (throwException)
            {
                throw new SSLContextConfigurator.GenericStoreException(e);
            }
        }
        catch (CertificateException e)
        {
            logger.log(Level.WARN, "Trust store certificate exception.", e);
            if (throwException)
            {
                throw new SSLContextConfigurator.GenericStoreException(e);
            }
        }
        catch (FileNotFoundException e)
        {
            logger.log(Level.WARN, "Can't find trust store file: {}", storeFileName, e);
            if (throwException)
            {
                throw new SSLContextConfigurator.GenericStoreException(e);
            }
        }
        catch (IOException e)
        {
            logger.log(Level.WARN, "Error loading trust store from file: {}", storeFileName, e);
            if (throwException)
            {
                throw new SSLContextConfigurator.GenericStoreException(e);
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            logger.log(Level.WARN, "Error initializing trust manager factory (no such algorithm)", e);
            if (throwException)
            {
                throw new SSLContextConfigurator.GenericStoreException(e);
            }
        }
        catch (NoSuchProviderException e)
        {
            logger.log(Level.WARN, "Error initializing trust store (no such provider)", e);
            if (throwException)
            {
                throw new SSLContextConfigurator.GenericStoreException(e);
            }
        }

        return null;
    }

    @Override
    public String getStoreFileName()
    {
        return getOrDefault(storeFileName, DEFAULT_FILENAME);
    }

    @Override
    protected String getStoreFactoryType()
    {
        return DEFAULT_FILENAME;
    }
}
