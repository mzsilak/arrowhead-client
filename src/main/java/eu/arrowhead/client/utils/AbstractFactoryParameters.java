package eu.arrowhead.client.utils;

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
    }

    AbstractFactoryParameters(final String storeProvider,
                              final String storeType,
                              final char[] storePassword,
                              final String storeFileName,
                              final String managerFactoryAlgorithm)
    {
        this.storeProvider = storeProvider;
        this.storeType = storeType;
        this.storePassword = storePassword;
        this.storeFileName = storeFileName;
        this.managerFactoryAlgorithm = managerFactoryAlgorithm;
    }


    AbstractFactoryParameters(final String storeProvider,
                              final String storeType,
                              final char[] storePassword,
                              final byte[] storeBytes,
                              final String managerFactoryAlgorithm)
    {
        this.storeProvider = storeProvider;
        this.storeType = storeType;
        this.storePassword = storePassword;
        this.storeBytes = storeBytes;
        this.managerFactoryAlgorithm = managerFactoryAlgorithm;
    }


    KeyStore loadStore()
            throws NoSuchProviderException, KeyStoreException, CertificateException, NoSuchAlgorithmException,
                   IOException
    {
        final String type = getOrDefault(storeType, KeyStore.getDefaultType());
        KeyStore store = null;

        if (Objects.nonNull(storeProvider))
        {
            logger.debug("Creating instance of type {} with provider {}", type, storeProvider);
            store = KeyStore.getInstance(type, storeProvider);
        }
        else
        {
            logger.debug("Creating instance of type {} with default provider", type);
            store = KeyStore.getInstance(type);
        }
        loadBytes(storeBytes, getStoreFileName(), getStorePassword(), store);
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
            else
            {
                logger.debug("Neither file nor bytes provided. Creating empty store.");
                saveStore(store, password, storeFileName);
            }

            store.load(inputStream, password);
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

    private void saveStore(final KeyStore store, final char[] password, final String filename) throws IOException
    {
        final char[] pwd = Objects.nonNull(password) ? password : filename.toCharArray();
        try (final FileOutputStream fileOutputStream = new FileOutputStream(filename))
        {
            store.load(null, pwd);
            store.store(fileOutputStream, pwd);
            setStoreFileName(filename);
            setStorePassword(pwd);
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
        else
        {
            setStorePassword(null);
        }

        setStoreFileName(properties.getProperty(propertyNameFile));
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
        return storePassword;
    }

    public void setStorePassword(final char[] storePassword)
    {
        this.storePassword = storePassword;
    }

    public String getStoreFileName()
    {
        return storeFileName;
    }

    public void setStoreFileName(final String storeFileName)
    {
        this.storeFileName = storeFileName;
        this.storeBytes = null;
    }

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
