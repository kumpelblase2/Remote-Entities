package de.kumpelblase2.remoteentities.entities;

import org.bukkit.entity.Creeper;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteCreeper extends RemoteAttackingBaseEntity<Creeper>
{
	public RemoteCreeper(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteCreeper(int inID, RemoteCreeperEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Creeper, inManager);
		this.m_entity = inEntity;
	}

	/**
	 * Lets the creeper explode at the current position with the default explosion radius.
	 */
	public void explode()
	{
		this.explode(1);
	}

	/**
	 * Lets the creeper explode at the current position with a given explosion modifier.
	 *
	 * @param inModifier	Modifier for explosion radius
	 */
	public void explode(int inModifier)
	{
		if(this.m_entity == null)
			return;

		this.getBukkitEntity().getWorld().createExplosion(this.getBukkitEntity().getLocation(), 3F * inModifier);
		this.getBukkitEntity().setHealth((double)0);
	}

	@Override
	public String getNativeEntityName()
	{
		return "Creeper";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.creeper.say");
		this.setSound(EntitySound.DEATH, "mob.creeper.death");
	}
}