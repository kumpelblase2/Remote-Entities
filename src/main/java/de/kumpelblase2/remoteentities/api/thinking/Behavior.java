package de.kumpelblase2.remoteentities.api.thinking;

import org.bukkit.event.Listener;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public interface Behavior extends Listener, Runnable
{
	/**
	 * Gets the name of the behavior
	 * 
	 * @return name
	 */
	public String getName();
	
	/**
	 * Method that gets executed when the behavior gets added to the entity 
	 */
	public void onAdd();
	
	/**
	 * Method that gets executed when the behavior gets removed from the entity
	 */
	public void onRemove();
	
	/**
	 * Gets the entity having this behavior
	 * 
	 * @return entity
	 */
	public RemoteEntity getRemoteEntity();

    /**
     * Returns private data used for persistence re-instantiation
     *
     * @return Array of strings to be used as basis for deserialized constructors.
     */
    public Object[] getConstructionals();
}
