package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteChicken extends RemoteBaseEntity
{
	public RemoteChicken(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteChicken(int inID, RemoteChickenEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Chicken, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Chicken";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.chicken.say");
		this.setSound(EntitySound.HURT, "mob.chicken.hurt");
		this.setSound(EntitySound.DEATH, "mob.chicken.hurt");
		this.setSound(EntitySound.STEP, "mob.chicken.step");
	}
}