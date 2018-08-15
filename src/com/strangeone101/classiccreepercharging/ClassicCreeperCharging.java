package com.strangeone101.classiccreepercharging;

import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.projectkorra.ability.LightningAbility;
import com.projectkorra.projectkorra.event.AbilityDamageEntityEvent;
import com.projectkorra.projectkorra.event.EntityBendingDeathEvent;

public class ClassicCreeperCharging extends JavaPlugin implements Listener {
	
	public static double CHANCE = 20.0D; //20% chance to charge per strike
	public static boolean DANGER = true; //Whether creepers become more dangerous in this plugin, too. 
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		ReflectionCrap.setPlugin(this);
		this.getLogger().info("ClassicCreeperCharging done! Enjoy your new explosive creepers!");
	}

	@EventHandler
	public void onDamage(AbilityDamageEntityEvent e) {
		if (e.getAbility() instanceof LightningAbility && e.getEntity() instanceof Creeper) {
			if (Math.random() * 100 <= CHANCE) {
				Creeper creeper = (Creeper) e.getEntity();
				if (creeper.isPowered() && DANGER) {
					ReflectionCrap.explodeCreeper(creeper);
					return;
				} 
				
				creeper.setPowered(true);
			}
		}
	}

	@EventHandler
	public void onDeath(EntityBendingDeathEvent e) {
		if (e.getAbility() instanceof LightningAbility && e.getEntity() instanceof Creeper) {
			
			Creeper creeper = (Creeper) e.getEntity();
			if ((Math.random() * 100 <= CHANCE || creeper.isPowered()) && DANGER) {
				ReflectionCrap.explodeCreeper(creeper);
				return;
			} 
		}
	}
}
