package eu.arrowhead.onboarding.services;

import java.util.function.Supplier;

public interface ServiceWrapper<T> extends Supplier<T>
{
    void start() throws Exception;

    void stop() throws Exception;
}
