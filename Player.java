/*

Creator: Travis Delly
Version: 1.0
Purpose: To play Monopoly

*/


import java.util.*;

public class Player{
	public String playRecap;
	public String name;
	public int money;
	public boolean computer;
	public boolean inJail;
	public int currentPosition;
	public ArrayList<PropertyCard> properties = new ArrayList<PropertyCard>();
	public ArrayList<UtilityCard> utilities = new ArrayList<UtilityCard>();
	public ArrayList<RailRoadCard> railroads = new ArrayList<RailRoadCard>();
	public ArrayList<Object> getOutOfJailCards = new ArrayList<Object>();

	public int houses;
	public int hotels;

	public boolean lost;

	private int reRoll;
	private int jailTurns;

	public Player(String name, boolean computer){
		this.lost = false;
		this.computer = computer;
		this.name = name;
		playRecap = name +"\n";
		money = Utilities.SEED_CASH;
		inJail = false;
		currentPosition = 0;
		jailTurns = 0;
	}


	public String toString(){
		return "Name: " + name + "\nMoney: " + money + "\nIn Jail? " + inJail +
			"\nPosition on Board: " + currentPosition + "\nProperty Cards: " + properties.size() +
			"\nUtility Cards: " +utilities.size()+"\nRailRoad Cards: "+railroads.size()+"\nGet out of Jail Cards: "+getOutOfJailCards.size();
	}

	public String useJailCard(Banker bank){
		/*
			- Check if in jail, if in jail, check if there is a get out of jail card, if not return
			- If get out of jail card present, get out of jail.
		*/
		if(inJail){
			if(getOutOfJailCards.size() > 0){
				if(getOutOfJailCards.get(0) instanceof CommunityCard){
					bank.communityCards.add((CommunityCard)getOutOfJailCards.get(0));
					getOutOfJailCards.remove(0);
				}else{
					bank.chanceCards.add((ChanceCard)getOutOfJailCards.get(0));
					getOutOfJailCards.remove(0);
				}
				inJail = false;
				return "You are out of jail, FREE!!";
			}else{
				return "You have 0 get out of jail free cards";
			}
		}else{
			return "Your not in jail!";
		}
	}

