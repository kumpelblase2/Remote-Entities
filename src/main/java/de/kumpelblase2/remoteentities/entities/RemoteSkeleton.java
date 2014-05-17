package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_7_R3.EntityLiving;
import org.bukkit.entity.Skeleton;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteSkeleton extends RemoteAttackingBaseEntity<Skeleton>
{
	public RemoteSkeleton(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteSkeleton(int inID, EntityLiving inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Skeleton, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Skeleton";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.skeleton.say");
		this.setSound(EntitySound.HURT, "mob.skeleton.hurt");
		this.setSound(EntitySound.DEATH, "mob.skeleton.death");
		this.setSound(EntitySound.STEP, "mob.skeleton.step");
		this.setSound(EntitySound.ATTACK, "random.bow");
	}
}