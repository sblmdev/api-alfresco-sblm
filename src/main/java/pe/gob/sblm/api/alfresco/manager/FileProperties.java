package pe.gob.sblm.api.alfresco.manager;

/**
 * Created by pjudicial on 08/05/2015.
 */

import java.util.HashMap;
import java.util.Map;

public class FileProperties {


    Map<String, Object> properties = new HashMap<String, Object>();

    public void addProp(String name, Object value){
        properties.put(name, value);
    }

    public Map<String, Object> getProperties(){
        return properties;
    }

}
