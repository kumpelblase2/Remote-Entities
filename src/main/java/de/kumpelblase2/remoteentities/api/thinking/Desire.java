package de.kumpelblase2.remoteentities.api.thinking;

import de.kumpelblase2.remoteentities.api.RemoteEntity;

public interface Desire
{
	public RemoteEntity getRemoteEntity();
	public int getType();
	public void setType(int inType);
	public boolean isContinous();
	public void startExecuting();
	public void stopExecuting();
	public boolean update();
	public boolean shouldExecute();
	public boolean canContinue();
}
