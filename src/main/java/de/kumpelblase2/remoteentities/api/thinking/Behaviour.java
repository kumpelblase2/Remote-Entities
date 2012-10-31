package de.kumpelblase2.remoteentities.api.thinking;

import org.bukkit.event.Listener;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public interface Behaviour extends Listener, Runnable
{
	public String getName();
	public void onAdd();
	public void onRemove();
	public RemoteEntity getRemoteEntity();
}
