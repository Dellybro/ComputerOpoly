abstract class Buyable{
	public String name;
	public int price;
	public Player owner;
	public int group;

	public String getColor(){
		switch(group){
			case 1:
				return "Magenta";
			case 2:
				return "Teal";
			case 3:
				return "Purple";
			case 4:
				return "Orange";
			case 5:
				return "Red";
			case 6:
				return "Green";
			case 7:
				return "Yellow";
			case 8:
				return "Blue";
			default:
				return "Utility or RailRoad";
		}
	}
}