package eu.arrowhead.client.utils.security;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.KeyManagerFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Objects;
import java.util.Properties;

import static eu.arrowhead.client.utils.security.SSLContextConfigurator.*;

public class KeyManagerFactoryParameters extends AbstractFactoryParameters<KeyManagerFactory>
{
    private static final String DEFAULT_FILENAME = "keystore";
    private static final char[] DEFAULT_KEYPASS = "123456".toCharArray();

    private final Logger logger = LogManager.getLogger();

    private char[] keyPassword;

    public KeyManagerFactoryParameters()
    {
        super();
    }

    KeyManagerFactoryParameters(final boolean readSystemProperties)
    {
        super(readSystemProperties);
    }

    public KeyManagerFactoryParameters(final String storeProvider,
                                       final String storeType,
                                       final char[] keyPassword,
                                       final char[] storePass,
                                       final byte[] storeBytes,
                                       final String managerFactoryAlgorithm)
    {
        super(storeProvider, storeType, storePass, storeBytes, managerFactoryAlgorithm);
        this.keyPassword = keyPassword;
    }

    public KeyManagerFactoryParameters(final String storeProvider,
                                       final String storeType,
                                       final char[] keyPassword,
                                       final char[] storePass,
                                       final String storeFile,
                                       final String managerFactoryAlgorithm)
    {
        super(storeProvider, storeType, storePass, storeFile, managerFactoryAlgorithm);
        this.keyPassword = keyPassword;
    }

    @Override
    void loadProperties(final Properties props)
    {
        super.loadProperties(props, KEY_STORE_PROVIDER, KEY_STORE_TYPE, KEY_STORE_PASSWORD, KEY_STORE_FILE);
    }

    @Override
    KeyManagerFactory initFactory(final boolean throwException)
    {
        final KeyManagerFactory keyManagerFactory;
        KeyStore keyStore;

        try
        {
            try
            {
                keyStore = loadStore();
            }
            catch (NoSuchProviderException | KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e)
            {
                logger.error("Error during loading ... attempting to create new store", e);
                keyStore = initEmptyStore();
            }

            String kmfAlgorithm = managerFactoryAlgorithm;
            if (kmfAlgorithm == null)
            {
                kmfAlgorithm = System.getProperty(
                        KEY_FACTORY_MANAGER_ALGORITHM,
                        KeyManagerFactory.getDefaultAlgorithm());
            }

            keyManagerFactory = KeyManagerFactory.getInstance(kmfAlgorithm);
            keyManagerFactory.init(keyStore, getKeyPassword());
            return keyManagerFactory;
        }
        catch (KeyStoreException e)
        {
            logger.log(Level.WARN, "Error initializing key store", e);
            if (throwException)
            {
                throw new GenericStoreException(e);
            }
        }
        catch (CertificateException e)
        {
            logger.log(Level.WARN, "Key store certificate exception.", e);
            if (throwException)
            {
                throw new GenericStoreException(e);
            }
        }
        catch (UnrecoverableKeyException e)
        {
            logger.log(Level.WARN, "Key store unrecoverable exception.", e);
            if (throwException)
            {
                throw new GenericStoreException(e);
            }
        }
        catch (FileNotFoundException e)
        {
            logger.log(Level.WARN, "Can't find key store file: {}", storeFileName, e);
            if (throwException)
            {
                throw new GenericStoreException(e);
            }
        }
        catch (IOException e)
        {
            logger.log(Level.WARN, "Error loading key store from file: {}", storeFileName, e);
            if (throwException)
            {
                throw new GenericStoreException(e);
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            logger.log(Level.WARN, "Error initializing key manager factory (no such algorithm)", e);
            if (throwException)
            {
                throw new GenericStoreException(e);
            }
        }
        catch (NoSuchProviderException e)
        {
            logger.log(Level.WARN, "Error initializing key store (no such provider)", e);
            if (throwException)
            {
                throw new GenericStoreException(e);
            }
        }

        return null;
    }

    @Override
    public String getStoreFileName()
    {
        return getOrDefault(storeFileName, DEFAULT_FILENAME);
    }

    public char[] getKeyPassword()
    {
        final char[] pwd = getOrDefault(keyPassword, DEFAULT_KEYPASS);
        logger.trace("Returning key password (length:{})", pwd.length);
        return pwd;
    }

    public void setKeyPassword(final char[] keyPassword)
    {
        Objects.requireNonNull(keyPassword, "KeyPassword must not be null");
        logger.trace("Setting key password to (length:{})", keyPassword.length);
        this.keyPassword = keyPassword;
    }

    @Override
    protected String getStoreFactoryType()
    {
        return DEFAULT_FILENAME;
    }
}
