package de.kumpelblase2.removeentities.entities;

import org.bukkit.entity.LivingEntity;

public class RemotePlayer extends RemoteBaseEntity implements RemoteEntity, Nameable, Fightable
{
	private RemotePlayerEntity m_entity;
	private String m_name;
	
	public RemotePlayer(int inID, String inName)
	{
		super(inID, RemoteEntityType.Human);
		this.m_name = inName;
	}
	
	public RemotePlayer(int inID, String inName, RemotePlayerEntity inEntity)
	{
		this(inID, inName);
		this.m_entity = inEntity;
	}
	
	public void setEntity(RemotePlayerEntity inEntity)
	{
		this.m_entity = inEntity;
	}
	
	@Override
	public LivingEntity getBukkitEntity()
	{
		return (LivingEntity)this.m_entity.getBukkitEntity();
	}

	@Override
	public void attack(LivingEntity inTarget)
	{
	}

	@Override
	public void loseTarget()
	{
	}

	@Override
	public String getName()
	{
		return this.m_name;
	}

	@Override
	public void setName(String inName)
	{
		this.m_name = inName;
	}
}
