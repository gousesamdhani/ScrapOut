package cashin.scrapout;

/**
 * Created by palisn on 2/22/16.
 */
public class OrderItem {
    String name;
    int imageName;
    boolean isSelected;

    public OrderItem(){

    }
    public OrderItem(String name, int imageName, boolean isSelected){
        this.name = name;
        this.imageName = imageName;
        this.isSelected = isSelected;
    }
}
