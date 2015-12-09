/*

Creator: Travis Delly
Version: 1.0
Purpose: To play Monopoly

*/


import java.util.*;

public class UtilityCard extends Buyable{
	public int mortgageValue;
	public int position;

	public UtilityCard(String name, int pos, int cost, int mortgage){
		//name/pos/cost/mortage
		owner = null;
		this.position = pos;
		this.name = name;
		this.price = cost;
		this.mortgageValue = mortgage;
	}
	public void payRent(Player fromPlayer){
		int multiDice = 4;
		if(owner.utilities.size() == 2)
			multiDice = 10;
		int payment = (Roll.dice1+Roll.dice2)*multiDice;
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

		return "Property name: " + name + "\nOwned by: " +ownedBy+ "\nPrice: " + price + "\nRent is 4x dice roll, if you own both railroads, rent is 10x dice roll" + 
		"\nMortgage value: " + mortgageValue + "Utility Position on Board: " + (position-1) + "\n--END OF UTILITY INFO--";
	}
}