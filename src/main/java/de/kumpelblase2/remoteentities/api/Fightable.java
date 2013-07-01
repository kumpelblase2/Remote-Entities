package de.kumpelblase2.remoteentities.api;

import org.bukkit.entity.LivingEntity;

public interface Fightable
{
	/**
	 * Sets the target of the entity
	 *
	 * @param inTarget	entity to attack
	 */
	public void attack(LivingEntity inTarget);

	/**
	 * Loses the current target
	 */
	public void loseTarget();

	/**
	 * Gets the current target of the entity
	 *
	 * @return	current target
	 */
	public LivingEntity getTarget();
}