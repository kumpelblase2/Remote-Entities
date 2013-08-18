package de.kumpelblase2.remoteentities.entities;

import org.bukkit.entity.Enderman;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteEnderman extends RemoteAttackingBaseEntity<Enderman>
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
	public String getNativeEntityName()
	{
		return "Enderman";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.endermen.idle");
		this.setSound(EntitySound.HURT, "mob.endermen.hit");
		this.setSound(EntitySound.DEATH, "mob.endermen.death");
		this.setSound(EntitySound.STARE, "mob.endermen.stare");
		this.setSound(EntitySound.TELEPORT, "mob.endermen.portal");
		this.setSound(EntitySound.SCREAM, "mob.endermen.scream");
	}
}