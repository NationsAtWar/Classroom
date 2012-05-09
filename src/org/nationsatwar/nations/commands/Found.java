package org.nationsatwar.nations.commands;

public class Found extends NationsCommand {

	double nationPrice = Double.parseDouble(plugin.config.get("nation_price"));
	
	public Found(User commandSender, String[] command) {
		super(commandSender, command);
	} // Found()
	
	@Override
	public void run() {
		
		// -found || found help
		if(command.length == 1 || command[1].equalsIgnoreCase("help")) {
			commandSender.message(Color.DarkRed() + "[Nations at War]" + Color.DarkAqua() + " -=[FOUND]=-");
			commandSender.message(Color.DarkRed() + "[Nations at War]" + Color.Green() + " i.e. '/naw found [nation name]'");
			commandSender.message(Color.Yellow() + "Establishes a nation for you to dick around with.");
			return;
		}
		
		MethodAccount userWallet = commandSender.getAccount();
		
		String nationName = this.connectStrings(command, 1, command.length);
		
		if (nationName.length() > 30) {
			commandSender.message(Color.Red() + "Name too long: " + Color.Yellow() + "Nation names can only be 32 characters long. " +
					"Blame iConomy.");
			return;
		}
		
		int aposAmount = 0;
		for (int i=0; i<nationName.length(); i++) {
			if (nationName.charAt(i) == '\'')
				aposAmount += 1;
			if (!Character.isLetterOrDigit(nationName.charAt(i)) && nationName.charAt(i) != ' ' && nationName.charAt(i) != '\'') {
				commandSender.message(Color.Red() + "Invalid Nation Name: " + Color.Yellow() + "Letters or numbers only.");
				return;
			}
		}
		
		if (aposAmount > 1) {
			commandSender.message(Color.Red() + "Too many apostrophes: " + Color.Yellow() + ":/");
			return;
		}
		
		if (!plugin.nationManager.exists(nationName)) {
			
			if (!plugin.userManager.isInNation(commandSender.getName())) {
				if (userWallet.balance() >= nationPrice) {
					userWallet.subtract(nationPrice);
					plugin.nationManager.foundNation(commandSender, nationName);
					plugin.messageAll(Color.Aqua() + "The Nation of '" + nationName + "' has been founded!");
					return;
				} else {
					commandSender.message(Color.Red() + "Insufficient funds: " + Color.Yellow() + "You need $" + 
							(nationPrice - userWallet.balance()) + " more to afford a new nation.");
					return;
				}
			} else {
				commandSender.message(Color.Yellow() + "You are already a member of a nation. You must leave that nation " +
						"before you can found a new one!");
				return;
			}
		}
		
		else {
			commandSender.message("A Nation with that name already exists!");
			return;
		}	
	}
}
