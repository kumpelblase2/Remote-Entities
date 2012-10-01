package de.kumpelblase2.removeentities.entities;

import org.bukkit.inventory.InventoryHolder;

public interface RemoteEntityHandle extends InventoryHolder
{
	public RemoteEntity getRemoteEntity();
}
