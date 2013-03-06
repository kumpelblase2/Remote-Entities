package de.kumpelblase2.remoteentities.api.thinking;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.ParameterData;

public interface Desire
{
	/**
	 * Gets the entity having this desire
	 * 
	 * @return entity
	 */
	public RemoteEntity getRemoteEntity();
	
	/**
	 * Gets the type
	 * 
	 * @return type
	 */
	public int getType();
	
	/**
	 * Sets the type
	 * 
	 * @param inType type
	 */
	public void setType(int inType);
	
	/**
	 * Returns whether this desire is continuous or not
	 * 
	 * @return continuous
	 */
	public boolean isContinuous();
	
	/**
	 * Method that gets called when the desire starts executing 
	 */
	public void startExecuting();
	
	/**
	 * Method that gets called when the desire stops executing
	 */
	public void stopExecuting();
	
	/**
	 * Method to update the task
	 * 
	 * @return true if updated successfully, false if not
	 */
	public boolean update();
	
	/**
	 * Checks whether the desire should execute or not
	 * 
	 * @return true if it should execute, false if not
	 */
	public boolean shouldExecute();
	
	/**
	 * Checks whether the desire should continue or not
	 *  
	 * @return true if it can continue, false if not
	 */
	public boolean canContinue();
	
	/**
	 * Returns all the parameters that can be serialized
	 * 
	 * @return Serializeable parameters
	 */
	public ParameterData[] getSerializeableData();
}
