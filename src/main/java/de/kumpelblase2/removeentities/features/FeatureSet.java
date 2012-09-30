package de.kumpelblase2.removeentities.features;

import java.util.HashMap;
import java.util.Map;

public class FeatureSet
{
	private Map<String, Feature> m_features;
	
	public FeatureSet()
	{
		this.m_features = new HashMap<String, Feature>();
	}
	
	public boolean hasFeature(String inName)
	{
		return this.m_features.containsKey(inName);
	}
	
	public void addFeature(Feature inFeature)
	{
		this.m_features.put(inFeature.getName(), inFeature);
	}
	
	public boolean removeFeature(String inName)
	{
		return this.m_features.remove(inName) != null;
	}
	
	public Feature getFeature(String inName)
	{
		return this.m_features.get(inName);
	}
}
