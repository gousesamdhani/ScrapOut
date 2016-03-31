package cashin.scrapout;

import java.util.ArrayList;

/**
 * Created by Sammy on 08/03/16.
 */
public class Areas {

    private String id;
    private String type;
    private ArrayList<AreaInfo> data;


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

    public ArrayList<AreaInfo> getAreas() {
        return data;
    }

    public void setAreas(ArrayList<AreaInfo> data) {
        this.data = data;
    }
}
