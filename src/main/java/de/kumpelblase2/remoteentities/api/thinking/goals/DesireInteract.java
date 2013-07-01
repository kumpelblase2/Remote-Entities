package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;

/**
 * This desire has the same functionality as #DesireLookAtNearest , however it works along other desires.
 */
public class DesireInteract extends DesireLookAtNearest
{
	public DesireInteract(RemoteEntity inEntity, Class<?> inTarget, float inMinDistance)
	{
		super(inEntity, inTarget, inMinDistance);
		this.m_type = DesireType.HAPPINESS;
	}

	public DesireInteract(RemoteEntity inEntity, Class<?> inTarget, float inMinDistance, float inPossibility)
	{
		super(inEntity, inTarget, inMinDistance, inPossibility);
		this.m_type = DesireType.HAPPINESS;
	}
}