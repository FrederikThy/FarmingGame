package dk.sdu.se4.group1.CommonEcs;

public class InviItme {
    int count;
    Item item;

    public InviItme(Item x_item){
        this.count = 1;
        this.item = x_item;
    }
    public InviItme(Item x_item, int amount) {
        this.count = amount;
        this.item = x_item;
    }

    public Item getItem() {
        return item;
    }
    public int getCount(){
        return count;
    }
    public void addCount(int amount) {
        this.count += amount;
    }
}
