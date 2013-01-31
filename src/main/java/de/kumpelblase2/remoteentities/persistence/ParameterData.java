package de.kumpelblase2.remoteentities.persistence;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class ParameterData implements ConfigurationSerializable
{
	public String type;
	public Object value;
	public int pos;
	public String special;
	
	public ParameterData()
	{
	}

	public ParameterData(int inPos, String inType, Object inValue)
	{
		this.pos = inPos;
		this.type = inType;
		this.value = inValue;
		this.special = "";
	}
	
	public ParameterData(int inPos, String inType, Object inValue, String inSpecial)
	{
		this(inPos, inType, inValue);
		this.special = inSpecial;
	}

	public ParameterData(Map<String, Object> inData)
	{
		this.pos = (Integer)inData.get("pos");
		this.type = (String)inData.get("type");
		this.value = inData.get("value");
		this.special = (String)inData.get("special");
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pos", this.pos);
		data.put("type", this.type);
		data.put("value", EntityData.objectParser.serialize(this.value));
		data.put("special", this.special);
		return data;
	}
}
