package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import org.bukkit.entity.Ocelot;

public class RemoteOcelote extends RemoteAttackingBaseEntity<Ocelot>
{
	public RemoteOcelote(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}
	
	public RemoteOcelote(int inID, RemoteOceloteEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Ocelot, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Ozelot";
	}
}
