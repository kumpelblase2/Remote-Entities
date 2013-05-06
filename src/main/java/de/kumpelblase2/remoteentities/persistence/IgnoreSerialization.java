package de.kumpelblase2.remoteentities.persistence;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IgnoreSerialization
{
}
