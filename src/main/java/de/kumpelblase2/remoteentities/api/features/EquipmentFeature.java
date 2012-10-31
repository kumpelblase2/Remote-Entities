package de.kumpelblase2.remoteentities.api.features;

public interface EquipmentFeature extends Feature
{
	public Equipment getEquipment();
	public void applyEquipment(Equipment inEquipment);
}
