package de.kumpelblase2.remoteentities.entities;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Wolf;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteWolf extends RemoteAttackingBaseEntity<Wolf>
{
	public RemoteWolf(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteWolf(int inID, RemoteWolfEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Wolf, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Wolf";
	}

	@Override
	protected void setupSounds()
	{
		Map<String, String> randoms = new HashMap<String, String>();
		randoms.put("growl", "mob.wolf.growl");
		randoms.put("whine", "mob.wolf.whine");
		randoms.put("panting", "mob.wolf.panting");
		randoms.put("bark", "mob.wolf.bark");
		this.setSounds(EntitySound.RANDOM, randoms);
		this.setSound(EntitySound.HURT, "mob.wolf.hurt");
		this.setSound(EntitySound.DEATH, "mob.wolf.death");
		this.setSound(EntitySound.STEP, "mob.wolf.step");
	}
}