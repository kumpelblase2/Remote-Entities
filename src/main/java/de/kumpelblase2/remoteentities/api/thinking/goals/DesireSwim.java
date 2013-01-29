package de.kumpelblase2.remoteentities.api.thinking.goals;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.persistence.ParameterData;

public class DesireSwim extends DesireBase
{
	public DesireSwim(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = 4;
		this.getEntityHandle().getNavigation().e(true);
	}

    public DesireSwim(ParameterData[] parameters)
    {
        this(((RemoteEntity)parameters[0].value));
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
    public Object[] getConstructionals()
    {
        Object[] constructionals = new Object[1];
        constructionals[0] = this.getRemoteEntity();

        return constructionals;
    }
}
