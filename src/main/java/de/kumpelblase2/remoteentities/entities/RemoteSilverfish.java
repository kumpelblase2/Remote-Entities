package de.kumpelblase2.remoteentities.entities;

import org.bukkit.entity.Silverfish;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteSilverfish extends RemoteAttackingBaseEntity<Silverfish>
{
	public RemoteSilverfish(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteSilverfish(int inID, RemoteSilverfishEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Silverfish, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Silverfish";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.silverfish.say");
		this.setSound(EntitySound.HURT, "mob.silverfish.hit");
		this.setSound(EntitySound.DEATH, "mob.silverfish.kill");
		this.setSound(EntitySound.STEP, "mob.silverfish.step");
	}
}