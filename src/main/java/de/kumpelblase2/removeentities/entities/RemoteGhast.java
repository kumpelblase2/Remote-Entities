package de.kumpelblase2.removeentities.entities;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.removeentities.EntityManager;
import de.kumpelblase2.removeentities.api.Fightable;
import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemoteGhast extends RemoteBaseEntity implements Fightable
{
	public RemoteGhast(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteGhast(int inID, RemoteGhastEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Ghast, inManager);
		this.m_entity = inEntity;
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

	@Override
	public void attack(LivingEntity inTarget)
	{
		((RemoteGhastEntity)this.m_entity).setTarget(((CraftLivingEntity)inTarget).getHandle());
		this.m_entity.c(((CraftLivingEntity)inTarget).getHandle());
	}

	@Override
	public void loseTarget()
	{
		((RemoteGhastEntity)this.m_entity).setTarget(null);
	}
	
	@Override
	public LivingEntity getTarget()
	{
		Entity target = ((RemoteGhastEntity)this.m_entity).getTarget();
		if(target != null && target instanceof EntityLiving)
			return (LivingEntity)target.getBukkitEntity();
		
		return null;	
	}
}
