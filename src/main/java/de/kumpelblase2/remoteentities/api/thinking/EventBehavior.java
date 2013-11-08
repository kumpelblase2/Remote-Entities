package de.kumpelblase2.remoteentities.api.thinking;

import org.bukkit.Bukkit;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public abstract class EventBehavior extends BaseBehavior
{
	public EventBehavior(RemoteEntity inEntity)
	{
		super(inEntity);
	}

	@Override
	public void onAdd()
	{
		Bukkit.getPluginManager().registerEvents(this, this.getRemoteEntity().getManager().getPlugin());
	}
}