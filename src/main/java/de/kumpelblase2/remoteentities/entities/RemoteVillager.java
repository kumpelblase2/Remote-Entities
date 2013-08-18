package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteVillager extends RemoteBaseEntity
{
	public RemoteVillager(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteVillager(int inID, RemoteVillagerEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Villager, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Villager";
	}

	@Override
	protected void setupSounds()
	{
		//TODO
		this.setSound(EntitySound.HURT, "mob.villager.hit");
		this.setSound(EntitySound.DEATH, "mob.villager.death");
		this.setSound(EntitySound.STEP, "mob.cow.step");
		this.setSound(EntitySound.YES, "mob.villager.yes");
		this.setSound(EntitySound.NO, "mob.villager.no");
	}
}