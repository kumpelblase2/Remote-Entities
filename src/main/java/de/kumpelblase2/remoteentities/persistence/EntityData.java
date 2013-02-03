package de.kumpelblase2.remoteentities.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import de.kumpelblase2.remoteentities.api.Nameable;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import de.kumpelblase2.remoteentities.api.thinking.Behavior;

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
	public static ObjectParser objectParser = new ObjectParser();
	
	public EntityData()
	{
	}
	
	public EntityData(RemoteEntity inEntity)
	{
		if(inEntity == null)
			return;
		
		this.id = inEntity.getID();
		this.type = inEntity.getType();
		this.name = (inEntity instanceof Nameable ? ((Nameable)inEntity).getName() : "");
		if(inEntity.isSpawned())
			this.location = new LocationData(inEntity.getBukkitEntity().getLocation());
		else
			this.location = new LocationData();
		
		this.stationary = inEntity.isStationary();
		this.pushable = inEntity.isPushable();
		this.speed = inEntity.getSpeed();
		this.actionDesires = new DesireData[inEntity.getMind().getActionDesires().size()];
		for(int i = 0; i < this.actionDesires.length; i++)
		{
			this.actionDesires[i] = new DesireData(inEntity.getMind().getActionDesires().get(i));
		}
		this.movementDesires = new DesireData[inEntity.getMind().getMovementDesires().size()];
		for(int i = 0; i < this.movementDesires.length; i++)
		{
			this.movementDesires[i] = new DesireData(inEntity.getMind().getMovementDesires().get(i));
		}
		this.behaviors = new BehaviorData[inEntity.getMind().getBehaviours().size()];
		int pos = 0;
		for(Behavior behavior : inEntity.getMind().getBehaviours())
		{
			this.behaviors[pos] = new BehaviorData(behavior);
			pos++;
		}
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
		List<Map<String, Object>> dataList = (List<Map<String, Object>>)inData.get("actionDesires");
		this.actionDesires = new DesireData[dataList.size()];
		for(int i = 0; i < this.actionDesires.length; i++)
		{
			this.actionDesires[i] = new DesireData(dataList.get(i));
		}
		dataList = (List<Map<String, Object>>)inData.get("movementDesires");
		this.movementDesires = new DesireData[dataList.size()];
		for(int i = 0; i < this.movementDesires.length; i++)
		{
			this.movementDesires[i] = new DesireData(dataList.get(i));
		}
		dataList = (List<Map<String, Object>>)inData.get("behaviors");
		this.behaviors = new BehaviorData[dataList.size()];
		for(int i = 0; i < behaviors.length; i++)
		{
			this.behaviors[i] = new BehaviorData(dataList.get(i));
		}
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
		List<Map<String, Object>> behaviorList = new ArrayList<Map<String, Object>>();
		for(BehaviorData bd : this.behaviors)
		{
			behaviorList.add(bd.serialize());
		}
		data.put("behaviors", behaviorList);
		return data;
	}
}
