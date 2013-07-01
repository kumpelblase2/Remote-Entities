package de.kumpelblase2.remoteentities.api.features;

import org.bukkit.entity.Player;

public interface TamingFeature extends Feature
{
	/**
	 * Returns whether the entity is tamed already or not
	 *
	 * @return true if tamed, false if not
	 */
	public boolean isTamed();

	/**
	 * Loses the current tamer
	 */
	public void untame();

	/**
	 * Sets the owner/tamer of the entity
	 *
	 * @param inPlayer tamer
	 */
	public void tame(Player inPlayer);

	/**
	 * Gets the owner/tamer of the entity
	 *
	 * @return tamer
	 */
	public Player getTamer();
}