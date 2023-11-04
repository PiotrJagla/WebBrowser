package org.example.browser.Style;


import org.example.browser.CSS.Values.Value;

import java.util.HashMap;
import java.util.Map;

public class PropertyMap{
    private Map<String, Value> map = new HashMap<>();

    public Map<String, Value> getMap() {
        return map;
    }

    public void setMap(Map<String, Value> map) {
        this.map = map;
    }
}
