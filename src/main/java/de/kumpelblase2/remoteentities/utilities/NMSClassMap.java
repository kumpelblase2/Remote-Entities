package de.kumpelblase2.remoteentities.utilities;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.*;

@SuppressWarnings("rawtypes")
public class NMSClassMap
{
	private static final Map<Class, Class> s_classes;

	static
	{
		s_classes = new HashMap<Class, Class>();
		s_classes.put(org.bukkit.entity.Entity.class, ReflectionUtil.getNMSClassByName("Entity"));
		s_classes.put(LivingEntity.class, ReflectionUtil.getNMSClassByName("EntityLiving"));
		s_classes.put(Creature.class, ReflectionUtil.getNMSClassByName("EntityCreature"));
		s_classes.put(Monster.class, ReflectionUtil.getNMSClassByName("EntityMonster"));
		s_classes.put(Animals.class, ReflectionUtil.getNMSClassByName("EntityAnimal"));
		s_classes.put(Tameable.class, ReflectionUtil.getNMSClassByName("EntityTameableAnimal"));
		s_classes.put(Ageable.class, ReflectionUtil.getNMSClassByName("EntityAgeable"));
		s_classes.put(Flying.class, ReflectionUtil.getNMSClassByName("EntityFlying"));

		s_classes.put(Bat.class, ReflectionUtil.getNMSClassByName("EntityBat"));
		s_classes.put(Blaze.class, ReflectionUtil.getNMSClassByName("EntityBlaze"));
		s_classes.put(CaveSpider.class, ReflectionUtil.getNMSClassByName("EntityCaveSpider"));
		s_classes.put(Chicken.class, ReflectionUtil.getNMSClassByName("EntityChicken"));
		s_classes.put(Cow.class, ReflectionUtil.getNMSClassByName("EntityCow"));
		s_classes.put(Creeper.class, ReflectionUtil.getNMSClassByName("EntityCreeper"));
		s_classes.put(EnderDragon.class, ReflectionUtil.getNMSClassByName("EntityEnderDragon"));
		s_classes.put(Enderman.class, ReflectionUtil.getNMSClassByName("EntityEnderman"));
		s_classes.put(Ghast.class, ReflectionUtil.getNMSClassByName("EntityGhast"));
		s_classes.put(IronGolem.class, ReflectionUtil.getNMSClassByName("EntityIronGolem"));
		s_classes.put(Slime.class, ReflectionUtil.getNMSClassByName("EntitySlime"));
		s_classes.put(MushroomCow.class, ReflectionUtil.getNMSClassByName("EntityMushroomCow"));
		s_classes.put(Ocelot.class, ReflectionUtil.getNMSClassByName("EntityOcelot"));
		s_classes.put(Pig.class, ReflectionUtil.getNMSClassByName("EntityPig"));
		s_classes.put(PigZombie.class, ReflectionUtil.getNMSClassByName("EntityPigZombie"));
		s_classes.put(Player.class, ReflectionUtil.getNMSClassByName("EntityPlayer"));
		s_classes.put(HumanEntity.class, ReflectionUtil.getNMSClassByName("EntityHuman"));
		s_classes.put(Sheep.class, ReflectionUtil.getNMSClassByName("EntitySheep"));
		s_classes.put(Silverfish.class, ReflectionUtil.getNMSClassByName("EntitySilverfish"));
		s_classes.put(Skeleton.class, ReflectionUtil.getNMSClassByName("EntitySkeleton"));
		s_classes.put(Snowman.class, ReflectionUtil.getNMSClassByName("EntitySnowman"));
		s_classes.put(Spider.class, ReflectionUtil.getNMSClassByName("EntitySpider"));
		s_classes.put(Squid.class, ReflectionUtil.getNMSClassByName("EntitySquid"));
		s_classes.put(Villager.class, ReflectionUtil.getNMSClassByName("EntityVillager"));
		s_classes.put(Witch.class, ReflectionUtil.getNMSClassByName("EntityWitch"));
		s_classes.put(Wither.class, ReflectionUtil.getNMSClassByName("EntityWither"));
		s_classes.put(Wolf.class, ReflectionUtil.getNMSClassByName("EntityWolf"));
		s_classes.put(Zombie.class, ReflectionUtil.getNMSClassByName("EntityZombie"));
		s_classes.put(Horse.class, ReflectionUtil.getNMSClassByName("EntityHorse"));
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