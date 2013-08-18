package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_6_R2.EntitySpider;
import org.bukkit.entity.CaveSpider;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.EntitySound;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class RemoteCaveSpider extends RemoteAttackingBaseEntity<CaveSpider>
{
	public RemoteCaveSpider(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteCaveSpider(int inID, RemoteCaveSpiderEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.CaveSpider, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "CaveSpider";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.spider.say");
		this.setSound(EntitySound.HURT, "mob.spider.say");
		this.setSound(EntitySound.DEATH, "mob.spider.death");
		this.setSound(EntitySound.STEP, "mob.spider.step");
	}
}