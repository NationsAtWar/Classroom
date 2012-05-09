package org.nationsatwar.nations.events;

/**
 * Listener class for all item activity relating to 'Nations at War'
 * 
 * @author Aculem
 *
 */
public class NationsItemListener extends EntityListener {
	
	@SuppressWarnings("unused")
	private static TownManager plugin;

	public NationsItemListener(TownManager instance) {
		
		plugin = instance;
	}
	
	// Need to record items used by which player and log them in the war object if appropriate.
}