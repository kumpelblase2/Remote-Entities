package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_7_R1.EntityBat;
import org.bukkit.entity.Bat;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteBat extends RemoteAttackingBaseEntity<Bat>
{
	public RemoteBat(int inID, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Bat, inManager);
	}

	public RemoteBat(int inID, RemoteBatEntity inEntity, EntityManager inManager)
	{
		this(inID, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Bat";
	}

	public boolean isHanging()
	{
		return ((EntityBat)this.m_entity).bN(); //TODO check!
	}

	public void setHanging(boolean inHanging)
	{
		((EntityBat)this.m_entity).a(inHanging);
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.SLEEPING, "mob.bat.idle");
		this.setSound(EntitySound.HURT, "mob.bat.hurt");
		this.setSound(EntitySound.DEATH, "mob.bat.death");
	}
}