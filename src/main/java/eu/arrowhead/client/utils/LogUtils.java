package eu.arrowhead.client.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class LogUtils
{
    private static final int MAX_STACKTRACE_ELEMENTS_SHORT = 5;
    private static final int MAX_STACKTRACE_ELEMENTS_LONG = 20;
    private static final int MAX_DEPTH_STACKTRACE = 5;
    private static final String INITIAL_PREFIX = "Exception in thread \"{}\" {}";
    private static final String CAUSED_BY_PREFIX = "Caused by: {}";

    public static void printShortStackTrace(final Logger logger, final Level level, final Throwable throwable)
    {
        printStackTrace(logger, level, MAX_STACKTRACE_ELEMENTS_SHORT, MAX_DEPTH_STACKTRACE, throwable);
    }

    public static void printLongStackTrace(final Logger logger, final Level level, final Throwable throwable)
    {
        printStackTrace(logger, level, MAX_STACKTRACE_ELEMENTS_LONG, MAX_DEPTH_STACKTRACE, throwable);
    }

    private static void printStackTrace(final Logger logger, final Level level, final int maxElements, final int depth,
                                        final Throwable root)
    {
        Objects.requireNonNull(logger, "Logger must not be null");
        Objects.requireNonNull(level, "Level must not be null");
        Objects.requireNonNull(root, "Throwable must not be null");


        logger.log(level, INITIAL_PREFIX, Thread.currentThread().getName(), exceptionAndType(root));
        printElements(logger, level, maxElements, root.getStackTrace());

        Throwable current = root.getCause();
        if (Objects.nonNull(current))
        {
            for (int i = 0; i < depth; i++)
            {
                logger.log(level, CAUSED_BY_PREFIX, exceptionAndType(current));
                printElements(logger, level, maxElements, current.getStackTrace());
                if (Objects.equals(current, current.getCause())
                        || Objects.isNull(current.getCause()))
                {
                    break;
                }
                current = current.getCause();
            }
        }
    }


    private static void printElements(final Logger logger, final Level level, final int maxElements,
                                      final StackTraceElement[] elements)
    {
        int size = Math.min(elements.length, maxElements);
        for (int i = 0; i < size; i++)
        {
            logger.log(level, stackTraceElement(elements[i]));
        }
        if (elements.length > size)
        {
            logger.log(level, "\t... {} more", elements.length - size);
        }
    }

    private static String exceptionAndType(final Throwable throwable)
    {
        return throwable.getClass() + ": " + throwable.getMessage();
    }

    private static String stackTraceElement(final StackTraceElement element)
    {
        final StringBuilder sb = new StringBuilder("\tat ");
        sb.append(element.getClassName()).append('.').append(element.getMethodName());
        sb.append('(').append(element.getFileName()).append(':').append(element.getLineNumber()).append(')');
        return sb.toString();
    }
}
