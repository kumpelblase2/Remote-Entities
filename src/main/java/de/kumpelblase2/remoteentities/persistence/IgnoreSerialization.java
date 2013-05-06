package de.kumpelblase2.remoteentities.persistence;

import java.lang.annotation.*;

/**
 * Using this annotation, the class this is applied to won't get saved when the entity is saved.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IgnoreSerialization
{
}
