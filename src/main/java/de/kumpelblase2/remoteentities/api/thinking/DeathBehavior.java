package de.kumpelblase2.remoteentities.api.thinking;

import de.kumpelblase2.remoteentities.api.RemoteEntity;

public abstract class DeathBehavior extends BaseBehavior
{
	public DeathBehavior(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_name = "Death";
	}

	/**
	 * This method gets called when the entities dies.
	 */
	public abstract void onDeath();
}
