package de.kumpelblase2.removeentities.thinking;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import de.kumpelblase2.removeentities.entities.RemoteEntity;

public class Mind
{
	private Map<String, Behaviour> m_behaviours;
	private RemoteEntity m_entity;
	private boolean m_canFeel = true;
	private Desire m_currentDesire;
	
	public Mind(RemoteEntity inEntity)
	{
		this.m_entity = inEntity;
		this.m_behaviours = new HashMap<String, Behaviour>();
	}
	
	public void addBehaviour(Behaviour inBehaviour)
	{
		this.m_behaviours.put(inBehaviour.getName(), inBehaviour);
	}
	
	public boolean removeBehaviour(String inName)
	{
		return this.m_behaviours.remove(inName) != null;
	}
	
	public boolean hasBehaviour(String inName)
	{
		return this.m_behaviours.containsKey(inName);
	}
	
	public boolean canFeel()
	{
		return this.m_canFeel;
	}
	
	public void blockFeelings(boolean inState)
	{
		this.m_canFeel = inState;
	}
	
	public RemoteEntity getEntity()
	{
		return this.m_entity;
	}
	
	public Behaviour getBehaviour(String inName)
	{
		return this.m_behaviours.get(inName);
	}
	
	public void setCurrentDesire(Desire inDesire)
	{
		this.m_currentDesire = inDesire;
	}
	
	public Desire getCurrentDesire()
	{
		return this.m_currentDesire;
	}
	
	public Collection<Behaviour> getBehaviours()
	{
		return this.m_behaviours.values();
	}

	public void clearBehaviours()
	{
		this.m_behaviours.clear();
	}
}