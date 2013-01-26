package de.kumpelblase2.remoteentities.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.kumpelblase2.remoteentities.api.thinking.Desire;
import org.bukkit.Bukkit;
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

        int totalDesires = inEntity.getMind().getActionDesires().size() + inEntity.getMind().getMovementDesires().size();
        int endLength = 0;
        this.desires = new DesireData[totalDesires];

        if (inEntity.getMind().getActionDesires().size() > 0) {
            List<Desire> actionDesires = inEntity.getMind().getActionDesires();

            for (int actionDesireIndex = 0; actionDesireIndex < actionDesires.size(); actionDesireIndex++) {
                Desire desire = actionDesires.get(actionDesireIndex);
                this.desires[actionDesireIndex] = new DesireData(desire);
                endLength++;
            }
        }

        if (inEntity.getMind().getMovementDesires().size() > 0) {
            List<Desire> movementDesires = inEntity.getMind().getMovementDesires();
            for (int movementDesireIndex = 0; movementDesireIndex < movementDesires.size(); movementDesireIndex++) {
                Desire desire = movementDesires.get(movementDesireIndex);
                this.desires[movementDesireIndex + endLength] = new DesireData(desire);
            }
        }

		//TODO: behaviors
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

        //TODO: behaviors and desires
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

        ArrayList<Map<String, Object>> serializedDesires = new ArrayList();
        for (DesireData desireData: this.desires) {
            serializedDesires.add(desireData.serialize());
        }

        data.put("desires", serializedDesires);

		//TODO: behaviors and desires
		return data;
	}
}