	public int roll(){
		/*
			- Check if in jail
			- If in jail, ask to pay to get out of jail.
			- If not, roll dice.
			- Keep counter for rerolls. If rerolled 3x go to jail and return 3.
			- If position if over 40, use subtract to get correct new position, if not subtract = 0
		*/
		if(money <= 0){
			return 5;
		}
		if(inJail == true){
			System.out.println("In jail, would you like to pay $50 to get out of jail? (Yes: 1 | No: 0)");
			//If computer
			if(computer == true){
				inJail = false;
				System.out.println("Computer player "+name+" has paid to get out of jail!");
				playRecap+= name+ " paid to get out of jail.\n";
			} else {
				if(jailTurns == 3){
					System.out.println("You have been released after 3 turns!\n" +
							"Welcome back to Society "+name+" !");
					playRecap+=name+" has been released from jail after 3 turns.\n";
					jailTurns = 0;
					inJail = false;
				} else {
					int choice = Utilities.yesOrNO();
					if(choice == 1){
						if(money >= 50){
							System.out.println("You are out of jail!");
							money-=50;
							jailTurns = 0;
							inJail = false;
						} else {
							System.out.println("You are to poor to buy your way out of this one!");
							playRecap+="In jail";
							jailTurns += 1;
						}
					} else {
						Roll.rollBothDice();
						if(Roll.dice1 == Roll.dice2){
							inJail = false;
							System.out.println("You rolled out of jail!");
							currentPosition += Roll.dice1+Roll.dice2;
							jailTurns = 0;
						}else{
							System.out.println("Sit and rot! No doubles for you!");
							playRecap+=" In jail\n";
							jailTurns +=1;
						}
					}
				}
			}
			return 2;
		}else {
			Roll.rollBothDice();
			int subtract = 0;
			
			if(currentPosition + Roll.dice1 + Roll.dice2 > 39){
				Banker.payPlayerStart(this);
				subtract = 39-currentPosition;
				currentPosition = 0;
			}
			if(Roll.dice1 == Roll.dice2){
				reRoll +=1;
				if(reRoll == 3){
					System.out.println(name + " got pulled over for speeding! Go directly to Jail!");
					playRecap+= name + " got pulled over for speeding. " + name + " is in jail!\n";
					inJail = true;
					currentPosition = Board.JAIL;
					reRoll = 0;
					return 3;
				} else {
					currentPosition = currentPosition + Roll.dice1 + Roll.dice2 - subtract;
					System.out.println(name + " rolled a double! Current position is: " + currentPosition);
					playRecap +=  "Rolled " + Roll.dice1 + " and " + Roll.dice2 + " it's a double!\n";
					return 1;
				}
			}else{
				currentPosition = currentPosition + Roll.dice1 + Roll.dice2 - subtract;
				System.out.println(name + " moved to space: " + currentPosition);
				playRecap +=  "Rolled " + Roll.dice1 + " and " + Roll.dice2 + "\n";
				reRoll = 0;
				return 0;
			}
		}
	}
	public void showCards(){
		/*Pretty simple*/
		System.out.println(name + "'s Current Cards");
		for(PropertyCard card : properties){
			System.out.println(card.toString());
		}
	}
	public void playerTurn(Banker bank){
		playRecap = name + "\n";
		/*
			This works for both the computer and human..
			if roll = 2, 3 break without doing anything, jail returns
			if roll = 1 reroll getspaceinfo
			if roll = 0 getspaceinfo
		*/
		System.out.println(name + " Going...");
		while(true){
			int roll = roll();
			if(roll == 2){
				break;
			}else if(roll == 3){
				break;
			} else if(roll == 1){
				getSpaceInfo(bank);
				if(inJail)
					break;
				System.out.println("Re Roll!");
				Utilities.waitme(2000);
			} else if(roll == 5){
				System.out.println("Lost during a Re Roll");
				break;
			} else {
				getSpaceInfo(bank);
				break;
			}
		}
		if(computer){
			bank.sellHouse(this);
		}
	}

	public void buyProperty(Player currentPlayer){
		/*
			- Buy property from computer player.
		*/
		ArrayList<Buyable> list = new ArrayList<Buyable>();
		for (PropertyCard card : currentPlayer.properties) {
			if(card.houses == 0){
				list.add(card);
			}
		}
		for (UtilityCard card : currentPlayer.utilities)
			list.add(card);
		for (RailRoadCard card : currentPlayer.railroads)
			list.add(card);

		while(true && list.size() > 0){
			for (int x = 0; x < list.size(); x++) {
				System.out.println(x+") " + list.get(x).name + "| Group: "+list.get(x).getColor() + " | - price 300 - | Owned by " + currentPlayer.name);
			}
			System.out.println("Which property would you like to purchase from "+ currentPlayer.name+"?" +
				"\nType " + list.size() + " to go to next player!");
			int choice = Utilities.inputNumber(list.size());

			if(choice == list.size()){
				System.out.println("Ok! Next!");
				break;
			}else{
				if(this.money >= 300){
					System.out.println("You have bought " + list.get(choice).name + "!");
					currentPlayer.money += 300;
					this.money-=300;

					if(list.get(choice) instanceof PropertyCard){

						PropertyCard card = (PropertyCard)list.get(choice);

						this.properties.add(card);
						currentPlayer.properties.remove(card);

					}else if(list.get(choice) instanceof UtilityCard){
						UtilityCard card = (UtilityCard)list.get(choice);

						this.utilities.add(card);
						currentPlayer.utilities.remove(card);

					}else if(list.get(choice) instanceof RailRoadCard){
						RailRoadCard card = (RailRoadCard)list.get(choice);

						this.railroads.add(card);
						currentPlayer.railroads.remove(card);
					}

					list.get(choice).owner = this;
					list.remove(choice);
				}else{
					System.out.println("You cannot buy this property, insufficient funds");
				}
			}
		}
	}

