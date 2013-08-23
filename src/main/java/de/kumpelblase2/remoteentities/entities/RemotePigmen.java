package de.kumpelblase2.remoteentities.entities;

import org.bukkit.entity.PigZombie;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemotePigmen extends RemoteAttackingBaseEntity<PigZombie>
{
	public RemotePigmen(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemotePigmen(int inID, RemotePigmenEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Pigmen, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "PigZombie";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.zombiepig.zpig");
		this.setSound(EntitySound.HURT, "mob.zombiepig.zpighurt");
		this.setSound(EntitySound.DEATH, "mob.zombiepig.zpigdeath");
	}
}