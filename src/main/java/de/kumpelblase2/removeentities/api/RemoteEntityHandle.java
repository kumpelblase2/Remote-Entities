package de.kumpelblase2.removeentities.api;

import org.bukkit.inventory.InventoryHolder;
import de.kumpelblase2.removeentities.api.thinking.PathfinderGoalSelectorHelper;

public interface RemoteEntityHandle extends InventoryHolder
{
	public RemoteEntity getRemoteEntity();
	public void setupStandardGoals();
	public PathfinderGoalSelectorHelper getGoalSelector();
	public PathfinderGoalSelectorHelper getTargetSelector();
	public void setMaxHealth(int inHealth);
}
