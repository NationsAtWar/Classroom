package org.nationsatwar.nations.managers;

import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.objects.Plot;

public class PlotManager extends NationsManagement {
	private HashMap<String, Plot> plotMap= new HashMap<String, Plot>();

	public PlotManager(PluginBase plugin) {
		super(plugin);
	}
	
	public void showBoundaries(Plot plot) {
		World world = plugin.getServer().getWorld(plot.getWorld());
		int x = plot.getX();
		int z = plot.getZ();
		int id = 76;

		world.getBlockAt(x * 16, world.getHighestBlockYAt(x * 16, z * 16), z * 16).setTypeId(id);
		world.getBlockAt((x * 16) + 15, world.getHighestBlockYAt((x * 16) + 15, z * 16), z * 16).setTypeId(id);
		world.getBlockAt(x * 16, world.getHighestBlockYAt(x * 16, (z * 16) + 15), (z * 16) + 15).setTypeId(id);
		world.getBlockAt((x * 16) + 15, world.getHighestBlockYAt((x * 16) + 15, (z * 16) + 15), (z * 16) + 15).setTypeId(id);
	}

	public boolean addPlot(Plot plot) {
		this.plotMap.put(plot.getLocationKey(), plot);
		return true;
	}

	public boolean isPlot(Plot plot) {
		return this.plotMap.containsValue(plot);
	}

}