	public void getSpaceInfo(Banker banker){
		String boardSpot = Board.board[currentPosition];
		/*
			- Get string spot from board
			- Board spaces, COMM, CHAN, TAX, JAIL, GOTO, FREE, GO, UTIL, RR, positions.
			- If landed on Util RR or Positions all follow similiar algorithm
			- Find card using Utilities.
			- check if card owner is null (banker)
			- if owned pay player, if owned by self do nothing.
			- if it's computers turn, buy the property if money allows it 
			- else ask to buy card, 1 yes 2 no.
			- If yes add card to player deck
		*/
		if(boardSpot == "COMM"){ //COMMUNITY
			System.out.println("Pulling Community Chest Card");
			playRecap+="Landed on Community\n";
			banker.dispenseCard(2, this);
		}else if(boardSpot == "CHAN"){ //CHANCE
			System.out.println("Pulling Chance Card");
			playRecap+="Landed on Chance\n";
			banker.dispenseCard(1, this);
		}else if(boardSpot == "TAX"){ //TAX
			banker.payBanker(this, Utilities.TAX);
			playRecap+= "Lands on Tax\n";
			System.out.println("Landed on Tax Space! Pay up!");
		}else if(boardSpot == "JAIL"){ // JAIL
			playRecap+="Visiting jail\n";
			System.out.println("Visiting your friends in jail...");
		}else if(boardSpot == "GOTO"){ //GOTO
			playRecap+="Landed on go to jail!\n";
			inJail = true;
			currentPosition = Board.JAIL;
			System.out.println("Landed on Go to jail! Position reset to 10");
		}else if(boardSpot == "FREE"){ // FREE
			banker.payMiddleMoney(this);
			playRecap+="GOT PAID MIDDLE MONEY!\n";
			System.out.println("Landed on Free Parking! Collecting the money in the middle!");
		}else if(boardSpot == "GO"){ // GO
			playRecap+="Landed on GO!\n";
			System.out.println("Landed on go! Collecting $200");
		}else if(boardSpot=="UTIL"){
			System.out.println("Landed on a Utilities! -- INFO BELOW --");
			int cardNumber = this.currentPosition+1;
			UtilityCard card = Utilities.findUtilityCard(cardNumber, banker);

			playRecap+="Landed on "+card.name +"\n";
			System.out.println(card);
			if(card.owner == null){
				if(this.computer == true){
					if((this.money-card.price)>50){
						banker.sellUtility(this, card);
						System.out.println("Computer "+name+" has chosen to purchase " + card.name);
						playRecap+= "Computer "+name+" purchased " + card.name+"\n";
					} else {
						System.out.println("Computer "+name+" has chosen not to buy " + card.name);
					}

				}else{
					System.out.println("Would you like to buy this property? (1)YES (2)NO");
					int choice = Utilities.yesOrNO();
					if(choice == 1){
						banker.sellUtility(this, card);
						System.out.println("Press anything to proceed...");
						Utilities.input.nextLine();
					}
				}
			} else if (card.owner != this){
				System.out.println("Property owned by " + card.owner.name);
				System.out.println(name + " must pay rent!");
				card.payRent(this);
			}else if(card.owner == this){
				System.out.println(name + " Landed on their own space!");
				card.toString();
			}
		}else if(boardSpot=="RR"){
			System.out.println("Landed on a RailRoad! -- INFO BELOW --");
			int cardNumber = this.currentPosition+1;
			RailRoadCard card = Utilities.findRailCard(cardNumber, banker);
			playRecap+="Landed on "+card.name +"\n";
			System.out.println(card);
			if(card.owner == null){
				if(this.computer == true){
					if((this.money-card.price)>100){
						banker.sellRailRoad(this, card);
						System.out.println("Computer "+name+" has chosen to purchase " + card.name);
						playRecap+= "Computer "+name+" purchased " + card.name+"\n";
					} else {
						System.out.println("Computer "+name+" has chosen not to buy " + card.name);
					}
				} else {
					System.out.println("Would you like to buy this property? (1)YES (2)NO");
					int choice = Utilities.yesOrNO();
					if(choice == 1){
						banker.sellRailRoad(this, card);
						System.out.println("Press anything to proceed...");
						Utilities.input.nextLine();
					}
				}
			} else if (card.owner != this){
				System.out.println("Property owned by " + card.owner.name);
				System.out.println(name + " must pay rent!");
				card.payRent(this);
			}else if(card.owner == this){
				System.out.println(name + " Landed on their own space!");
				card.toString();
			}
		}else{
			int cardNumber = Integer.parseInt(boardSpot);
			PropertyCard card = Utilities.findCard(cardNumber, banker);
			playRecap+="Landed on "+card.name +"\n";
			System.out.println(name + " landed on a property: -- INFO BELOW --");
			System.out.println(card);

			if(card.owner == null){
				if(this.computer == true){
					if((this.money-card.price) > 100){
						banker.sellProperty(this, card);
						System.out.println("Computer "+name+" has chosen to purchase " + card.name);
						playRecap+= "Computer "+name+" purchased " + card.name+"\n";
					} else {
						System.out.println("Computer "+name+" has chosen not to buy " + card.name);
					}
				}else {
					System.out.println("Would you like to buy this property? (1)YES (2)NO");
					int choice = Utilities.yesOrNO();
					if(choice == 1){
						banker.sellProperty(this, card);
						System.out.println("Press anything to proceed...");
						Utilities.input.nextLine();
					}
				}
			} else if(card.owner != this){
				System.out.println("Property owned by " + card.owner.name);
				System.out.println(name + " must pay rent!");
				card.payRent(this);
			}else if(card.owner == this){
				System.out.println(name + " Landed on their own space!");
				playRecap+= name+" landed on their own property!\n";
				card.toString();
			}
		}
	}
	//AI decision making

