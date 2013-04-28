package de.kumpelblase2.remoteentities.api.pathfinding;

import org.bukkit.Material;
import org.bukkit.material.Door;
import org.bukkit.material.Gate;

@SuppressWarnings("deprecation")
public class DoorOpenChecker implements MoveChecker
{
	private boolean m_ironDoor;
	
	public DoorOpenChecker()
	{
		this(true);
	}
	
	public DoorOpenChecker(boolean inIgnoreIronDoor)
	{
		this.m_ironDoor = inIgnoreIronDoor;
		Pathfinder.transparentMaterial.add(Material.WOOD_DOOR);
		if(!this.m_ironDoor)
			Pathfinder.transparentMaterial.add(Material.IRON_DOOR_BLOCK);
	}
	
	@Override
	public void checkMove(MoveData inData)
	{
		if(inData.isValid())
			return;
		
		if(inData.getAboveBlock().getType() == Material.FENCE_GATE)
		{
			Gate g = (Gate)inData.getAboveBlock().getState().getData();
			if(g.isOpen())
			{
				inData.setValid(true);
				return;
			}
		}
		else if(inData.getAboveBlock().getType() == Material.WOOD_DOOR || (!this.m_ironDoor && inData.getAboveBlock().getType() == Material.IRON_DOOR_BLOCK))
		{
			Door d = (Door)inData.getAboveBlock().getState().getData();
			if(d.isOpen())
				inData.setValid(true);
		}
	}
	
	public void setIgnoreIronDoor(boolean inIgnore)
	{
		this.m_ironDoor = inIgnore;
	}
	
	public boolean isIronDoorIgnored()
	{
		return this.m_ironDoor;
	}
}