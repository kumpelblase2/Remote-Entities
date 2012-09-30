package de.kumpelblase2.removeentities.thinking;

import java.util.HashSet;
import java.util.Set;

public class EnterSightBehaviour implements Behaviour
{
	protected final String NAME = "EnterSight";
	private int m_tick = 20;
	private Set<String> inRange;
	
	@Override
	public void run()
	{
		this.m_tick--;
		if(this.m_tick <= 0)
		{
			this.m_tick = 20;
			//TODO
		}
	}

	@Override
	public String getName()
	{
		return this.NAME;
	}

	@Override
	public void onRemove()
	{
		this.inRange.clear();
	}

	@Override
	public void onAdd()
	{
		this.inRange = new HashSet<String>();
	}
}
