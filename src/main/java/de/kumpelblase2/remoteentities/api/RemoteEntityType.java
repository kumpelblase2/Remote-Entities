package de.kumpelblase2.remoteentities.api;

import de.kumpelblase2.remoteentities.entities.*;
import de.kumpelblase2.remoteentities.utilities.EnumChange;
import net.minecraft.server.EntityLiving;

public enum RemoteEntityType
{
	Human(RemotePlayer.class, RemotePlayerEntity.class, true),
	Zombie(RemoteZombie.class, RemoteZombieEntity.class, false),
	Spider(RemoteSpider.class, RemoteSpiderEntity.class, false),
	Creeper(RemoteCreeper.class, RemoteCreeperEntity.class, false), 
	Skeleton(RemoteSkeleton.class, RemoteSkeletonEntity.class, false),
	Blaze(RemoteBlaze.class, RemoteBlazeEntity.class, false),
	CaveSpider(RemoteCaveSpider.class, RemoteCaveSpiderEntity.class, false),
	Chicken(RemoteChicken.class, RemoteChickenEntity.class, false),
	Cow(RemoteCow.class, RemoteCowEntity.class, false),
	EnderDragon(RemoteEnderDragon.class, RemoteEnderDragonEntity.class, false),
	Enderman(RemoteEnderman.class, RemoteEndermanEntity.class, false),
	Ghast(RemoteGhast.class, RemoteGhastEntity.class, false),
	IronGolem(RemoteIronGolem.class, RemoteIronGolemEntity.class, false),
	LavaSlime(RemoteLavaSlime.class, RemoteLavaSlimeEntity.class, false),
	Mushroom(RemoteMushroom.class, RemoteMushroomEntity.class, false),
	Ocelot(RemoteOcelote.class, RemoteOceloteEntity.class, false),
	Pig(RemotePig.class, RemotePigEntity.class, false),
	Pigmen(RemotePigmen.class, RemotePigmenEntity.class, false),
	Sheep(RemoteSheep.class, RemoteSheepEntity.class, false),
	Silverfish(RemoteSilverfish.class, RemoteSilverfishEntity.class, false),
	Slime(RemoteSlime.class, RemoteSlimeEntity.class, false),
	Snowman(RemoteSnowman.class, RemoteSnowmanEntity.class, false),
	Squid(RemoteSquid.class, RemoteSquidEntity.class, false),
	Villager(RemoteVillager.class, RemoteVillagerEntity.class, false),
	Wolf(RemoteWolf.class, RemoteWolfEntity.class, false);
	
	
	private Class<? extends EntityLiving> m_entityClass;
	private Class<? extends RemoteEntity> m_remoteClass;
	private boolean m_isNamed = false;
	
	private RemoteEntityType(Class<? extends RemoteEntity> inRemoteClass, Class<? extends EntityLiving> inEntityClass)
	{
		this.m_entityClass = inEntityClass;
		this.m_remoteClass = inRemoteClass;
	}
	
	private RemoteEntityType(Class<? extends RemoteEntity> inRemoteClass, Class<? extends EntityLiving> inEntityClass, boolean inNamed)
	{
		this(inRemoteClass, inEntityClass);
		this.m_isNamed = inNamed;
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
	
	public static void addType(String inName, Class<? extends EntityLiving> inEntityClass, Class<? extends RemoteEntity> inRemoteClass, boolean isNamed)
	{
		if(valueOf(inName) == null)
			EnumChange.addEnum(RemoteEntityType.class, inName, new Class<?>[] { Class.class, Class.class, boolean.class }, new Object[] { inRemoteClass, inEntityClass, isNamed });
		else
			replaceType(inName, inEntityClass, inRemoteClass, isNamed);
	}
	
	public static void removeType(String inName)
	{
		EnumChange.removeEnum(RemoteEntityType.class, inName);
	}
	
	public static void replaceType(String inName, Class<? extends EntityLiving> inEntityClass, Class<? extends RemoteEntity> inRemoteClass, boolean isNamed)
	{
		removeType(inName);
		addType(inName, inEntityClass, inRemoteClass, isNamed);
	}
	
	public static RemoteEntityType getByEntityClass(Class<? extends EntityLiving> inEntityClass)
	{
		for(RemoteEntityType type : values())
		{
			if(type.getEntityClass().equals(inEntityClass) || type.getEntityClass().getSuperclass().equals(inEntityClass))
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
}
