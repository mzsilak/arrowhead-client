package eu.arrowhead.client.utils;

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

import static eu.arrowhead.client.utils.SSLContextConfigurator.*;

public class TrustManagerFactoryParameters extends AbstractFactoryParameters<TrustManagerFactory>
{
    private final Logger logger = LogManager.getLogger();

    public TrustManagerFactoryParameters()
    {
        super();
    }

    public TrustManagerFactoryParameters(final boolean readSystemProperties)
    {
        super(readSystemProperties);
    }

    public TrustManagerFactoryParameters(final String storeProvider, final String storeType, final char[] storePass,
                                         final String storeFile, final byte[] storeBytes,
                                         final String managerFactoryAlgorithm)
    {
        super(storeProvider, storeType, storePass, storeFile, storeBytes, managerFactoryAlgorithm);
    }

    @Override
    void loadProperties(final Properties props)
    {
        storeProvider = props.getProperty(TRUST_STORE_PROVIDER);
        storeType = props.getProperty(TRUST_STORE_TYPE);

        if (props.getProperty(TRUST_STORE_PASSWORD) != null)
        {
            storePass = props.getProperty(TRUST_STORE_PASSWORD)
                             .toCharArray();
        }
        else
        {
            storePass = null;
        }

        storeFile = props.getProperty(TRUST_STORE_FILE);
        storeBytes = null;
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
            logger.log(Level.WARN, "Can't find trust store file: {}", storeFile, e);
            if (throwException)
            {
                throw new SSLContextConfigurator.GenericStoreException(e);
            }
        }
        catch (IOException e)
        {
            logger.log(Level.WARN, "Error loading trust store from file: {}", storeFile, e);
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

}
