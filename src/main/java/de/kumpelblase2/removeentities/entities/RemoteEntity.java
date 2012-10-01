package de.kumpelblase2.removeentities.entities;

import net.minecraft.server.EntityLiving;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.removeentities.features.FeatureSet;
import de.kumpelblase2.removeentities.thinking.Mind;

public interface RemoteEntity
{
	public int getID();
	public RemoteEntityType getType();
	public Mind getMind();
	public LivingEntity getBukkitEntity();
	public EntityLiving getHandle();
	public FeatureSet getFeatures();
	public void move(Location inLocation);
	public void teleport(Location inLocation);
	public void spawn(Location inLocation);
	public void despawn();
	public boolean isSpawned();
	public void setMaxHealth(int inMax);
	public int getMaxHealth();
	public void setStationary(boolean inState);
	public boolean isStationary();
}
