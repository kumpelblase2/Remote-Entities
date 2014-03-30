package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_7_R2.EntityLiving;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.Fightable;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;
import de.kumpelblase2.remoteentities.utilities.WorldUtilities;

public abstract class RemoteAttackingBaseEntity<T extends LivingEntity> extends RemoteBaseEntity<T> implements Fightable
{
	public RemoteAttackingBaseEntity(int inID, RemoteEntityType inType, EntityManager inManager)
	{
		super(inID, inType, inManager);
	}

	@Override
	public void attack(LivingEntity inTarget)
	{
		if(this.m_entity == null)
			return;

		NMSUtil.setGoalTarget(this.m_entity, WorldUtilities.getNMSEntity(inTarget));
	}

	@Override
	public void loseTarget()
	{
		if(this.m_entity == null)
			return;

		NMSUtil.setGoalTarget(this.m_entity, null);
	}

	@Override
	public LivingEntity getTarget()
	{
		if(this.m_entity == null)
			return null;

		EntityLiving target = NMSUtil.getGoalTarget(this.m_entity);
		if(target != null)
			return (LivingEntity)target.getBukkitEntity();

		return null;
	}
}