package de.kumpelblase2.remoteentities.entities;

import org.bukkit.entity.Wither;
import de.kumpelblase2.remoteentities.EntityManager;
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
		return "Wither";
	}
}
