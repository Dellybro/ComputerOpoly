/*

Creator: Travis Delly
Version: 1.0
Purpose: To play Monopoly

*/

import java.util.*;
import java.io.*;

public class Banker{
	public int houses;
	public int hotels;
	public static int[][] propertylinks = {{2,4},{7,9,10},{12,14,15},{17,19,20},{22,24,25},{27,28,30},{32,33,35},{38,40}};

	//Card Holders
	public ArrayList<UtilityCard> utilityCards = new ArrayList<UtilityCard>();
	public ArrayList<RailRoadCard> railRoadCards = new ArrayList<RailRoadCard>();
	public ArrayList<CommunityCard> communityCards = new ArrayList<CommunityCard>();
	public ArrayList<ChanceCard> chanceCards = new ArrayList<ChanceCard>();
	public ArrayList<PropertyCard> propertyCards = new ArrayList<PropertyCard>();

	public static int middleMoney;
	//Class Instantiators
	public Banker(){
		setupCards();
		houses = 32;
		hotels = 12;
		middleMoney = 0;
	}

	public void payBanker(Player player, int payment){
		player.playRecap+= player.name + " paid "+payment+ " to banker. Middle Money increases!" +"\n";
		player.money-=Math.abs(payment);
		middleMoney+= Math.abs(payment);
	}

	public String toString(){
		String returnString = new String();
		for(PropertyCard card : propertyCards){
			String owner = (card.owner == null) ? "Banker" : card.owner.name;
			returnString += card.name + " owned by: " + owner + " | " + card.getColor() + " | " + " | Houses on property: " + card.houses +"\n";
		}
		returnString+= "(5houses = hotel)\n";
		return returnString + "Houses left: " + houses + "\nHotels left: " + hotels + "\nMoney in middle " + middleMoney+"\n";
	}
	public void payMiddleMoney(Player player){
		player.playRecap+= player + " got paid " + middleMoney + " from the middle money!";	
		player.money +=middleMoney;
		middleMoney =0;
	}

	public void dispenseCard(int cardType, Player player){
		//If card type is one dispense Chance Card
		//If card type is two dispense Community Card
		if(cardType == 1){
			ChanceCard card = chanceCards.get(0);
			chanceCards.remove(0);
			card.useCard(player, this);
		}else{
			CommunityCard card = communityCards.get(0);
			communityCards.remove(0);
			card.useCard(player, this);
		}
	}
	public void sellHouse(Player player){
		/*
			- Make List of all card positions player has
			- Check which positions are grouped in Propertylinks
			- If there is no break applied, will add properties to Allowed houses
			- If there is no houses allowed go home else proceed
			- Get the names of the properties and the cards in a new Array.
			- Use inputNumber from Utilities to chose property.
			- Get card position from allowedHouses array using choice, than turn choice into that number
			- Use choice to find the card in players deck
			- Break outerloop once found and dealt with.
		*/

		ArrayList<Integer> playerCardPositions = new ArrayList<Integer>();
		for (PropertyCard card : player.properties ) {
			playerCardPositions.add(card.position);
		}
		ArrayList<Integer> allowedHouses = new ArrayList<Integer>();

		for (int x = 0; x < propertylinks.length; x++) {
			for (int y = 0; y < propertylinks[x].length; y++) {
				if(!(playerCardPositions.contains(propertylinks[x][y]))){
					break;
				}else{
					if(y+1==propertylinks[x].length){
						for (int i = 0; i < propertylinks[x].length; i++) {
							allowedHouses.add(propertylinks[x][i]);
						}
					}
				}
			}
		}
		if(allowedHouses.size() == 0){
			if(!(player.computer))
				System.out.println("None of the properties you have contain all group members!\n");
		}else{
			for (int x = 0; x < allowedHouses.size(); x++) {
				for (PropertyCard card : player.properties) {
					if(card.position == allowedHouses.get(x)){
						if(player.computer){
							while(true && card.houses<5){
								if(player.money>card.houseCosts){
									if(!(checkHouses(player,card)))
										break;
								}else
									break;
							}
							break;
						}else{
							System.out.println(x+ ") Add house on " + card.name);
							break;
						}

					}
				}
			}

			if(!(player.computer)){
				System.out.println("Type " + allowedHouses.size() + " to go back.");
				int choice = Utilities.inputNumber(allowedHouses.size());

				if(choice == allowedHouses.size()){
					System.out.println("Going back...\n");
				}else{

					choice = allowedHouses.get(choice);
					System.out.println("How many houses? (Max 5)");
					int times = Utilities.inputNumber(5);


					outerloop:
					for (PropertyCard card : player.properties ) {
						if(player.money > card.houseCosts*times){
							if(card.position == choice){
								for (int x = 0; x<times; x++) {
									if(!(checkHouses(player, card)))
										break outerloop;
								}
								break outerloop;
							}
						}else{
							System.out.println("You are to poor to buy a house for this property\n");
							break outerloop;
						}
					}
				}
			}
		}
	}

