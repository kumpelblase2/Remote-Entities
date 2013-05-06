package de.kumpelblase2.remoteentities.utilities;

import net.minecraft.server.v1_5_R3.*;
import org.bukkit.entity.*;
import java.util.*;

@SuppressWarnings("rawtypes")
public class NMSClassMap
{
	private static final Map<Class, Class> s_classes;
	
	static
	{
		s_classes = new HashMap<Class, Class>();
		s_classes.put(org.bukkit.entity.Entity.class, net.minecraft.server.v1_5_R3.Entity.class);
		s_classes.put(LivingEntity.class, EntityLiving.class);
		s_classes.put(Creature.class, EntityCreature.class);
		s_classes.put(Monster.class, EntityMonster.class);
		s_classes.put(Animals.class, EntityAnimal.class);
		s_classes.put(Tameable.class, EntityTameableAnimal.class);
		s_classes.put(Ageable.class, EntityAgeable.class);
		s_classes.put(Flying.class, EntityFlying.class);
		
		s_classes.put(Bat.class, EntityBat.class);
		s_classes.put(Blaze.class, EntityBlaze.class);
		s_classes.put(CaveSpider.class, EntityCaveSpider.class);
		s_classes.put(Chicken.class, EntityChicken.class);
		s_classes.put(Cow.class, EntityCow.class);
		s_classes.put(Creeper.class, EntityCreeper.class);
		s_classes.put(EnderDragon.class, EntityEnderDragon.class);
		s_classes.put(Enderman.class, EntityEnderman.class);
		s_classes.put(Ghast.class, EntityGhast.class);
		s_classes.put(IronGolem.class, EntityIronGolem.class);
		s_classes.put(Slime.class, EntitySlime.class);
		s_classes.put(MushroomCow.class, EntityMushroomCow.class);
		s_classes.put(Ocelot.class, EntityOcelot.class);
		s_classes.put(Pig.class, EntityPig.class);
		s_classes.put(PigZombie.class, EntityPigZombie.class);
		s_classes.put(Player.class, EntityPlayer.class);
		s_classes.put(HumanEntity.class, EntityHuman.class);
		s_classes.put(Sheep.class, EntitySheep.class);
		s_classes.put(Silverfish.class, EntitySilverfish.class);
		s_classes.put(Skeleton.class, EntitySkeleton.class);
		s_classes.put(Snowman.class, EntitySnowman.class);
		s_classes.put(Spider.class, EntitySpider.class);
		s_classes.put(Squid.class, EntitySquid.class);
		s_classes.put(Villager.class, EntityVillager.class);
		s_classes.put(Witch.class, EntityWitch.class);
		s_classes.put(Wither.class, EntityWither.class);
		s_classes.put(Wolf.class, EntityWolf.class);
		s_classes.put(Zombie.class, EntityZombie.class);
	}
	
	public static Class getNMSClass(Class inClass)
	{
		Class bukkit = null;
		Class search = inClass;
		while(bukkit == null && search != Object.class)
		{
			bukkit = s_classes.get(search);
			search = search.getSuperclass();
		}
		return bukkit;
	}
}