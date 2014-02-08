package de.kumpelblase2.remoteentities.api.thinking;

public class DesireItem
{
	private final Desire m_desire;
	private final int m_priority;

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

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;

		if(!(o instanceof DesireItem))
			return false;

		DesireItem item = (DesireItem)o;

		if(this.m_priority != item.m_priority)
			return false;

		return !(this.m_desire != null ? !this.m_desire.equals(item.m_desire) : item.m_desire != null);
	}

	@Override
	public int hashCode()
	{
		int result = this.m_desire != null ? this.m_desire.hashCode() : 0;
		result = 31 * result + this.m_priority;
		return result;
	}
}