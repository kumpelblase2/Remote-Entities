package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import org.bukkit.entity.Witch;

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
}
