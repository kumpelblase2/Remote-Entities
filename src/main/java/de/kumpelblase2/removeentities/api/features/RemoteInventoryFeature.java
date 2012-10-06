package de.kumpelblase2.removeentities.api.features;

import org.bukkit.craftbukkit.inventory.CraftInventoryCustom;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import de.kumpelblase2.removeentities.api.RemoteEntity;

public class RemoteInventoryFeature extends RemoteFeature implements InventoryFeature
{
	private CraftInventoryCustom m_inventory;
	
	public RemoteInventoryFeature(String inName, RemoteEntity inEntity)
	{
		super(inName, inEntity);
		this.m_inventory = new CraftInventoryCustom((InventoryHolder)inEntity.getHandle(), InventoryType.CHEST);
	}
	
	@Override
	public Inventory getInventory()
	{
		return this.m_inventory;
	}
}
