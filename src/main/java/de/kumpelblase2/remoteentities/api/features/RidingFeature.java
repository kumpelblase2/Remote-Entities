package de.kumpelblase2.remoteentities.api.features;

public interface RidingFeature extends Feature
{
	public boolean isPreparedToRide();
	public void setRideable(boolean inStatus);
	public int getRideableChance();
	public void increaseRideableChance(int inChance);
}