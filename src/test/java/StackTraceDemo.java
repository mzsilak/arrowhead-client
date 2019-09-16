import eu.arrowhead.client.utils.LogUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class StackTraceDemo
{

    public static void main(String[] args)
    {
        try
        {
            new StackTraceDemo().startDemo();
        }
        catch (Exception e)
        {
            LogUtils.printShortStackTrace(LogManager.getLogger(), Level.INFO, e);
        }
    }

    private void startDemo() throws Exception
    {
        final Exception exception = new Exception("root exception");
        Exception child = exception;

        for (int i = 0; i < 3; i++)
        {
            try
            {
                method1();
            }
            catch (Exception e)
            {
                child.initCause(e);
                child = e;
            }
        }

        throw exception;
    }

    private void method1() throws Exception
    {
        method2();
    }

    private void method2() throws Exception
    {
        method3();
    }

    private void method3() throws Exception
    {
        method4();
    }

    private void method4() throws Exception
    {
        throw new Exception("this is a test");
    }
}
