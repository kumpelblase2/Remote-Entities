package de.kumpelblase2.removeentities.entities;

import org.bukkit.Bukkit;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import de.kumpelblase2.removeentities.EntityManager;
import de.kumpelblase2.removeentities.api.Fightable;
import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemoteEnderDragon extends RemoteBaseEntity implements Fightable
{
	protected boolean m_shouldDestroyBlocks = false;
	
	public RemoteEnderDragon(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteEnderDragon(int inID, RemoteEnderDragonEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.EnderDragon, inManager);
		this.m_entity = inEntity;
		
		Bukkit.getPluginManager().registerEvents(new Listener()
			{
				@EventHandler
				public void onEntityExplode(EntityExplodeEvent event)
				{
					if(event.getEntity() instanceof EnderDragon)
					{
						if(event.getEntity() == getBukkitEntity() && !shouldDestroyBlocks())
							event.setCancelled(true);
					}
					else if(event.getEntity() instanceof ComplexEntityPart)
					{
						if(((ComplexEntityPart)event.getEntity()).getParent() == getBukkitEntity() && !shouldDestroyBlocks())
							event.setCancelled(true);
					}
				}
			},
		this.m_manager.getPlugin());
	}

	@Override
	public void setMaxHealth(int inMax)
	{
		((RemoteEntityHandle)this.m_entity).setMaxHealth(inMax);
	}

	@Override
	public int getMaxHealth()
	{
		return this.m_entity.getMaxHealth();
	}
	
	public boolean shouldDestroyBlocks()
	{
		return this.m_shouldDestroyBlocks;
	}
	
	public void shouldDestroyBlocks(boolean inState)
	{
		this.m_shouldDestroyBlocks = inState;
	}

	@Override
	public void attack(LivingEntity inTarget)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loseTarget()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public LivingEntity getTarget()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
