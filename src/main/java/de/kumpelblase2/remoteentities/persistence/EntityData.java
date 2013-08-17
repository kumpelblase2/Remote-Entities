package de.kumpelblase2.remoteentities.persistence;

import java.util.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import de.kumpelblase2.remoteentities.api.features.Feature;
import de.kumpelblase2.remoteentities.api.thinking.Behavior;
import de.kumpelblase2.remoteentities.api.thinking.DesireItem;
import de.kumpelblase2.remoteentities.entities.RemoteBaseEntity;

public class EntityData implements ConfigurationSerializable
{
	public int id;
	public RemoteEntityType type;
	public String name;
	public LocationData location;
	public boolean stationary;
	public boolean pushable;
	public double speed;
	public double pathfindingRange = 32;
	public DesireData[] actionDesires = new DesireData[0];
	public DesireData[] movementDesires = new DesireData[0];
	public BehaviorData[] behaviors = new BehaviorData[0];
	public FeatureData[] features = new FeatureData[0];
	public static transient ObjectParser objectParser = new ObjectParser();

	public EntityData()
	{
	}

	public EntityData(RemoteEntity inEntity)
	{
		if(inEntity == null)
			return;

		this.id = inEntity.getID();
		this.type = inEntity.getType();
		this.name = inEntity.getName() != null && inEntity.getName().length() > 0 ? inEntity.getName() : "";
		if(inEntity.isSpawned())
			this.location = new LocationData(inEntity.getBukkitEntity().getLocation());
		else
		{
			if(inEntity instanceof RemoteBaseEntity)
			{
				RemoteBaseEntity base = (RemoteBaseEntity)inEntity;
				if(base.getUnloadedLocation() != null)
					this.location = new LocationData(base.getUnloadedLocation());
				else
					this.location = new LocationData();
			}
			else
				this.location = new LocationData();
		}

		this.stationary = inEntity.isStationary();
		this.pushable = inEntity.isPushable();
		this.speed = inEntity.getSpeed();
		this.pathfindingRange = inEntity.getPathfindingRange();
		List<DesireData> action = new ArrayList<DesireData>();
		for(int i = 0; i < inEntity.getMind().getTargetingDesires().size(); i++)
		{
			DesireItem desire = inEntity.getMind().getTargetingDesires().get(i);
			if(!desire.getDesire().getClass().isAnnotationPresent(IgnoreSerialization.class))
				action.add(new DesireData(desire));
		}

		this.actionDesires = action.toArray(new DesireData[action.size()]);
		List<DesireData> movement = new ArrayList<DesireData>();
		for(int i = 0; i < inEntity.getMind().getMovementDesires().size(); i++)
		{
			DesireItem desire = inEntity.getMind().getMovementDesires().get(i);
			if(!desire.getDesire().getClass().isAnnotationPresent(IgnoreSerialization.class))
				movement.add(new DesireData(desire));
		}

		this.movementDesires = movement.toArray(new DesireData[movement.size()]);
		this.behaviors = new BehaviorData[inEntity.getMind().getBehaviours().size()];
		int pos = 0;
		for(Behavior behavior : inEntity.getMind().getBehaviours())
		{
			this.behaviors[pos] = new BehaviorData(behavior);
			pos++;
		}

		List<FeatureData> featureList = new ArrayList<FeatureData>();
		for(int i = 0; i < this.features.length; i++)
		{
			Feature f = inEntity.getFeatures().getAllFeatures().get(i);
			if(!f.getClass().isAnnotationPresent(IgnoreSerialization.class))
				featureList.add(new FeatureData(f));
		}

		this.features = featureList.toArray(new FeatureData[featureList.size()]);
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
		if(inData.containsKey("pathfindingRange"))
			this.pathfindingRange = (Double)inData.get("pathfindingRange");

		List<Map<String, Object>> dataList = (List<Map<String, Object>>)inData.get("actionDesires");
		this.actionDesires = new DesireData[dataList.size()];
		for(int i = 0; i < this.actionDesires.length; i++)
		{
			this.actionDesires[i] = new DesireData(dataList.get(i));
		}

		dataList = (List<Map<String, Object>>)inData.get("movementDesires");
		if(dataList != null)
		{
			this.movementDesires = new DesireData[dataList.size()];
			for(int i = 0; i < this.movementDesires.length; i++)
			{
				this.movementDesires[i] = new DesireData(dataList.get(i));
			}
		}

		dataList = (List<Map<String, Object>>)inData.get("behaviors");
		if(dataList != null)
		{
			this.behaviors = new BehaviorData[dataList.size()];
			for(int i = 0; i < this.behaviors.length; i++)
			{
				this.behaviors[i] = new BehaviorData(dataList.get(i));
			}
		}

		dataList = (List<Map<String, Object>>)inData.get("features");
		if(dataList != null)
		{
			this.features = new FeatureData[dataList.size()];
			for(int i = 0; i < this.features.length; i++)
			{
				this.features[i] = new FeatureData(dataList.get(i));
			}
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
		data.put("pathfindingRange", this.pathfindingRange);
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
		List<Map<String, Object>> featureList = new ArrayList<Map<String, Object>>();
		for(FeatureData fd : this.features)
		{
			featureList.add(fd.serialize());
		}

		data.put("features", featureList);
		return data;
	}
}