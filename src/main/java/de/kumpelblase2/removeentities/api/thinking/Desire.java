package de.kumpelblase2.removeentities.api.thinking;

import de.kumpelblase2.removeentities.api.RemoteEntity;

public interface Desire
{
	public RemoteEntity getRemoteEntity();
	public int getType();
	public boolean isContinous();
	public void startExecuting();
	public void stopExecuting();
	public boolean update();
	public boolean shouldExecute();
	public boolean canContinue();
}
