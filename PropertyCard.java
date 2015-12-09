/*

Creator: Travis Delly
Version: 1.0
Purpose: To play Monopoly

*/


import java.util.*;

public class PropertyCard extends Buyable{
	public int houseCosts;
	public int mortgageValue;
	public int position;
	//New will add later

	public int houses;

	public int[] rents = new int[6];

	public void payRent(Player fromPlayer){
		int payment = rents[houses];
		System.out.println(fromPlayer.name + " paid " + payment + " to " + owner.name);
		fromPlayer.playRecap += fromPlayer.name + " paid " + payment + " to " + owner.name +"\n";

		owner.playRecap += "Got paid " + payment + " from " + fromPlayer.name + " for overnight stay at " + name + "\n";

		owner.money += payment;
		fromPlayer.money -= payment;
	}

	public PropertyCard(String[] variables){
		this.name = variables[0];
		this.position = Integer.parseInt(variables[1]);
		this.price = Integer.parseInt(variables[2]);
		this.rents[0] = Integer.parseInt(variables[3]);
		this.mortgageValue = Integer.parseInt(variables[4]);
		this.houseCosts = Integer.parseInt(variables[5]);
		this.group = Integer.parseInt(variables[6]);
		this.owner = null;

		this.rents[1] = Integer.parseInt(variables[7]);
		this.rents[2] = Integer.parseInt(variables[8]);
		this.rents[3] = Integer.parseInt(variables[9]);
		this.rents[4] = Integer.parseInt(variables[10]);
		this.rents[5] = Integer.parseInt(variables[11]);
	}

	public String toString(){
		int house = houses;
		int hotel = 0;
		if(houses == 5){
			hotel = 1;
			house = 0;
		}
		String ownedBy = "Banker";
		if(this.owner != null)
			ownedBy = owner.name;

		return "Card Name: " + name + "\nOwned by: " + ownedBy + "\nPrice: " + price + "\nGroup: "+ getColor() + "\nRent: " + this.rents[0] + "\nRent with one house 1: " + this.rents[1] + 
				"\nRent with two houses: " + this.rents[2] + "\nRent with three houses: " + this.rents[3] + "\nRent with four houses: " +
				this.rents[4] + "\nRent with hotel: " + this.rents[5] + "\nHouse costs: " + houseCosts + "\nMortgage Value: " + mortgageValue +
				"\nHouses on property: " + house + "\nHotels on property " + hotel + "\nProperty Position on Board: " + (position-1) + "\n--END OF PROPERTY INFO--";
	}

}