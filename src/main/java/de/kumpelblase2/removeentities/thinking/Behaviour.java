package de.kumpelblase2.removeentities.thinking;

import org.bukkit.event.Listener;

public interface Behaviour extends Listener, Runnable
{
	public String getName();
	public void onAdd();
	public void onRemove();
}
