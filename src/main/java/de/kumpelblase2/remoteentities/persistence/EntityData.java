package de.kumpelblase2.remoteentities.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import de.kumpelblase2.remoteentities.api.Nameable;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import de.kumpelblase2.remoteentities.api.thinking.Desire;

public class EntityData implements ConfigurationSerializable
{
	public int id;
	public RemoteEntityType type;
	public String name;
	public LocationData location;
	public boolean stationary;
	public boolean pushable;
	public float speed;
	public DesireData[] actionDesires;
	public DesireData[] movementDesires;
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
		this.actionDesires = new DesireData[inEntity.getMind().getActionDesires().size()];
		int pos = 0;
		for(Desire desire : inEntity.getMind().getActionDesires())
		{
			this.actionDesires[pos] = new DesireData(desire);
			pos++;
		}
		this.movementDesires = new DesireData[inEntity.getMind().getMovementDesires().size()];
		pos = 0;
		for(Desire desire : inEntity.getMind().getMovementDesires())
		{
			this.movementDesires[pos] = new DesireData(desire);
			pos++;
		}
		//TODO behaviors
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
		List<Map<String, Object>> desireList = (List<Map<String, Object>>)inData.get("actionDesires");
		this.actionDesires = new DesireData[desireList.size()];
		int pos = 0;
		for(Map<String, Object> desireData : desireList)
		{
			this.actionDesires[pos] = new DesireData(desireData);
			pos++;
		}
		pos = 0;
		desireList = (List<Map<String, Object>>)inData.get("movementDesires");
		this.movementDesires = new DesireData[desireList.size()];
		for(Map<String, Object> desiredata : desireList)
		{
			this.movementDesires[pos] = new DesireData(desiredata);
			pos++;
		}
		//TODO behaviors
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
		List<Map<String, Object>> desirelist = new ArrayList<Map<String, Object>>();
		for(DesireData dd : movementDesires)
		{
			desirelist.add(dd.serialize());
		}
		data.put("movementDesires", desirelist);
		List<Map<String, Object>> actiondesirelist = new ArrayList<Map<String, Object>>();
		for(DesireData dd : this.actionDesires)
		{
			actiondesirelist.add(dd.serialize());
		}
		data.put("actionDesires", actiondesirelist);
		//TODO: behaviors
		return data;
	}
}
