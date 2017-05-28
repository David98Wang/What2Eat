package eng.waterloo.what2eat.Database;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by whcda on 5/30/2017.
 */

public class Group {
    public String ID;
    public int size;
    public LatLng location;
    public Map<String,String> info = new HashMap<String,String>();
    public HashMap<String,String> votes= new LinkedHashMap<String,String>();
    public Group(String ID, int size, LatLng location){
        this.ID = ID;
        this.size = size;
        this.location = location;
    }

    public String getID() {
        return ID;
    }

    public int getSize() {
        return size;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
