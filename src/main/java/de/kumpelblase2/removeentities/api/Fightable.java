package de.kumpelblase2.removeentities.api;

import org.bukkit.entity.LivingEntity;

public interface Fightable
{
	public void attack(LivingEntity inTarget);
	public void loseTarget();
	public LivingEntity getTarget();
}
