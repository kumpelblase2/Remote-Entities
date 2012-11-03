package de.kumpelblase2.remoteentities.api;

import org.bukkit.inventory.InventoryHolder;

public interface RemoteEntityHandle extends InventoryHolder
{
	public RemoteEntity getRemoteEntity();
	public void setupStandardGoals();
	public void setMaxHealth(int inHealth);
}
