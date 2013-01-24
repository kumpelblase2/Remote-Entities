package de.kumpelblase2.remoteentities.persistence;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import de.kumpelblase2.remoteentities.api.Nameable;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class EntityData implements ConfigurationSerializable
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
	
	@SuppressWarnings("unchecked")
	public EntityData(Map<String, Object> inData)
	{
		this.id = (Integer)inData.get("id");
		this.type = RemoteEntityType.valueOf((String)inData.get("type"));
		this.name = (String)inData.get("name");
		this.location = new LocationData((Map<String, Object>)inData.get("location"));
		this.stationary = (Boolean)inData.get("stationary");
		this.pushable = (Boolean)inData.get("pushable");
		this.speed = ((Double)inData.get("speed")).floatValue();
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", this.id);
		data.put("type", this.type.name());
		data.put("name", this.name);
		data.put("location", location.serialize());
		data.put("stationary", this.stationary);
		data.put("pushable", this.pushable);
		data.put("speed", this.speed);
		//TODO: behaviors and desires
		return data;
	}
}
