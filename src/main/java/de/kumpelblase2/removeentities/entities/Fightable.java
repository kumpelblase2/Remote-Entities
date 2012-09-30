package de.kumpelblase2.removeentities.entities;

import org.bukkit.entity.LivingEntity;

public interface Fightable
{
	public void attack(LivingEntity inTarget);
	public void loseTarget();
}
