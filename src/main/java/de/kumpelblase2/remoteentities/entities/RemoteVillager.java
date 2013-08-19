package de.kumpelblase2.remoteentities.entities;

import java.util.HashMap;
import java.util.Map;
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
		Map<String, String> randoms = new HashMap<String, String>();
		randoms.put("haggle", "mob.villager.haggle");
		randoms.put("idle", "mob.villager.idle");
		this.setSounds(EntitySound.RANDOM, randoms);
		this.setSound(EntitySound.HURT, "mob.villager.hit");
		this.setSound(EntitySound.DEATH, "mob.villager.death");
		this.setSound(EntitySound.STEP, "mob.cow.step");
		this.setSound(EntitySound.YES, "mob.villager.yes");
		this.setSound(EntitySound.NO, "mob.villager.no");
	}
}