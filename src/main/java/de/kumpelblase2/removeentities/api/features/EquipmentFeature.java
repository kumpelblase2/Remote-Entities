package de.kumpelblase2.removeentities.api.features;

public interface EquipmentFeature extends Feature
{
	public Equipment getEquipment();
	public void applyEquipment(Equipment inEquipment);
}
