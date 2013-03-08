package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_4_R1.EntityLiving;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.Fightable;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireFindAttackingTarget;

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
	public void attack(LivingEntity inTarget)
	{
		if(this.m_entity == null)
			return;
		
		this.m_hadAttackDesire = this.getMind().getTargetingDesire(DesireFindAttackingTarget.class) != null;
		if(!this.m_hadAttackDesire)
			this.getMind().addTargetingDesire(new DesireFindAttackingTarget(this, 16, false, false), this.getMind().getHighestTargetingPriority() + 1);
		
		this.getHandle().setGoalTarget(((CraftLivingEntity)inTarget).getHandle());
	}

	@Override
	public void loseTarget()
	{
		if(this.m_entity == null)
			return;
		
		this.getHandle().setGoalTarget((EntityLiving)null);
		if(!this.m_hadAttackDesire)
			this.getMind().removeTargetingDesire(DesireFindAttackingTarget.class);
	}

	@Override
	public LivingEntity getTarget()
	{
		if(this.m_entity == null)
			return null;
		
		EntityLiving target = this.m_entity.getGoalTarget();
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
