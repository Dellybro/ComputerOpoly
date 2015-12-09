/*

Creator: Travis Delly
Version: 1.0
Purpose: To play Monopoly

*/

import java.util.*;

public class ChanceCard{
	public String desc;

	public int money;
	public String internal;

	public ChanceCard(String desc, int money, String internal){
		this.desc = desc;
		this.internal = internal;
		this.money = money;
	}
	public void useCard(Player player, Banker banker){
		System.out.println(desc);
		player.playRecap+="Chance card: " + desc + "\n";

		boolean addBacktoDeck = true;

		if(internal.equals("j")){//Go to jail
			player.currentPosition = Board.JAIL;
			player.inJail=true;
		}else if(internal.equals("g")){//Go to go
			player.currentPosition = Board.GO;
			Banker.payPlayerStart(player);
			System.out.println("\nNew position is 0");
		}else if(internal.equals("f")){//Getoutofjailfree
			player.getOutOfJailCards.add(this);
			addBacktoDeck=false;
		}else if (internal.equals("i")){//Go to illinois
			player.currentPosition = Board.ILLINOIS;
			player.getSpaceInfo(banker);
		}else if (internal.equals("s")){//Go to st charles
			player.currentPosition = Board.STCHARLES;
			player.getSpaceInfo(banker);
		}else if(internal.equals("r")){///Go to neareest railraod
			int finalR = Math.min(Math.min(Math.abs(player.currentPosition-=Board.READINGRAILROAD), Math.abs(player.currentPosition-=Board.PRAILROAD)), Math.min(Math.abs(player.currentPosition-=Board.BORAILROAD), Math.abs(player.currentPosition-=Board.SLRAILROAD)));
			player.currentPosition = finalR;
			player.getSpaceInfo(banker);
		}else if(internal.equals("rr")){//Go to readingrailroad
			player.currentPosition = Board.READINGRAILROAD;
			player.getSpaceInfo(banker);
		}else if(internal.equals("b")){//Go to boardwalk
			player.currentPosition = Board.BOARDWALK;
			player.getSpaceInfo(banker);
		}else if(internal.equals("u")){//Nearest Utitlity
			int finalU = Math.min(Math.abs(player.currentPosition-=Board.ELECTRIC), Math.abs(player.currentPosition-=Board.WATERWORKS));
			player.currentPosition=finalU;
			player.getSpaceInfo(banker);
		}else if(internal.equals("b3")){//Back 3 spaces
			player.currentPosition -= 3;
			player.getSpaceInfo(banker);
		}else {
			if(money < 0){
				banker.payBanker(player, money);
			}else{
				player.money+=money;
			}
		}

		if(addBacktoDeck){
			banker.chanceCards.add(this);
		}
	}

}