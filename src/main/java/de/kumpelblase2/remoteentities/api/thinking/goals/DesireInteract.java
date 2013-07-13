package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;

/**
 * This desire has the same functionality as #DesireLookAtNearest , however it works along other desires.
 */
public class DesireInteract extends DesireLookAtNearest
{
	@Deprecated
	public DesireInteract(RemoteEntity inEntity, Class<?> inTarget, float inMinDistance)
	{
		super(inEntity, inTarget, inMinDistance);
		this.m_type = DesireType.HAPPINESS;
	}

	@Deprecated
	public DesireInteract(RemoteEntity inEntity, Class<?> inTarget, float inMinDistance, float inPossibility)
	{
		super(inEntity, inTarget, inMinDistance, inPossibility);
		this.m_type = DesireType.HAPPINESS;
	}

	public DesireInteract(Class<?> inTarget, float inMinDistance)
	{
		super(inTarget, inMinDistance);
		this.m_type = DesireType.HAPPINESS;
	}

	public DesireInteract(Class<?> inTarget, float inMinDistance, float inPossibility)
	{
		super(inTarget, inMinDistance, inPossibility);
		this.m_type = DesireType.HAPPINESS;
	}
}