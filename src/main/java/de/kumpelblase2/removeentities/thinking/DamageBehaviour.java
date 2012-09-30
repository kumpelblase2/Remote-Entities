package de.kumpelblase2.removeentities.thinking;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import de.kumpelblase2.removeentities.EntityManager;

public abstract class DamageBehaviour implements Behaviour
{
	protected final String NAME = "Damage";
	
	@Override
	public void onRemove()
	{
		EntityDamageEvent.getHandlerList().unregister(this);
	}
	
	public void onAdd()
	{
		Bukkit.getPluginManager().registerEvents(this, EntityManager.getInstance().getPlugin());
	}
	
	@Override
	public void run()
	{
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	private void onDamageEvent(EntityDamageEvent event)
	{
		this.onDamage(event);
	}

	public abstract void onDamage(EntityDamageEvent event);
	
	@Override
	public String getName()
	{
		return this.NAME;
	}
}