	public boolean checkHouses(Player player, PropertyCard card){

		if(card.houses == 4){
			if(hotels > 0){
				System.out.println("You have bought a hotel for " + card.name +"!\n");
				card.houses+=1;
				houses += 4;
				hotels-=1;
				player.money -= card.houseCosts;
				player.houses-=4;
				player.hotels+=1;
				return true;
			}else{
				System.out.println("Banker is out of hotels!");
				return false;
			}
		}else if(card.houses == 5){
			System.out.println("You already have a hotel on this property!\n");
			return false;
		} else {
			if(houses>0){
				player.money -= card.houseCosts;
				card.houses+=1;
				houses-=1;
				player.houses+=1;
				System.out.println("House bought for card " + card.name + "! There is now "+card.houses+" on this property!\n");
				player.playRecap+="House bought for card " + card.name + "! There is now "+card.houses+" on this property!\n";
				return true;
			}else{
				System.out.println("Banker is out of houses!");
				return false;
			}
		}

	}

	public boolean sellProperty(Player player, PropertyCard card){
		/*
			- Make sure player has enough money to buy property from bank
			- Sell property, subtract money, add card to players deck
			- Return true or false
			- Same applies to railroads and Utility methods.
		*/
		if(player.money > card.price){
			card.owner = player;
			System.out.println("Buying Property From Banker");
			player.properties.add(card);
			player.money -= card.price;
			return true;
		} else {
			System.out.println("Banker rejects you small offer. You are to poor to buy this property!");
			return false;
		}
	}
	public boolean sellRailRoad(Player player, RailRoadCard card){
		if(player.money > card.price){
			card.owner = player;
			System.out.println("Buying Railroad From Banker");
			player.railroads.add(card);
			player.money -= card.price;
			return true;
		} else {
			System.out.println("Banker rejects you small offer. You are to poor to buy this rail road!");
			return false;
		}
	}
	public boolean sellUtility(Player player, UtilityCard card){
		if(player.money > card.price){
			card.owner = player;
			System.out.println("Buying Utility From Banker");
			player.utilities.add(card);
			player.money -= card.price;
			return true;
		} else {
			System.out.println("Banker rejects you small offer. You are to poor to buy this company!!");
			return false;
		}
	}
	/*Static - methods*/
	public static void payPlayerStart(Player playertoPlay){
		System.out.println("Banker dispensing $200 for passing go");
		playertoPlay.playRecap+="Banker paid $200 for passing go\n";
		playertoPlay.money += 200;
	}





	//SETUP CARDS ETC V

	private void setupCards(){
		/*
		*	Setup Cards and stuff
		*/
		final String FILETOPARSE = "propertys.csv";
		BufferedReader fileReader = null;
		final String DELIMITER = ",";
		try{
			String line = new String();
			fileReader = new BufferedReader(new FileReader(FILETOPARSE));
			while((line = fileReader.readLine()) != null){
				String[] tokens = line.split(DELIMITER);

				if(tokens[tokens.length-1].equals("u")){
					UtilityCard utility = new UtilityCard(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
					utilityCards.add(utility);
				}else if(tokens[tokens.length-1].equals("r")){
					RailRoadCard railroad = new RailRoadCard(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
					railRoadCards.add(railroad);
				}else if(tokens[tokens.length-1].equals("p")) {
					PropertyCard property = new PropertyCard(tokens);
					propertyCards.add(property);
				}else if(tokens[tokens.length-1].equals("co")) {
					//String desc, int money, String internal
					CommunityCard comm = new CommunityCard(tokens[0], Integer.parseInt(tokens[1]), tokens[2]);
					communityCards.add(comm);
				}else if(tokens[tokens.length-1].equals("ch")){
					ChanceCard chance = new ChanceCard(tokens[0], Integer.parseInt(tokens[1]), tokens[2]);
					chanceCards.add(chance);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				fileReader.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}

		long seed = System.nanoTime();
		Collections.shuffle(chanceCards, new Random(seed));
		Collections.shuffle(chanceCards, new Random(seed));
		Collections.shuffle(communityCards, new Random(seed));
		Collections.shuffle(communityCards, new Random(seed));

		System.out.println("Scrambling Chance and Community Cards...\nChance Cards in deck: "+ chanceCards.size() + "\nCommunity cards in deck " + communityCards.size()+"\n");
	}
}