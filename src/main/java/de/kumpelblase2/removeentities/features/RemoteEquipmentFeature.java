package de.kumpelblase2.removeentities.features;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import de.kumpelblase2.removeentities.entities.RemoteEntity;

public class RemoteEquipmentFeature extends RemoteFeature implements EquipmentFeature
{
	private Equipment m_equipment;
	
	public RemoteEquipmentFeature(String inName, RemoteEntity inEntity)
	{
		super(inName, inEntity);
		this.m_equipment = new Equipment(new ItemStack(Material.AIR), new ItemStack[4]);
	}

	@Override
	public Equipment getEquipment()
	{
		return this.m_equipment;
	}

	@Override
	public void applyEquipment(Equipment inEquipment)
	{
		this.m_equipment = inEquipment;
		//TODO
	}
}
