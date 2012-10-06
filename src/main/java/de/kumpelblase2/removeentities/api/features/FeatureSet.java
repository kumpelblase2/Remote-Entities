package de.kumpelblase2.removeentities.api.features;

import java.util.HashMap;

@SuppressWarnings("serial")
public class FeatureSet extends HashMap<String, Feature>
{	
	public boolean hasFeature(String inName)
	{
		return this.containsKey(inName);
	}
	
	public void addFeature(Feature inFeature)
	{
		this.put(inFeature.getName(), inFeature);
	}
	
	public boolean removeFeature(String inName)
	{
		return this.remove(inName) != null;
	}
	
	public Feature getFeature(String inName)
	{
		return this.get(inName);
	}
}
