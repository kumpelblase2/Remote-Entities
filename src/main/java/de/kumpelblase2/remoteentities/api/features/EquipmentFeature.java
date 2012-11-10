package de.kumpelblase2.remoteentities.api.features;

public interface EquipmentFeature extends Feature
{
	/**
	 * Gets the {@link Equipment}
	 * 
	 * @return equipment
	 */
	public Equipment getEquipment();
	
	/**
	 * Applies the equipment to the entity
	 * 
	 * @param inEquipment equipment to apply
	 */
	public void applyEquipment(Equipment inEquipment);
	
	/**
	 * Updates the current equipment on the entity
	 */
	public void updateEquipment();
}
