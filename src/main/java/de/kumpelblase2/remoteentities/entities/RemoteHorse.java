package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_6_R1.EntityHorse;
import org.bukkit.entity.Horse;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.*;

public class RemoteHorse extends RemoteBaseEntity<Horse>
{
	public RemoteHorse(int inID, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Horse, inManager);
	}

	public RemoteHorse(int inID, RemoteHorseEntity inEntity, EntityManager inManager)
	{
		this(inID, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "EntityHorse";
	}

	public void setType(HorseType inType)
	{
		((EntityHorse)this.m_entity).p(inType.ordinal());
	}

	public HorseType getHorseType()
	{
		return HorseType.values()[((EntityHorse)this.m_entity).bP()];
	}

	public void setVariant(HorseVariant inVariant)
	{
		((EntityHorse)this.m_entity).q(inVariant.getID());
	}

	public HorseVariant getVariant()
	{
		return HorseVariant.getByID(((EntityHorse)this.m_entity).bQ());
	}
}