package org.nationsatwar.nations.managers;

import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.NationsObject;
import org.nationsatwar.nations.objects.Plot;

public class PlotManager extends NationsManagement {
	private HashMap<Integer, Plot> plotMap = new HashMap<Integer, Plot>();

	public PlotManager(PluginBase plugin) {
		super(plugin);
	}
	
	@Override
	public void loadAll() {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		plotMap.clear();
		for (NationsObject obj : nations.database.gatherDataset(new Plot(0, null))) {
			Plot plot = (Plot) obj;
			if (!plotMap.containsKey(plot.getID()))
				plotMap.put(plot.getID(), plot);
		}
	}

	@Override
	public void saveAll() {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		for (Plot plot : plotMap.values()) {
			nations.database.save(plot);
		}
	}

	@Override
	public void deleteAll() {
		if(!(plugin instanceof Nations)) {
			return;
		}
		Nations nations = (Nations) plugin;
		
		for (Plot plot : plotMap.values()) {
			nations.database.delete(plot);
		}
		plotMap.clear();
	}
	
	public void showBoundaries(Plot plot) {
		World world = plugin.getServer().getWorld(plot.getWorld());
		int x = plot.getX();
		int z = plot.getZ();
		int id = 50;

		world.getBlockAt(x * 16, world.getHighestBlockYAt(x * 16, z * 16), z * 16).setTypeId(id);
		world.getBlockAt((x * 16) + 15, world.getHighestBlockYAt((x * 16) + 15, z * 16), z * 16).setTypeId(id);
		world.getBlockAt(x * 16, world.getHighestBlockYAt(x * 16, (z * 16) + 15), (z * 16) + 15).setTypeId(id);
		world.getBlockAt((x * 16) + 15, world.getHighestBlockYAt((x * 16) + 15, (z * 16) + 15), (z * 16) + 15).setTypeId(id);
	}
	
	public Plot createPlot(Location loc) {
		int newKey = 0; 
		try {
			newKey = Collections.max(plotMap.keySet())+1;
		} catch(NoSuchElementException e) {
			
		}
		Plot newPlot = new Plot(newKey, loc);
		if(this.addPlot(newPlot)) {
			return newPlot;
		}
		return null;
	}

	private boolean addPlot(Plot plot) {
		this.plotMap.put(plot.getID(), plot);
		return true;
	}

	public Plot getPlotByLocation(Location location) {
		for(Plot plot : this.plotMap.values()) {
			if(plot.getLocationKey().equalsIgnoreCase(this.getLocationKey(location))) {
				return plot;
			}
		}
		return null;
	}
	
	public String getLocationKey(Location location) {
		return location.getWorld()+";"+location.getChunk().getX()+";"+location.getChunk().getZ();
	}

}
