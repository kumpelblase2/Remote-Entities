package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_5_R1.Entity;
import net.minecraft.server.v1_5_R1.EntityCreature;
import net.minecraft.server.v1_5_R1.EntityLiving;
import org.bukkit.craftbukkit.v1_5_R1.entity.CraftLivingEntity;
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
	
	public void explode()
	{
		this.explode(1);
	}
	
	public void explode(int inModifier)
	{
		if(this.m_entity == null)
			return;
		
		this.getBukkitEntity().getWorld().createExplosion(this.getBukkitEntity().getLocation(), 3F * inModifier);
		this.getBukkitEntity().setHealth(0);
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
		return "Creeper";
	}
}
