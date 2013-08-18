package de.kumpelblase2.remoteentities.entities;

import org.bukkit.entity.Slime;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteSlime extends RemoteAttackingBaseEntity<Slime>
{
	public RemoteSlime(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteSlime(int inID, RemoteSlimeEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Slime, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Slime";
	}

	@Override
	protected void setupSounds()
	{
	}
}