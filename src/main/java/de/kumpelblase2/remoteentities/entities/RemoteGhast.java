package de.kumpelblase2.remoteentities.entities;

import org.bukkit.entity.Ghast;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteGhast extends RemoteAttackingBaseEntity<Ghast>
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
	public String getNativeEntityName()
	{
		return "Ghast";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.ghast.moan");
		this.setSound(EntitySound.HURT, "mob.ghast.scream");
		this.setSound(EntitySound.DEATH, "mob.ghast.death");
	}
}