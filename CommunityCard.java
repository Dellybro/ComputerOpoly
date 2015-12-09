/*

Creator: Travis Delly
Version: 1.0
Purpose: To play Monopoly

*/

import java.util.*;

public class CommunityCard{
	public String desc;

	private String internal;

	public int money;
	public int getOutOfJailCard;

	public CommunityCard(String desc, int money, String internal){
		this.desc = desc;
		this.money = money;
		this.internal = internal;
	}
	public void useCard(Player player, Banker banker){
		player.playRecap+="Community card: " + desc + "\n";
		System.out.println(desc);

		boolean addBacktoDeck = true;

		if(internal.equals("j")){
			player.currentPosition = Board.JAIL;
			player.inJail=true;
		}else if(internal.equals("g")){
			player.currentPosition = Board.GO;
			Banker.payPlayerStart(player);
		}else if(internal.equals("f")){
			player.getOutOfJailCards.add(this);
			addBacktoDeck=false;
		}else if (internal.equals("h")){
			player.money-= (40*player.houses+110*player.hotels);
		}else {
			if(money < 0){
				banker.payBanker(player, money);
			}else{
				player.money+=money;
			}
		}

		if(addBacktoDeck){
			banker.communityCards.add(this);
		}
	}

}