package de.kumpelblase2.remoteentities.api.thinking;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public abstract class DamageBehavior extends EventBehavior
{
	public DamageBehavior(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_name = "Damage";
	}

	@Override
	public void onRemove()
	{
		EntityDamageEvent.getHandlerList().unregister(this);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onDamageEvent(EntityDamageEvent event)
	{
		if(!event.getEntity().equals(this.m_entity.getBukkitEntity()))
			return;

		if(this.m_entity.getMind().canFeel())
			this.onDamage(event);
	}

	/**
	 * Called when the entity get damaged by something
	 *
	 * @param event damage event
	 */
	public abstract void onDamage(EntityDamageEvent event);
}