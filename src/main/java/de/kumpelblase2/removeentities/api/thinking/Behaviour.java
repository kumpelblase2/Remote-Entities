package de.kumpelblase2.removeentities.api.thinking;

import org.bukkit.event.Listener;
import de.kumpelblase2.removeentities.api.RemoteEntity;

public interface Behaviour extends Listener, Runnable
{
	public String getName();
	public void onAdd();
	public void onRemove();
	public RemoteEntity getRemoteEntity();
}
