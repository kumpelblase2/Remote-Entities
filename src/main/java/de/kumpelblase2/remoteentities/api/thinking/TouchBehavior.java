package de.kumpelblase2.remoteentities.api.thinking;

import org.bukkit.entity.Player;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public abstract class TouchBehavior extends BaseBehavior
{	
	public TouchBehavior(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_name = "Touch";
	}
	
	/**
	 * Gets called when the entity gets Touched by a player
	 * 
	 * @param inPlayer player
	 */
	public abstract void onTouch(Player inPlayer);
}
