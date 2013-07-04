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
	 * Gets the chance by that the entity can now be ridden.
	 *
	 * @return  The chance
	 */
	public int getRideableChance();

	/**
	 * Sets the change by that the entity can now be ridden.
	 *
	 * @param inChance  The chance
	 */
	public void increaseRideableChance(int inChance);
}