package de.kumpelblase2.remoteentities.entities;

import org.bukkit.entity.Snowman;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteSnowman extends RemoteAttackingBaseEntity<Snowman>
{
	public RemoteSnowman(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteSnowman(int inID, RemoteSnowmanEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Snowman, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "SnowMan";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "none");
		this.setSound(EntitySound.HURT, "none");
		this.setSound(EntitySound.DEATH, "none");
		this.setSound(EntitySound.ATTACK, "random.bow");
	}
}