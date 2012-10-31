package de.kumpelblase2.remoteentities.api.thinking;

public class DesireItem
{
	private final Desire m_desire;
	private int m_priority;
	
	public DesireItem(Desire inDesire, int inPriority)
	{
		this.m_desire = inDesire;
		this.m_priority = inPriority;
	}
	
	public Desire getDesire()
	{
		return this.m_desire;
	}
	
	public int getPriority()
	{
		return this.m_priority;
	}
}
