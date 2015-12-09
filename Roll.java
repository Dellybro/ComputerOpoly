import java.util.*;

public class Roll{

	public static int dice1;
	public static int dice2;

	public static int makeRandom(){
		return Utilities.ran.nextInt(6) + 1;
	}

	public static void rollBothDice(){
		rollDiceOne();
		rollDiceTwo();
		System.out.println("Rolled a " + (dice1+dice2) + " Dice: " + dice1 + ", " + dice2);
	}

	public static int rollDiceOne(){
		dice1 = makeRandom();
		return dice1;
	}
	public static int rollDiceTwo(){
		dice2 = makeRandom();
		return dice2;
	}
}