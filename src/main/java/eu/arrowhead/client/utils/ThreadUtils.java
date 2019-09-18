package eu.arrowhead.client.utils;

import java.util.concurrent.TimeUnit;

public class ThreadUtils
{
    public static void sleep(final long delay, final TimeUnit timeUnit)
    {
        try
        {
            timeUnit.sleep(delay);
        }
        catch (InterruptedException e)
        {
            // noop
        }
    }

    public static void sleepMillis(final long delay)
    {
        sleep(delay, TimeUnit.MILLISECONDS);
    }

    public static void sleepSeconds(final long delay)
    {
        sleep(delay, TimeUnit.SECONDS);
    }
}
