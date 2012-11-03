package de.kumpelblase2.remoteentities.api.features;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class RemoteEquipmentFeature extends RemoteFeature implements EquipmentFeature
{
	private Equipment m_equipment;
	
	public RemoteEquipmentFeature(RemoteEntity inEntity)
	{
		super("EQUIPMENT", inEntity);
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
		if(this.m_entity.getBukkitEntity() instanceof HumanEntity)
		{
			HumanEntity human = (HumanEntity)this.m_entity.getBukkitEntity();
			human.setItemInHand(this.m_equipment.getItemInHand());
			human.getInventory().setArmorContents(this.m_equipment.getArmorParts());
		}
	}

	@Override
	public void updateEquipment()
	{
		this.applyEquipment(this.getEquipment());
	}
}
