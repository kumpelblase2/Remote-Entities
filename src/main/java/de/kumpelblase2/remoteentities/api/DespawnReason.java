package de.kumpelblase2.remoteentities.api;

public enum DespawnReason
{
	/**
	 * When the entity dies normally
	 */
	DEATH,
	/**
	 * When the owner plugin gets disabled
	 */
	PLUGIN_DISABLE,
	/**
	 * When a plugin request despawn
	 */
	CUSTOM,
	/**
	 * When the name of the entitiy changed
	 */
	NAME_CHANGE
}
