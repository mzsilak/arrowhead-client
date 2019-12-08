package eu.arrowhead.client.utils.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Objects;
import java.util.Properties;

abstract class AbstractFactoryParameters<T>
{
    private final Logger logger = LogManager.getLogger();

    protected String storeProvider;
    protected String storeType;
    protected char[] storePassword;
    protected String storeFileName;
    protected byte[] storeBytes;
    protected String managerFactoryAlgorithm;

    private KeyStore store;

    AbstractFactoryParameters()
    {
        super();
    }

    AbstractFactoryParameters(final boolean readSystemProperties)
    {
        super();
        if (readSystemProperties)
        {
            loadProperties(System.getProperties());
        }

        this.storeType = getOrDefault(storeType, KeyStore.getDefaultType());
        final String provider = getOrDefault(storeProvider, "default");
        logger.debug("Creating instance {} of type {} with provider {}", getClass().getSimpleName(), storeType, provider);
    }

    AbstractFactoryParameters(final String storeProvider,
                              final String storeType,
                              final char[] storePassword,
                              final String storeFileName,
                              final String managerFactoryAlgorithm)
    {
        this.storeProvider = storeProvider;
        this.storeType = getOrDefault(storeType, KeyStore.getDefaultType());
        this.storePassword = storePassword;
        this.storeFileName = storeFileName;
        this.managerFactoryAlgorithm = managerFactoryAlgorithm;
        final String provider = getOrDefault(storeProvider, "default");
        logger.debug("Creating instance {} of type {} with provider {}", getClass().getSimpleName(), storeType, provider);
    }


    AbstractFactoryParameters(final String storeProvider,
                              final String storeType,
                              final char[] storePassword,
                              final byte[] storeBytes,
                              final String managerFactoryAlgorithm)
    {
        this.storeProvider = storeProvider;
        this.storeType = getOrDefault(storeType, KeyStore.getDefaultType());
        this.storePassword = storePassword;
        this.storeBytes = storeBytes;
        this.managerFactoryAlgorithm = managerFactoryAlgorithm;
        final String provider = getOrDefault(storeProvider, "default");
        logger.debug("Creating instance {} of type {} with provider {}", getClass().getSimpleName(), storeType, provider);
    }

    protected KeyStore initEmptyStore() throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, NoSuchProviderException
    {
        final KeyStore store;

        if (Objects.nonNull(storeProvider))
        {
            store = KeyStore.getInstance(storeType, storeProvider);
        }
        else
        {
            store = KeyStore.getInstance(storeType);
        }

        logger.trace("Initializing empty Store ...");
        store.load(null, null);
        return store;
    }

    public KeyStore getStore() throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException
    {
        if (Objects.isNull(store))
        {
            loadStore();
        }
        return store;
    }

    public KeyStore loadStore()
            throws NoSuchProviderException, KeyStoreException, CertificateException, NoSuchAlgorithmException,
                   IOException
    {
        logger.trace("Loading Store for {}", getClass().getSimpleName());
        final KeyStore store = initEmptyStore();
        loadBytes(storeBytes, getStoreFileName(), getStorePassword(), store);
        this.store = store;
        return store;
    }

    private boolean fileExists(final String storeFileName)
    {
        return Files.exists(Paths.get(storeFileName));
    }

    private void loadBytes(final byte[] bytes, final String storeFileName, final char[] password,
                           final KeyStore store)
            throws IOException, CertificateException, NoSuchAlgorithmException
    {
        InputStream inputStream = null;
        try
        {
            if (Objects.nonNull(bytes))
            {
                logger.debug("Loading store through byte array (size={})", bytes.length);
                inputStream = new ByteArrayInputStream(bytes);
            }
            else if (Objects.nonNull(storeFileName) && fileExists(storeFileName))
            {
                logger.debug("Loading store through file (name={})", storeFileName);
                inputStream = new FileInputStream(storeFileName);
            }
           /* else
            {
                logger.debug("Neither file nor bytes provided. Creating empty store  (name={})", getStoreFileName());
                saveStore(store);
                logger.trace("Loading store again from file");
                inputStream = new FileInputStream(getStoreFileName());
            }*/

            store.load(inputStream, getStorePassword());
            this.store = store;
        }
        finally
        {
            try
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }
            }
            catch (IOException ignored)
            {
            }
        }
    }

    public void saveStore(final KeyStore store) throws IOException
    {
        try (final FileOutputStream fileOutputStream = new FileOutputStream(getStoreFileName()))
        {
            store.store(fileOutputStream, getStorePassword());
            this.store = store;
        }
        catch (final IOException e)
        {
            logger.error("Unable to save the store to disk: {}", e.getMessage());
            throw e;
        }
        catch (final Exception e)
        {
            logger.error("Unable to save the store to disk: {}", e.getMessage());
            throw new IOException(e);
        }
    }

    <T> T getOrDefault(final T value, final T defaultValue)
    {
        return Objects.nonNull(value) ? value : defaultValue;
    }

    abstract void loadProperties(final Properties properties);

    protected void loadProperties(final Properties properties,
                                  final String propertyNameProvider,
                                  final String propertyNameType,
                                  final String propertyNamePassword,
                                  final String propertyNameFile)
    {

        storeProvider = properties.getProperty(propertyNameProvider);
        storeType = properties.getProperty(propertyNameType);

        if (properties.getProperty(propertyNamePassword) != null)
        {
            setStorePassword(properties.getProperty(propertyNamePassword).toCharArray());
        }

        storeFileName = properties.getProperty(propertyNameFile);
        if (Objects.nonNull(storeFileName))
        { storeBytes = null; }
    }

    abstract T initFactory(final boolean throwException);

    public String getStoreProvider()
    {
        return storeProvider;
    }

    public void setStoreProvider(final String storeProvider)
    {
        this.storeProvider = storeProvider;
    }

    public String getStoreType()
    {
        return storeType;
    }

    public void setStoreType(final String storeType)
    {
        this.storeType = storeType;
    }

    public char[] getStorePassword()
    {
        final char[] pwd = getOrDefault(storePassword, getStoreFileName().toCharArray());
        logger.trace("Returning store password for '{}' (length:{})", getStoreFactoryType(), pwd.length);
        return pwd;
    }

    public void setStorePassword(final char[] storePassword)
    {
        Objects.requireNonNull(storePassword, "StorePassword must not be null");
        logger.trace("Setting store password for '{}' to (length:{})", getStoreFactoryType(), storePassword.length);
        this.storePassword = storePassword;
    }

    public abstract String getStoreFileName();

    public void setStoreFileName(final String storeFileName)
    {
        logger.trace("Setting filename for '{}' to '{}'", getStoreFactoryType(), storeFileName);
        this.storeFileName = storeFileName;
        this.storeBytes = null;
    }

    protected abstract String getStoreFactoryType();

    public byte[] getStoreBytes()
    {
        return storeBytes;
    }

    public void setStoreBytes(final byte[] storeBytes)
    {
        this.storeBytes = storeBytes;
        this.storeFileName = null;
    }

    public String getManagerFactoryAlgorithm()
    {
        return managerFactoryAlgorithm;
    }

    public void setManagerFactoryAlgorithm(final String managerFactoryAlgorithm)
    {
        this.managerFactoryAlgorithm = managerFactoryAlgorithm;
    }

    boolean hasFileOrBytes()
    {
        return Objects.nonNull(storeBytes) || Objects.nonNull(getStoreFileName());
    }
}
