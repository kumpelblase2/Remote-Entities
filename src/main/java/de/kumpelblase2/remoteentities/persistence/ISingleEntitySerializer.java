package de.kumpelblase2.remoteentities.persistence;

public interface ISingleEntitySerializer extends IEntitySerializer
{
	public void save(EntityData inData);
	public void load(Object inParameter);
}
