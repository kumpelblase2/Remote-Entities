package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityLiving;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.*;

public class RemoteZombie extends RemoteBaseEntity implements RemoteEntity, Fightable
{	
	public RemoteZombie(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteZombie(int inID, RemoteZombieEntity inEntitiy, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Zombie, inManager);
		this.m_entity = inEntitiy;
	}

	@Override
	public void setMaxHealth(int inMax)
	{
		if(this.m_entity == null)
			return;
		
		((RemoteZombieEntity)this.m_entity).setMaxHealth(inMax);
	}

	@Override
	public void attack(LivingEntity inTarget)
	{
		if(this.m_entity == null)
			return;
		
		((EntityCreature)this.m_entity).setTarget(((CraftLivingEntity)inTarget).getHandle());
		this.m_entity.c(((CraftLivingEntity)inTarget).getHandle());
	}

	@Override
	public void loseTarget()
	{
		if(this.m_entity == null)
			return;
		
		((EntityCreature)this.m_entity).setTarget(null);
	}

	@Override
	public LivingEntity getTarget()
	{
		if(this.m_entity == null)
			return null;
		
		Entity target = ((EntityCreature)this.m_entity).l();
		if(target != null && target instanceof EntityLiving)
			return (LivingEntity)target.getBukkitEntity();
		
		return null;	
	}

	@Override
	public String getNativeEntityName()
	{
		return "Zombie";
	}
}
