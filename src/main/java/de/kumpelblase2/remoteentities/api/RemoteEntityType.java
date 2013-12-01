package de.kumpelblase2.remoteentities.api;

import java.util.*;
import net.minecraft.server.v1_7_R1.EntityLiving;
import de.kumpelblase2.remoteentities.entities.*;

public class RemoteEntityType
{
	public static final RemoteEntityType Human = new RemoteEntityType("Human", RemotePlayer.class, RemotePlayerEntity.class, true);
	public static final RemoteEntityType Zombie = new RemoteEntityType("Zombie", RemoteZombie.class, RemoteZombieEntity.class, false);
	public static final RemoteEntityType Spider = new RemoteEntityType("Spider", RemoteSpider.class, RemoteSpiderEntity.class, false);
	public static final RemoteEntityType Creeper = new RemoteEntityType("Creeper", RemoteCreeper.class, RemoteCreeperEntity.class, false);
	public static final RemoteEntityType Skeleton = new RemoteEntityType("Skeleton", RemoteSkeleton.class, RemoteSkeletonEntity.class, false);
	public static final RemoteEntityType Blaze = new RemoteEntityType("Blaze", RemoteBlaze.class, RemoteBlazeEntity.class, false);
	public static final RemoteEntityType CaveSpider = new RemoteEntityType("CaveSpider", RemoteCaveSpider.class, RemoteCaveSpiderEntity.class, false);
	public static final RemoteEntityType Chicken = new RemoteEntityType("Chicken", RemoteChicken.class, RemoteChickenEntity.class, false);
	public static final RemoteEntityType Cow = new RemoteEntityType("Cow", RemoteCow.class, RemoteCowEntity.class, false);
	public static final RemoteEntityType EnderDragon = new RemoteEntityType("EnderDragon", RemoteEnderDragon.class, RemoteEnderDragonEntity.class, false);
	public static final RemoteEntityType Enderman = new RemoteEntityType("Enderman", RemoteEnderman.class, RemoteEndermanEntity.class, false);
	public static final RemoteEntityType Ghast = new RemoteEntityType("Ghast", RemoteGhast.class, RemoteGhastEntity.class, false);
	public static final RemoteEntityType IronGolem = new RemoteEntityType("IronGolem", RemoteIronGolem.class, RemoteIronGolemEntity.class, false);
	public static final RemoteEntityType LavaSlime = new RemoteEntityType("LavaSlime", RemoteLavaSlime.class, RemoteLavaSlimeEntity.class, false);
	public static final RemoteEntityType Mushroom = new RemoteEntityType("Mushroom", RemoteMushroom.class, RemoteMushroomEntity.class, false);
	public static final RemoteEntityType Ocelot = new RemoteEntityType("Ocelote", RemoteOcelote.class, RemoteOceloteEntity.class, false);
	public static final RemoteEntityType Pig = new RemoteEntityType("Pig", RemotePig.class, RemotePigEntity.class, false);
	public static final RemoteEntityType Pigmen = new RemoteEntityType("Pigmen", RemotePigmen.class, RemotePigmenEntity.class, false);
	public static final RemoteEntityType Sheep = new RemoteEntityType("Sheep", RemoteSheep.class, RemoteSheepEntity.class, false);
	public static final RemoteEntityType Silverfish = new RemoteEntityType("Silverfish", RemoteSilverfish.class, RemoteSilverfishEntity.class, false);
	public static final RemoteEntityType Slime = new RemoteEntityType("Slime", RemoteSlime.class, RemoteSlimeEntity.class, false);
	public static final RemoteEntityType Snowman = new RemoteEntityType("Snowman", RemoteSnowman.class, RemoteSnowmanEntity.class, false);
	public static final RemoteEntityType Squid = new RemoteEntityType("Squid", RemoteSquid.class, RemoteSquidEntity.class, false);
	public static final RemoteEntityType Villager = new RemoteEntityType("Villager", RemoteVillager.class, RemoteVillagerEntity.class, false);
	public static final RemoteEntityType Wolf = new RemoteEntityType("Wolf", RemoteWolf.class, RemoteWolfEntity.class, false);
	public static final RemoteEntityType Witch = new RemoteEntityType("Witch", RemoteWitch.class, RemoteWitchEntity.class, false);
	public static final RemoteEntityType Wither = new RemoteEntityType("Wither", RemoteWither.class, RemoteWitherEntity.class, false);
	public static final RemoteEntityType Bat = new RemoteEntityType("Bat", RemoteBat.class, RemoteBatEntity.class, false);
	public static final RemoteEntityType Horse = new RemoteEntityType("Horse", RemoteHorse.class, RemoteHorseEntity.class, false);

