package cashin.scrapout;

/**
 * Created by Sammy on 08/03/16.
 */
public class District {

    private String id;
    private String state_id;
    private String district_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    @Override
    public String toString(){
        return "{\nid : "+this.getId()+"\nstate_id : "+this.getState_id()+"\ndistrict_name : "+this.getDistrict_name()+"\n}";
    }

}
