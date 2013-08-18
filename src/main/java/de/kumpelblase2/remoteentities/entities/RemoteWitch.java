package de.kumpelblase2.remoteentities.entities;

import org.bukkit.entity.Witch;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteWitch extends RemoteAttackingBaseEntity<Witch>
{
	public RemoteWitch(int inID, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Witch, inManager);
	}

	@Override
	public String getNativeEntityName()
	{
		return "Witch";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.witch.idle");
		this.setSound(EntitySound.HURT, "mob.witch.hurt");
		this.setSound(EntitySound.DEATH, "mob.witch.death");
	}
}