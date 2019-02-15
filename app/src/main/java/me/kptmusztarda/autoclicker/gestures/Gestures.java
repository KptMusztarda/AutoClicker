package me.kptmusztarda.autoclicker.gestures;

import java.util.ArrayList;
import java.util.List;

public enum Gestures {

    RANDOM_CIRCLE("Random Circle", 0),
    SINGLE_POINT("Single Point", 1);

    private String name;
    public int type;

    Gestures(String name, int type) {
        this.name = name;
        this.type = type;
    }

    static public List<String> getNamesList() {
        List<String> list = new ArrayList<>(Gestures.values().length);
        for(Gestures g : Gestures.values()) {
            list.add(g.name);
        }
        return list;
    }

}
