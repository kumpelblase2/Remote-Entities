package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireSwim extends DesireBase
{
	public DesireSwim(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = 4;
		this.getEntityHandle().getNavigation().e(true);
	}

	@Override
	public boolean shouldExecute()
	{
		return this.getEntityHandle() != null && (this.getEntityHandle().H() || this.getEntityHandle().J());
	}

    @Override
	public boolean update()
	{
		if(this.getEntityHandle().aB().nextFloat() < 0.8F)
			this.getEntityHandle().getControllerJump().a();
		
		return true;
	}

    @Override
    public String[] getConstructionData()
    {
        String[] constructionData = new String[1];
        constructionData[0] = "EntityID = " + this.getRemoteEntity().getID();

        return constructionData;
    }
}
