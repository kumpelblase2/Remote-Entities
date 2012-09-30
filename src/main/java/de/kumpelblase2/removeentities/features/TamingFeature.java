package de.kumpelblase2.removeentities.features;

import org.bukkit.entity.Player;

public interface TamingFeature extends Feature
{
	public boolean isTamed();
	public void untame();
	public void tame(Player inPlayer);
}
