/*

Creator: Travis Delly
Version: 1.0
Purpose: To play Monopoly

*/


import java.util.*;

public class RailRoadCard extends Buyable{
	public int mortgageValue;
	public int position;
	public int[] rents = {50, 100, 150, 200};

	public RailRoadCard(String name, int pos, int cost, int mortgage){
		owner = null;
		this.position = pos;
		this.name = name;
		this.price = cost;
		this.mortgageValue = mortgage;
	}
	public void payRent(Player fromPlayer){
		int payment = rents[owner.railroads.size()-1];
		System.out.println(fromPlayer.name + " paid " + payment + " to " + owner.name);
		
		fromPlayer.playRecap += fromPlayer.name + " paid " + payment + " to " + owner.name +"\n";

		owner.playRecap += "Got paid " + payment + " from " + fromPlayer.name + " for overnight stay at " + name + "\n";

		owner.money += payment;
		fromPlayer.money -= payment;
	}
	public String toString(){
		String ownedBy = "Banker";
		if(this.owner != null)
			ownedBy = owner.name;

		return "Property Name: " + name + "\nOwned by: " +ownedBy+ "\nPrice: " + price + "\nRent with one: " + rents[0] +
		"\nRent with two: " + rents[1] + "\nRent with three: " + rents[2] + "\nRent with four: " + rents[3] + 
		"\nMortgageValue " + mortgageValue + "\nRailRaod Position on Board: " + (position-1) + "\n--END OF RAILROAD INFO--";
	}

}