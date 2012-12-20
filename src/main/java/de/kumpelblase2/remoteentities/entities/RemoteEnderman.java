package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_4_5.EntityLiving;
import org.bukkit.craftbukkit.v1_4_5.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.Fightable;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireAttackTarget;

public class RemoteEnderman extends RemoteBaseEntity implements Fightable
{
	protected boolean m_hadAttackDesire;
	
	public RemoteEnderman(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteEnderman(int inID, RemoteEndermanEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Enderman, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public void setMaxHealth(int inMax)
	{
		if(this.m_entity == null)
			return;
		
		((RemoteEntityHandle)this.m_entity).setMaxHealth(inMax);
	}

	@Override
	public void attack(LivingEntity inTarget)
	{
		if(this.m_entity == null)
			return;
		
		this.m_hadAttackDesire = this.getMind().getActionDesire(DesireAttackTarget.class) != null;
		if(!this.m_hadAttackDesire)
			this.getMind().addActionDesire(new DesireAttackTarget(this, 16, false, false), this.getMind().getHighestActionPriority() + 1);
		
		this.getHandle().b(((CraftLivingEntity)inTarget).getHandle());
	}

	@Override
	public void loseTarget()
	{
		if(this.m_entity == null)
			return;
		
		this.getHandle().b((EntityLiving)null);
		if(!this.m_hadAttackDesire)
			this.getMind().removeActionDesire(DesireAttackTarget.class);
	}

	@Override
	public LivingEntity getTarget()
	{
		if(this.m_entity == null)
			return null;
		
		EntityLiving target = this.m_entity.aG();
		if(target != null)
			return (LivingEntity)target.getBukkitEntity();
		
		return null;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Enderman";
	}
}
