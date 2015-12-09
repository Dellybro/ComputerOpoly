/*
   Created By Travis Delly

   This class represents the board of the game. an Array filled with 40 slots.
   There are also "global variables" for the board 0, 10, 20, 30 positions. (0 being 40).
*/


import java.util.*;

class Board{

	public static String[] board = {"GO", "2", "COMM", "4", "TAX",  "RR", "7", "CHAN", "9", "10", "JAIL",
   		"12", "UTIL", "14", "15", "RR", "17", "COMM", "19", "20",
   		"FREE", "22", "CHAN", "24", "25", "RR", "27", "28", "UTIL", "30", 
   		"GOTO", "32", "33", "COMM", "35", "RR", "CHAN", "38", "TAX", "40"};
   	final public static int GO = 0;
   	final public static int JAIL = 10;
   	final public static int FREE = 20;
   	final public static int GOTO = 30;
      final public static int ILLINOIS = 24;
      final public static int READINGRAILROAD = 5;
      final public static int WATERWORKS = 28;
      final public static int ELECTRIC = 12;
      final public static int PRAILROAD = 15;
      final public static int BORAILROAD = 25;
      final public static int SLRAILROAD = 35;
      final public static int BOARDWALK = 39;
      final public static int STCHARLES = 11;

	public Board(){

	}
}