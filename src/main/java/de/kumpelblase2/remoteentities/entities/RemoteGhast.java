package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_4_R1.Entity;
import net.minecraft.server.v1_4_R1.EntityLiving;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.Fightable;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

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
	public void attack(LivingEntity inTarget)
	{
		if(this.m_entity == null)
			return;
		
		((RemoteGhastEntity)this.m_entity).setTarget(((CraftLivingEntity)inTarget).getHandle());
		this.m_entity.c(((CraftLivingEntity)inTarget).getHandle());
	}

	@Override
	public void loseTarget()
	{
		if(this.m_entity == null)
			return;
		
		((RemoteGhastEntity)this.m_entity).setTarget(null);
	}
	
	@Override
	public LivingEntity getTarget()
	{
		if(this.m_entity == null)
			return null;
		
		Entity target = ((RemoteGhastEntity)this.m_entity).getTarget();
		if(target != null && target instanceof EntityLiving)
			return (LivingEntity)target.getBukkitEntity();
		
		return null;	
	}

	@Override
	public String getNativeEntityName()
	{
		return "Ghast";
	}
}
