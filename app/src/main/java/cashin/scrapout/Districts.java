package cashin.scrapout;

import java.util.ArrayList;

/**
 * Created by Sammy on 08/03/16.
 */
public class Districts {

    private String id;
    private String type;
    private ArrayList<District> data;


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

    public ArrayList<District> getDistrictsInfo() {
        return data;
    }

    public void setDistrictsInfo(ArrayList<District> data) {
        this.data = data;
    }
}
