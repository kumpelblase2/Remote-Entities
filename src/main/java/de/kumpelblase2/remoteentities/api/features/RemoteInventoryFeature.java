package de.kumpelblase2.remoteentities.api.features;

import org.bukkit.craftbukkit.v1_5_R3.inventory.CraftInventoryCustom;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class RemoteInventoryFeature extends RemoteFeature implements InventoryFeature
{
	private CraftInventoryCustom m_inventory;
	
	public RemoteInventoryFeature(RemoteEntity inEntity)
	{
		this(inEntity, 36);
	}
	
	public RemoteInventoryFeature(RemoteEntity inEntity, int inSize)
	{
		super("INVENTORY", inEntity);
		this.m_inventory = new CraftInventoryCustom((InventoryHolder)inEntity.getHandle(), inSize);
	}
	
	@Override
	public Inventory getInventory()
	{
		return this.m_inventory;
	}
}
