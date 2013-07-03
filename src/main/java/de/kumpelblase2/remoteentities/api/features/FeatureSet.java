package de.kumpelblase2.remoteentities.api.features;

import java.util.*;

public class FeatureSet
{
	protected final List<Feature> m_features = new ArrayList<Feature>();

	/**
	 * Checks if the entity has a specific kind of feature
	 *
	 * @param inType 	type of the feature to check
	 * @return 			true if the entity has it, false if not
	 */
	public boolean hasFeature(Class<? extends Feature> inType)
	{
		for(Feature feature : this.m_features)
		{
			if(feature.getClass().equals(inType) || inType.isAssignableFrom(feature.getClass()))
				return true;
		}
		return false;
	}

	/**
	 * Adds the feature to the entity
	 *
	 * @param inFeature feature
	 */
	public void addFeature(Feature inFeature)
	{
		this.removeFeature(inFeature.getClass());
		this.m_features.add(inFeature);
	}

	/**
	 * Removes a feature from the entity
	 *
	 * @param inType feature
	 */
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

	/**
	 * Gets a feature by the given type
	 *
	 * @param inType	feature type
	 * @return			feature
	 */
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

	/**
	 * Gets the amount of features on this entity.
	 *
	 * @return  feature count
	 */
	public int size()
	{
		return this.m_features.size();
	}

	public List<Feature> getAllFeatures()
	{
		return this.m_features;
	}
}