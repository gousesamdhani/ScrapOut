package cashin.scrapout;

/**
 * Created by Sammy on 09/03/16.
 */
public class Order {

    private String id;
    private String order_number;
    private String user_id;
    private String state_id;
    private String district_id;
    private String area_id;
    private String deal_status;
    private String row_status;
    private String address;
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getDeal_status() {
        return deal_status;
    }

    public void setDeal_status(String deal_status) {
        this.deal_status = deal_status;
    }

    public String getRow_status() {
        return row_status;
    }

    public void setRow_status(String row_status) {
        this.row_status = row_status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", order_number='" + order_number + '\'' +
                ", user_id='" + user_id + '\'' +
                ", state_id='" + state_id + '\'' +
                ", district_id='" + district_id + '\'' +
                ", area_id='" + area_id + '\'' +
                ", deal_status='" + deal_status + '\'' +
                ", row_status='" + row_status + '\'' +
                '}';
    }

}
