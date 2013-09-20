package de.kumpelblase2.remoteentities.entities;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.v1_6_R3.EntityHorse;
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
		((EntityHorse)this.m_entity).setType(inType.ordinal());
	}

	public HorseType getHorseType()
	{
		return HorseType.values()[((EntityHorse)this.m_entity).getType()];
	}

	public void setVariant(HorseVariant inVariant)
	{
		((EntityHorse)this.m_entity).setVariant(inVariant.getID());
	}

	public HorseVariant getVariant()
	{
		return HorseVariant.getByID(((EntityHorse)this.m_entity).getVariant());
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.LAND, "mob.horse.land");
	}
}