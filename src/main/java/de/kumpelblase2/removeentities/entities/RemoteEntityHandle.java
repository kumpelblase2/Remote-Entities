package de.kumpelblase2.removeentities.entities;

import org.bukkit.inventory.InventoryHolder;
import de.kumpelblase2.removeentities.thinking.PathfinderGoalSelectorHelper;

public interface RemoteEntityHandle extends InventoryHolder
{
	public RemoteEntity getRemoteEntity();
	public void setupStandardGoals();
	public PathfinderGoalSelectorHelper getGoalSelector();
	public PathfinderGoalSelectorHelper getTargetSelector();
}
