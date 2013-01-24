package de.kumpelblase2.remoteentities.persistence;

import de.kumpelblase2.remoteentities.api.Nameable;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class EntityData
{
	public int id;
	public RemoteEntityType type;
	public String name;
	public LocationData location;
	public boolean stationary;
	public boolean pushable;
	public float speed;
	public DesireData[] desires;
	public BehaviorData[] behaviors;
	
	public EntityData()
	{
	}
	
	public EntityData(RemoteEntity inEntity)
	{
		this.id = inEntity.getID();
		this.type = inEntity.getType();
		this.name = (inEntity instanceof Nameable ? ((Nameable)inEntity).getName() : "");
		this.location = new LocationData(inEntity.getBukkitEntity().getLocation());
		this.stationary = inEntity.isStationary();
		this.pushable = inEntity.isPushable();
		this.speed = inEntity.getSpeed();
		//TODO: behaviors and desires
	}
}
