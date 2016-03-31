package cashin.scrapout;

import java.util.ArrayList;

/**
 * Created by Sammy on 12/03/16.
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

    public ArrayList<Order> getActiveOrders() {
        return getOrdersWithStatus(Constants.DEAL_PENDING);
    }

    public ArrayList<Order> getCompletedOrders() {
        return getOrdersWithStatus(Constants.DEAL_DONE);
    }

    public ArrayList<Order> getCancelledOrders() {
        return getOrdersWithStatus(Constants.DEAL_CANCELLED);
    }

    private ArrayList<Order> getOrdersWithStatus(int status) {
        Order order;
        int deal_status;
        ArrayList<Order> orders = new ArrayList<Order>();
        for(int i=0; i < data.size(); i++) {
            order = data.get(i);
            deal_status = Integer.parseInt(order.getDeal_status());
            if(deal_status == status) {
                orders.add(order);
            }
        }
        return orders;
    }

    public void setOrders(ArrayList<Order> data) {
        this.data = data;
    }
}
