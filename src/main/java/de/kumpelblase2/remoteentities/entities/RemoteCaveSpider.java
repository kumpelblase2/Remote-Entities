package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityLiving;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.Fightable;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteCaveSpider extends RemoteBaseEntity implements Fightable
{
	public RemoteCaveSpider(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteCaveSpider(int inID, RemoteCaveSpiderEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.CaveSpider, inManager);
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
		((EntityCreature)this.m_entity).setTarget(((CraftLivingEntity)inTarget).getHandle());
		this.m_entity.c(((CraftLivingEntity)inTarget).getHandle());
	}

	@Override
	public void loseTarget()
	{
		((EntityCreature)this.m_entity).setTarget(null);
	}
	
	@Override
	public LivingEntity getTarget()
	{
		Entity target = ((EntityCreature)this.m_entity).m();
		if(target != null && target instanceof EntityLiving)
			return (LivingEntity)target.getBukkitEntity();
		
		return null;	
	}
}
