package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_4_6.Entity;
import net.minecraft.server.v1_4_6.EntityCreature;
import net.minecraft.server.v1_4_6.EntityLiving;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.Fightable;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteIronGolem extends RemoteBaseEntity implements Fightable
{
	public RemoteIronGolem(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteIronGolem(int inID, RemoteIronGolemEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.IronGolem, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public void attack(LivingEntity inTarget)
	{
		if(this.m_entity == null)
			return;
		
		((EntityCreature)this.m_entity).setTarget(((CraftLivingEntity)inTarget).getHandle());
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
		return "VillagerGolem";
	}
}
