package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;

public class DesireInteract extends DesireLookAtNearest
{
	public DesireInteract(RemoteEntity inEntity, Class<?> inTarget, float inMinDistance)
	{
		super(inEntity, inTarget, inMinDistance);
		this.m_type = 3;
	}
	
	public DesireInteract(RemoteEntity inEntity, Class<?> inTarget, float inMinDistance, float inPossibility)
	{
		super(inEntity, inTarget, inMinDistance, inPossibility);
		this.m_type = 3;
	}
}
