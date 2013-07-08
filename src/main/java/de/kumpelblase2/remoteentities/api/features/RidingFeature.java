package de.kumpelblase2.remoteentities.api.features;

public interface RidingFeature extends Feature
{
	/**
	 * Checks if the entity can be ridden.
	 *
	 * @return  true if it can, false if not
	 */
	public boolean isPreparedToRide();

	/**
	 * Sets if the entity is rideable or not.
	 *
	 * @param inStatus rideable status
	 */
	public void setRideable(boolean inStatus);

	/**
	 * Gets the temper of this entity.
	 *
	 * @return  The chance
	 */
	public int getTemper();

	/**
	 * Sets the temper of the entity.
	 *
	 * @param inTemper  The temper
	 */
	public void increaseTemper(int inTemper);
}