	public ArrayList<Integer> cardsToLook(){
		ArrayList<Integer> stuffToBuy = new ArrayList<Integer>();
		for (PropertyCard card : this.properties) {
			for (int x = 0; x < Banker.propertylinks.length; x++) {
				for (int y = 0; y < Banker.propertylinks[x].length; y++) {
					if(card.position == Banker.propertylinks[x][y]){
						for (int i = 0; i < Banker.propertylinks[x].length; i++) {
							stuffToBuy.add(Banker.propertylinks[x][i]);
						}
						break;
					}
				}
			}
		}
		return stuffToBuy;
	}

	public void compProps(Player player){
		ArrayList<Integer> stuffToBuy = cardsToLook();
		ArrayList<Integer> otherPlayerToBuy = player.cardsToLook();

		for (PropertyCard card : player.properties) {
			if(stuffToBuy.contains(card.position) && money > card.price+200 && card.houses == 0){
				if(player.computer){
					System.out.println(name + " bought " + card.name + " from player " + player.name);
					playRecap+= name+" offers $190 for " + card.name + " from player " + player.name+"\n";
					player.playRecap+=player.name+ " sold " +card.name + " to "+ name+"\n";
					playRecap+=player.name +" sold " + card.name + " to "+ name + "\n";

					money-=card.price+50;
					player.money+=card.price+50;

					card.owner = this;

					properties.add(card);
					player.properties.remove(card);
					break;
				}else{
					System.out.println(name + " offers you $" + card.price + " for your property " + card.name + " YES(1) or NO(2)");
					int decision = Utilities.yesOrNO();

					if(decision == 1){
						player.money+=card.price+50;
						money-=card.price+50;
						card.owner = this;

						properties.add(card);
						player.properties.remove(card);
						System.out.println("You have sold this property for $"+(card.price+50)+"!");
						player.playRecap+=player.name +" sold " + card.name + " to "+ name + "\n";
						playRecap+=player.name +" sold " + card.name + " to "+ name + "\n";
						break;	
					}else{
						System.out.println("You have chosen not to sell this property");
					}
				}
			}
		}
	}
}