package eu.arrowhead.client.utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Objects;
import java.util.Properties;

abstract class AbstractFactoryParameters<T>
{

    protected String storeProvider;
    protected String storeType;
    protected char[] storePass;
    protected String storeFile;
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

    AbstractFactoryParameters(final String storeProvider, final String storeType, final char[] storePass,
                              final String storeFile,
                              final byte[] storeBytes, final String managerFactoryAlgorithm)
    {
        this.storeProvider = storeProvider;
        this.storeType = storeType;
        this.storePass = storePass;
        this.storeFile = storeFile;
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
            store = KeyStore.getInstance(type, storeProvider);
        }
        else
        {
            store = KeyStore.getInstance(type);
        }
        loadBytes(storeBytes, storeFile, storePass, store);
        return store;
    }

    private static void loadBytes(final byte[] bytes, final String storeFile, final char[] password,
                                  final KeyStore store)
            throws IOException, CertificateException, NoSuchAlgorithmException
    {
        InputStream inputStream = null;
        try
        {
            if (bytes != null)
            {
                inputStream = new ByteArrayInputStream(bytes);
            }
            else if (!"NONE".equals(storeFile))
            {
                inputStream = new FileInputStream(storeFile);
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

    <T> T getOrDefault(final T value, final T defaultValue)
    {
        return Objects.nonNull(value) ? value : defaultValue;
    }

    abstract void loadProperties(final Properties properties);

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

    public char[] getStorePass()
    {
        return storePass;
    }

    public void setStorePass(final char[] storePass)
    {
        this.storePass = storePass;
    }

    public String getStoreFile()
    {
        return storeFile;
    }

    public void setStoreFile(final String storeFile)
    {
        this.storeFile = storeFile;
        this.storeBytes = null;
    }

    public byte[] getStoreBytes()
    {
        return storeBytes;
    }

    public void setStoreBytes(final byte[] storeBytes)
    {
        this.storeBytes = storeBytes;
        this.storeFile = null;
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
        return Objects.nonNull(storeBytes) || Objects.nonNull(storeFile);
    }
}
