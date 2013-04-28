package de.kumpelblase2.remoteentities.persistence;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IgnoreSerialization
{
}
