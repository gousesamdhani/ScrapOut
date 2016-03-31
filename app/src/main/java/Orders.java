import java.util.ArrayList;

import cashin.scrapout.Order;

/**
 * Created by Sammy on 09/03/16.
 * Commiting the change
 */
public class Orders {

    private String id;
    private String type;
    private ArrayList<Order> data;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Order> getOrders() {
        return data;
    }

    public void setOrders(ArrayList<Order> data) {
        this.data = data;
    }
}
