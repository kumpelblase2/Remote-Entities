package de.kumpelblase2.remoteentities.persistence;

public @interface SerializeAs
{
	public int pos();
	public String special() default "";
}
