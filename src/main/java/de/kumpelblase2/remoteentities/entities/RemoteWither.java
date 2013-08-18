package de.kumpelblase2.remoteentities.entities;

import org.bukkit.entity.Wither;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteWither extends RemoteAttackingBaseEntity<Wither>
{
	public RemoteWither(int inID, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Wither, inManager);
	}

	@Override
	public String getNativeEntityName()
	{
		return "WitherBoss";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.wither.idle");
		this.setSound(EntitySound.HURT, "mob.wither.hurt");
		this.setSound(EntitySound.DEATH, "mob.wither.death");
	}
}