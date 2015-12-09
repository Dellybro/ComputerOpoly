/*

Creator: Travis Delly
Version: 1.0
Purpose: To play Monopoly

*/

import java.util.*;

public abstract class Opoly implements Cloneable{

	public static ArrayList<Player> computerPlayers = new ArrayList<Player>();
	public static boolean computerTurn = false;
	public static String[] randomName = {"Terry", "Leonard", "Jacob", "Amy", "Perelto", "Holk", "Charles"};



	public static void play(){
		while(true){
			Banker bank = new Banker();

			Player firstPlayer = new Player("Travis", false);


			System.out.println("Welcome to Monopoly!");
			System.out.println("How many computers would you like to play with? ("+Utilities.MAXPLAYERS+" Max)");
			int players = Utilities.inputNumber(Utilities.MAXPLAYERS);

			for(int x = 0; x < players; x++){
				Player computer = new Player(randomName[x], true);
				computerPlayers.add(computer);
			}

			while(firstPlayer.money > 0){
				if(firstPlayer.inJail == true)
					System.out.println("\nYou are currently in Jail\n");

				int choice = Utilities.goMenu();
				System.out.println("----------------------------------------");
				switch(choice){
					case 1:
						for (int x = 0; x < computerPlayers.size(); x++) {
							System.out.println(computerPlayers.get(x).name + "'s position:" + computerPlayers.get(x).currentPosition + "\n");
						}
						System.out.println("Your position: " + firstPlayer.currentPosition + "\n");
						break;
					case 2:
						firstPlayer.playerTurn(bank);
						computerTurn = true;
						break;
					case 3:
						firstPlayer.showCards();
						break;
					case 4:
						System.out.println(firstPlayer.useJailCard(bank));
						computerTurn = true;
						break;
					case 5:
						for (int x = 0; x < computerPlayers.size(); x++) {
							System.out.println(computerPlayers.get(x) + "\n");
						}
						System.out.println(firstPlayer.toString() + "\n");
						break;
					case 6:
						bank.sellHouse(firstPlayer);
						break;
					case 7:
						for (int x = 0; x < computerPlayers.size(); x++) {
							firstPlayer.buyProperty(computerPlayers.get(x));
						}
						break;
					case 8:
						System.out.println(bank);
						break;
					case 9:
						for (Player currP : computerPlayers) {
							System.out.println(currP.playRecap);
						}
						System.out.println(firstPlayer.playRecap);
						break;
					default:
						firstPlayer.money+=2000;
						System.out.println(Utilities.WRONG);
						break;
				}
				if(computerTurn){
					System.out.println("----------------------------------------");
					for (int x = 0; x < computerPlayers.size(); x++) {
						if(computerPlayers.get(x).lost == false){
							computerPlayers.get(x).playerTurn(bank);

							for (int y = 0; y < computerPlayers.size(); y++) {
								if (!(computerPlayers.get(x) == computerPlayers.get(y) && computerPlayers.get(y).lost == false))
									computerPlayers.get(x).compProps(computerPlayers.get(y));
							}
							computerPlayers.get(x).compProps(firstPlayer);

							if(computerPlayers.get(x).money <= 0){
								System.out.println("Lost! Cards go back to Dealer");
								computerPlayers.get(x).playRecap+="Lost! Cards go back to dealer houses go back to dealer\n";
								Utilities.playerLost(computerPlayers.get(x), bank);
								computerPlayers.get(x).lost = true;
							}
						}else{
							System.out.println("-- Computer Player"+computerPlayers.get(x).name+"Lost --");
							computerPlayers.get(x).playRecap = computerPlayers.get(x).name + " lost!\n";
						}
						System.out.println("----------------------------------------");
					}
					System.out.println("---Computer Turns over---");
					System.out.println("You have "+firstPlayer.money+" left.\n");
					computerTurn = false;
				}
				if(checkForWin() == 1)
					break;
			}
			System.out.println("Game over.");

			break;

		}
	}

	public static int checkForWin(){
		for (int x = 0; x < computerPlayers.size(); x++) {
			if(computerPlayers.get(x).lost == false){
				return 0;
			}
		}
		return 1;
	}


}