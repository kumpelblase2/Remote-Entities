package de.kumpelblase2.remoteentities.persistence;

import java.lang.reflect.Constructor;
import java.util.*;
import org.apache.commons.lang3.ClassUtils;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import de.kumpelblase2.remoteentities.RemoteEntities;
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
		if(parameterData == null || parameterData.size() == 0)
		{
			this.parameters = new ParameterData[0];
			return;
		}
		
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
			for(int i = 0; i < values.length; i++)
			{
				if(this.parameters[i].special.equals("entity"))
					values[i] = inEntity;
				else if(this.parameters[i].special.equals("manager"))
					values[i] = inEntity.getManager();
				else				
					values[i] = EntityData.objectParser.deserialize(this.parameters[i]);
			}
			return con.newInstance(values);
		}
		catch(Exception e)
		{
			RemoteEntities.getInstance().getLogger().warning("Error when trying to deserialize behavior with type " + this.type + ": ");
			RemoteEntities.getInstance().getLogger().warning(e.getMessage());
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
				Class c = ClassUtils.getClass(this.getClass().getClassLoader(), this.parameters[i].type);
				if(ClassUtils.wrapperToPrimitive(c) != null)	
					c = ClassUtils.wrapperToPrimitive(c);
				
				classes[i] = c;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return classes;
	}
}
