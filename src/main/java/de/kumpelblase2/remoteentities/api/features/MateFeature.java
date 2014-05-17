package de.kumpelblase2.remoteentities.api.features;

import org.bukkit.entity.LivingEntity;

public interface MateFeature extends Feature
{
	/**
	 * Checks if the entity could be a possible partner of this entity.
	 *
	 * @param inPartner The partner to check
	 * @return True if it is possible, otherwise false
	 */
	public boolean isPossiblePartner(LivingEntity inPartner);

	/**
	 * Makes both entities mates of another.
	 *
	 * @param inPartner New mate
	 * @return True if it was successful, otherwise false
	 */
	public boolean mate(LivingEntity inPartner);

	/**
	 * Creates a baby with the current partner
	 *
	 * @return The created baby
	 */
	public LivingEntity makeBaby();

	/**
	 * Checks if the entity is ready to mate.
	 *
	 * @return true if it can, otherwise false
	 */
	public boolean isAffected();

	/**
	 * Resets the affection for this entity.
	 */
	public void resetAffection();

	/**
	 * Gets the partner of this entity if it has one.
	 *
	 * @return The partner entity
	 */
	public LivingEntity getPartner();
}