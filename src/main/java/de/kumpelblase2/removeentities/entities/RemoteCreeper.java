package de.kumpelblase2.removeentities.entities;

import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.removeentities.api.Fightable;
import de.kumpelblase2.removeentities.api.RemoteEntityType;

public class RemoteCreeper extends RemoteBaseEntity implements Fightable
{
	public RemoteCreeper(int inID)
	{
		this(inID, null);
	}
	
	public RemoteCreeper(int inID, RemoteCreeperEntity inEntity)
	{
		super(inID, RemoteEntityType.Creeper);
		this.m_entity = inEntity;
		this.m_speed = 0.3F;
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
	}

	@Override
	public void loseTarget()
	{
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
}
