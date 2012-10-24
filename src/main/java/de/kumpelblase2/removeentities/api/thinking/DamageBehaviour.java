package de.kumpelblase2.removeentities.api.thinking;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import de.kumpelblase2.removeentities.api.RemoteEntity;

public abstract class DamageBehaviour implements Behaviour
{
	protected final String NAME = "Damage";
	private final RemoteEntity m_entity;
	
	public DamageBehaviour(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
	}
	
	@Override
	public void onRemove()
	{
		EntityDamageEvent.getHandlerList().unregister(this);
	}
	
	public void onAdd()
	{
		Bukkit.getPluginManager().registerEvents(this, this.m_entity.getManager().getPlugin());
	}
	
	@Override
	public void run()
	{
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	private void onDamageEvent(EntityDamageEvent event)
	{
		if(this.m_entity.getMind().canFeel())
			this.onDamage(event);
	}

	public abstract void onDamage(EntityDamageEvent event);
	
	@Override
	public String getName()
	{
		return this.NAME;
	}
}
