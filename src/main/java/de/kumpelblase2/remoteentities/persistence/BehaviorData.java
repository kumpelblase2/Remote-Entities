package de.kumpelblase2.remoteentities.persistence;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.Behavior;

public class BehaviorData implements ConfigurationSerializable
{
	public String type;
	public ParameterData[] parameters;
	
	public BehaviorData()
	{
	}
	
	public BehaviorData(Behavior inBehavior)
	{
		this.type = inBehavior.getClass().getName();
		this.parameters = inBehavior.getSerializeableData();
	}
	
	@SuppressWarnings("unchecked")
	public BehaviorData(Map<String, Object> inData)
	{
		this.type = (String)inData.get("type");
		List<Map<String, Object>> parameterData = (List<Map<String, Object>>)inData.get("parameters");
		this.parameters = new ParameterData[parameterData.size()];
		for(Map<String, Object> param : parameterData)
		{
			ParameterData paramData = new ParameterData(param);
			this.parameters[paramData.pos] = paramData;
		}
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("type", this.type);
		List<Map<String, Object>> parameterData = new ArrayList<Map<String, Object>>();
		for(ParameterData param : this.parameters)
		{
			parameterData.add(param.serialize());
		}
		data.put("parameters", parameterData);
		return data;
	}
	
	@SuppressWarnings("unchecked")
	public Behavior create(RemoteEntity inEntity)
	{
		try
		{
			Class<? extends Behavior> c = (Class<? extends Behavior>)Class.forName(this.type);
			Constructor<? extends Behavior> con = c.getConstructor(this.getParameterClasses());
			if(con == null)
				return null;
			
			Object[] values = new Object[this.parameters.length];
			int pos = 0;
			for(ParameterData data : this.parameters)
			{
				values[pos] = EntityData.objectParser.deserialize(data);
			}
			return con.newInstance(values);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Class[] getParameterClasses()
	{
		Class[] classes = new Class[this.parameters.length];
		for(int i = 0; i < classes.length; i++)
		{
			try
			{
				classes[i] = Class.forName(this.parameters[i].type);
			}
			catch(Exception e)
			{
				continue;
			}
		}
		return classes;
	}
}