	private static List<RemoteEntityType> values;
	private static RemoteEntityType[] lastConvert = null;

	private Class<? extends EntityLiving> m_entityClass;
	private Class<? extends RemoteEntity> m_remoteClass;
	private boolean m_isNamed = false;
	private final String m_name;

	private RemoteEntityType(String inName, Class<? extends RemoteEntity> inRemoteClass, Class<? extends EntityLiving> inEntityClass, boolean inNamed)
	{
		this.m_name = inName;
		this.m_entityClass = inEntityClass;
		this.m_remoteClass = inRemoteClass;
		this.m_isNamed = inNamed;
		if(values == null)
			values = new ArrayList<RemoteEntityType>();

		if(!values.contains(this))
			values.add(this);
	}

	public Class<? extends RemoteEntity> getRemoteClass()
	{
		return this.m_remoteClass;
	}

	public Class<? extends EntityLiving> getEntityClass()
	{
		return this.m_entityClass;
	}

	public boolean isNamed()
	{
		return this.m_isNamed;
	}

	public void setRemoteClass(Class<? extends RemoteEntity> inClass)
	{
		this.m_remoteClass = inClass;
	}

	public void setEntityClass(Class<? extends EntityLiving> inClass)
	{
		this.m_entityClass = inClass;
	}

	public void setNamed(boolean inNamed)
	{
		this.m_isNamed = inNamed;
	}

	public String toString()
	{
		return this.name();
	}

	public String name()
	{
		return this.m_name;
	}

	public int ordinal()
	{
		for(int i = 0; i < values.size(); i++)
		{
			if(values.get(i) == this)
				return i;
		}
		return -1; //This shouldn't happen however.
	}

	public boolean equals(Object o)
	{
		return o instanceof RemoteEntity && ((RemoteEntityType)o).name().equals(this.name());
	}

	public int hashCode()
	{
		return this.name().hashCode();
	}

	public static RemoteEntityType[] values()
	{
		return lastConvert;
	}

	public static RemoteEntityType valueOf(String inName)
	{
		for(RemoteEntityType type : values)
		{
			if(type.name().equals(inName))
				return type;
		}
		return null;
	}

	public static boolean addType(String inName, Class<? extends EntityLiving> inEntityClass, Class<? extends RemoteEntity> inRemoteClass, boolean isNamed)
	{
		return addType(new RemoteEntityType(inName, inRemoteClass, inEntityClass, isNamed));
	}

	private static boolean addType(RemoteEntityType inType)
	{
		if(valueOf(inType.name()) != null)
			return false;

		if(!values.contains(inType))
			values.add(inType);

		update();
		return true;
	}

	public static boolean removeType(String inName)
	{
		Iterator<RemoteEntityType> it = values.iterator();
		int pos = 0;
		while(it.hasNext())
		{
			RemoteEntityType type = it.next();
			if(type.name().equals(inName))
			{
				if(pos <= 27)
					return false;

				it.remove();
				update();
				return true;
			}
			pos++;
		}
		return false;
	}

	public static RemoteEntityType getByEntityClass(Class<? extends EntityLiving> inEntityClass)
	{
		for(RemoteEntityType type : values())
		{
			if(type.getEntityClass().equals(inEntityClass) || type.getEntityClass().getSuperclass().equals(inEntityClass) || type.getEntityClass().isAssignableFrom(inEntityClass))
				return type;
		}
		return null;
	}

	public static RemoteEntityType getByRemoteClass(Class<? extends RemoteEntity> inRemoteClass)
	{
		for(RemoteEntityType type : values())
		{
			if(type.getRemoteClass().equals(inRemoteClass))
				return type;
		}
		return null;
	}

	public static void update()
	{
		lastConvert = values.toArray(new RemoteEntityType[values.size()]);
	}
}