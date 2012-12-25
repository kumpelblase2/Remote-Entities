package de.kumpelblase2.remoteentities.api;

import org.bukkit.inventory.InventoryHolder;

public interface RemoteEntityHandle extends InventoryHolder
{
	/**
	 * Gets the RemoteEntity of this entity
	 * 
	 * @return	RemoteEntity
	 */
	public RemoteEntity getRemoteEntity();
	
	/**
	 * Sets up the default goals/desires for this entity 
	 */
	public void setupStandardGoals();
}
