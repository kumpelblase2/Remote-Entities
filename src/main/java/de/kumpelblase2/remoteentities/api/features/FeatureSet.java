package de.kumpelblase2.remoteentities.api.features;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FeatureSet
{
	protected List<Feature> m_features = new ArrayList<Feature>();
	
	public boolean hasFeature(Class<? extends Feature> inType)
	{
		for(Feature feature : this.m_features)
		{
			if(feature.getClass().equals(inType) || inType.isAssignableFrom(feature.getClass()))
				return true;
		}
		return false;
	}
	
	public void addFeature(Feature inFeature)
	{
		this.removeFeature(inFeature.getClass());
		this.m_features.add(inFeature);
	}
	
	public void removeFeature(Class<? extends Feature> inType)
	{
		Iterator<Feature> it = this.m_features.iterator();
		while(it.hasNext())
		{
			Feature feature = it.next();
			if(feature.getClass().equals(inType) || inType.isAssignableFrom(feature.getClass()))
				it.remove();
		}
	}
	
	@SuppressWarnings("unchecked")
	public<T extends Feature> T getFeature(Class<T> inType)
	{		
		for(Feature feature : this.m_features)
		{
			if(feature.getClass().equals(inType) || inType.isAssignableFrom(feature.getClass()))
				return (T)feature;
		}
		return null;
	}
}
