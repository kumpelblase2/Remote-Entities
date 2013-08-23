package de.kumpelblase2.remoteentities.entities;

import java.util.HashMap;
import java.util.Map;
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
		Map<String, String> random = new HashMap<String, String>();
		random.put("idle", "mob.endermen.idle");
		random.put("scream", "mob.endermen.scream");
		this.setSounds(EntitySound.RANDOM, random);
		this.setSound(EntitySound.HURT, "mob.endermen.hit");
		this.setSound(EntitySound.DEATH, "mob.endermen.death");
		this.setSound(EntitySound.TELEPORT, "mob.endermen.portal");
	}
}