package de.kumpelblase2.remoteentities.api.features;

import net.minecraft.server.EntityLiving;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
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
		this.loadEquipment();
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
		else
		{
			EntityLiving entity = this.m_entity.getHandle();
			entity.setEquipment(0, (this.m_equipment.getItemInHand() != null ? (new CraftItemStack(this.m_equipment.getItemInHand())).getHandle() : null));
			for(int i = 4, j = 0; i >= 1; i--, j++)
			{
				entity.setEquipment(i, (this.m_equipment.getArmorParts()[j] != null ? (new CraftItemStack(this.m_equipment.getArmorParts()[j])).getHandle() : null));
			}
		}
	}

	@Override
	public void updateEquipment()
	{
		this.applyEquipment(this.getEquipment());
	}
	
	public void loadEquipment()
	{
		if(this.m_entity.getHandle() == null)
			return;
		
		this.m_equipment.setItemInHand(new CraftItemStack(this.m_entity.getHandle().getEquipment(0)));
		for(int i = 0, j = 4; i < 4; i++, j--)
		{
			this.m_equipment.getArmorParts()[i] = new CraftItemStack(this.m_entity.getHandle().getEquipment(j));
		}
	}
}
