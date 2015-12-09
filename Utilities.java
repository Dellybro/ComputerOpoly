/*

Creator: Travis Delly
Version: 1.0
Purpose: To play Monopoly

*/


import java.util.*;

public class Utilities{

	public static final int MAXPLAYERS = 7;
	public static final int TAX = 75;
   	public static final int SEED_CASH = 1500;     // $ at start-of-game
	public static Scanner input = new Scanner(System.in);
	public static final String WRONG = "Enter one of these choices..";
	public static Random ran = new Random();

	public static void playerLost(Player player, Banker bank){
		for (PropertyCard card : player.properties){
			card.owner = null;
			if(card.houses == 5)
				bank.hotels+=1;
			else
				bank.houses+=card.houses;
			card.houses=0;
		}
		for (UtilityCard card : player.utilities) {
			card.owner = null;
		}
		for(RailRoadCard card : player.railroads){
			card.owner = null;
		}
		player.properties.clear();
		player.utilities.clear();
		player.railroads.clear();
	}

	public static PropertyCard findCard(int cardNumber, Banker banker){
		for (int x = 0; x < banker.propertyCards.size(); x++) {
			if(banker.propertyCards.get(x).position == cardNumber){
				return banker.propertyCards.get(x);
			}
		}
		return null;
	}
	public static UtilityCard findUtilityCard(int cardNumber, Banker banker){
		for (int x = 0; x < banker.utilityCards.size(); x++) {
			if(banker.utilityCards.get(x).position == cardNumber){
				return banker.utilityCards.get(x);
			}
		}
		return null;
	}
	public static RailRoadCard findRailCard(int cardNumber, Banker banker){
		for (int x = 0; x < banker.railRoadCards.size(); x++) {
			if(banker.railRoadCards.get(x).position == cardNumber){
				return banker.railRoadCards.get(x);
			}
		}
		return null;
	}

	public static int inputNumber(int ammount){
		while(true){
			try{
				int choice = input.nextInt();
				if(choice > ammount){
					System.out.println(ammount + " Maximum");
				}else {
					return choice;
				}
			}catch(Exception e){
				input.next();
				System.out.println(WRONG);
			}
		}
	}
	public static int yesOrNO(){
		Scanner input = new Scanner(System.in);
		while(true){
			try{
				int choice = input.nextInt();
				return choice;
			}catch(Exception e){
				input.next();
				System.out.println("1 or 2?");
			}
		}
	}
	public static int goMenu(){
		System.out.println("What would you like to do.");
		System.out.println("1. Player Positions\n" +
							"2. Roll dice\n" + 
							"3. Current Cards\n"+
							"4. Use Jail Free\n" + 
							"5. Check Players\n" +
							"6. Buy houses\n" +
							"7. Buy from players\n" +
							"8. Check banker\n"+
							"9. Last play Recap");
		while(true){
			try{
				int choice = input.nextInt();
				return choice;
			}catch(Exception e){
				input.next();
				System.out.println(Utilities.WRONG);
			}
		}
	}

	public static void waitme(int time){
		try {
		    Thread.sleep(time);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}

}