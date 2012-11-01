package de.kumpelblase2.remoteentities.api.features;

import org.bukkit.craftbukkit.inventory.CraftInventoryCustom;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class RemoteInventoryFeature extends RemoteFeature implements InventoryFeature
{
	private CraftInventoryCustom m_inventory;
	
	public RemoteInventoryFeature(RemoteEntity inEntity)
	{
		super("INVENTORY", inEntity);
		this.m_inventory = new CraftInventoryCustom((InventoryHolder)inEntity.getHandle(), InventoryType.CHEST);
	}
	
	@Override
	public Inventory getInventory()
	{
		return this.m_inventory;
	}
}
