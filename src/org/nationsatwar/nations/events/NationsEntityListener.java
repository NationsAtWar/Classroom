package org.nationsatwar.nations.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class NationsEntityListener extends EntityListener {
	
	private static TownManager plugin;
	
	public NationsEntityListener(TownManager instance) {
		
		plugin = instance;
	}

	@Override
    public synchronized void onEntityDamage(EntityDamageEvent event) {
		
		if (!(event.getEntity() instanceof Player))
			return;
		
		// This whole block just checks to see if this event was a PvP attack, and then sets up the variables appropriately.
		if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent sub = (EntityDamageByEntityEvent) event;
            
    		if (sub.getEntity() instanceof Player && 
    				(sub.getDamager() instanceof Player || sub.getDamager() instanceof CraftWolf)) {
				User damaged = plugin.userManager.getUser((Player)sub.getEntity());
				User damager = null;
				
				if (sub.getDamager() instanceof CraftWolf) {
					CraftWolf wolf = (CraftWolf) sub.getDamager();
					if ((Player) wolf.getOwner() != null)
						damager = plugin.userManager.getUser((Player) wolf.getOwner());
				} else
					damager = plugin.userManager.getUser((Player)sub.getDamager());
				
				if (damaged == null || damager == null)
					return;
				
				/*					String lastDamager = damager.getName();
				String lastDamaged = damaged.getName();
				int damage = sub.getDamage();
				
			// This will cancel the event if this shit occurred in a PvP off territory
				Plot plot = plugin.plotManager.getPlot(damaged.getLocationKey());
				Nation damagedNation = plugin.nationManager.getNationByUser(damaged.getName());
				
				if ((plot != null && plugin.nationManager.getNation(plot.nation).adminNoPVP) ||
						(damagedNation != null && !damagedNation.pvpEnabled && plot != null && 
	    						plot.getNation().equalsIgnoreCase(damagedNation.getName()))) {
					sub.setCancelled(true);
					sub.setDamage(0);
					sub.getEntity().setFireTicks(0);
    	            if(sub.getCause().equals(DamageCause.FIRE) || sub.getCause().equals(DamageCause.FIRE_TICK))
    	            	sub.setCancelled(true);
    	            return;
				} */
    		}
		}
	}
	
	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if(event.getEntity() instanceof Monster) {
			LivingEntity livingEntity = (LivingEntity)event.getEntity();
			
			Plot plot = plugin.plotManager.getPlotByLocation(plugin.util.getLocationKey(livingEntity.getLocation()));
			if(plot != null) {
				if (MobRemovalThread.isRemovingTownEntity(livingEntity)) {
					event.setCancelled(true);
				}
			}
		}
	}
}