package dk.sdu.se4.group1.CommonEcs;


public class Item {
    Entity entityID;
    String name;
    int price;
    int level;
    FarmingType type;

    //
    public Item(Entity entityID,String name, int price, int level, FarmingType type){
        this.entityID= entityID;
        this.name = name;
        this.price = price;
        this.level = level;
        this.type = type;

    }
    // method to get all in a string
    public String GetAllnfo(){

        return "EnetityID"+entityID.getID()+ "Name:"+name+" Price:"+price+" Level:"+level;
    }

    // Get Name Of Item
    public String getName(){

        return name;
    }

    // Get Price for item
    public int getPrice(){

        return price;

    }
    // Get Item Of item
    public int getLevel(){

        return level;

    }

    public String getEntiryID(){
        return entityID.getID();
    }

    // FarmingType of the item
    public String getType(){

        return type.toString();

    }

    public enum FarmingType{
        Dirt,
        Planer,
        shovel
    }





}