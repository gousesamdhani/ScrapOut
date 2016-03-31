package cashin.scrapout;

/**
 * Created by Sammy on 08/03/16.
 */
public class StateInfo {

    private String id;
    private String state_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStateName() {
        return state_name;
    }

    public void setStateName(String stateName) {
        this.state_name = stateName;
    }

    @Override
    public String toString(){
        return "{\nid : "+this.getId()+"\nstate_name : "+this.getStateName()+"\n}";
    }
}
