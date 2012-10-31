package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityLiving;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.Fightable;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteCreeper extends RemoteBaseEntity implements Fightable
{
	public RemoteCreeper(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteCreeper(int inID, RemoteCreeperEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Creeper, inManager);
		this.m_entity = inEntity;
	}
	@Override
	public void setMaxHealth(int inMax)
	{
		this.getHandle().setMaxHealth(inMax);
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
	public RemoteCreeperEntity getHandle()
	{
		return (RemoteCreeperEntity)this.m_entity;
	}
	
	public void explode()
	{
		this.explode(1);
	}
	
	public void explode(int inModifier)
	{
		this.getBukkitEntity().getWorld().createExplosion(this.getBukkitEntity().getLocation(), 3F * inModifier);
		this.getBukkitEntity().setHealth(0);
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
