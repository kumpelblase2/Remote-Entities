package de.kumpelblase2.remoteentities.persistence;

public class ObjectParser
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object parse(ParameterData data)
	{
		Class typeClass = this.getClass(data.type);
		if(typeClass.isAssignableFrom(Class.class))
			return this.getClass(data.value.toString());
		else if(typeClass.isAssignableFrom(int.class) || typeClass.isAssignableFrom(Integer.class))
			return this.getInt(data.value);
		else if(typeClass.isAssignableFrom(boolean.class) || typeClass.isAssignableFrom(Boolean.class))
			return this.getBoolean(data.value);
		else if(typeClass.isAssignableFrom(Enum.class))
			return Enum.valueOf(typeClass, data.value.toString());
		else if(typeClass.isAssignableFrom(float.class) || typeClass.isAssignableFrom(Float.class))
			return this.getFloat(data.value);
		else if(typeClass.isAssignableFrom(double.class) || typeClass.isAssignableFrom(Double.class))
			return this.getDouble(data.value);
		else
			return data.value.toString();
	}
	
	@SuppressWarnings("rawtypes")
	protected Class getClass(String inName)
	{
		try
		{
			return Class.forName(inName);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	protected Integer getInt(Object inValue)
	{
		try
		{
			return Integer.parseInt(inValue.toString());
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	
	protected Boolean getBoolean(Object inValue)
	{
		try
		{
			return Boolean.parseBoolean(inValue.toString());
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	protected Float getFloat(Object inValue)
	{
		try
		{
			return Float.parseFloat(inValue.toString());
		}
		catch(Exception e)
		{
			return 0f;
		}
	}
	
	protected Double getDouble(Object inValue)
	{
		try
		{
			return Double.parseDouble(inValue.toString());
		}
		catch(Exception e)
		{
			return 0d;
		}
	}
}
