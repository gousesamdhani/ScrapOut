package cashin.scrapout;

import java.util.ArrayList;

/**
 * Created by Sammy on 08/03/16.
 */
public class States {

    private String type;
    private String id;
    private ArrayList<StateInfo> data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<StateInfo> getStatesInfo() {
        return this.data;
    }

    public void setStatesInfo(ArrayList<StateInfo> statesInfo) {
        this.data = statesInfo;
    }
}

