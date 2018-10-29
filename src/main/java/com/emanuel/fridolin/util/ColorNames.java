package com.emanuel.fridolin.util;

import com.emanuel.fridolin.exception.NoSuchColorException;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ColorNames {

    private static final Map<String, Color> colorByName = generateColorMap();

    private ColorNames() {
    }

    private static Map<String, Color> generateColorMap() {
        HashMap<String, Color> colorByName = new HashMap<>();

        colorByName.put("white", Color.white);
        colorByName.put("lightGray", Color.lightGray);
        colorByName.put("gray", Color.gray);
        colorByName.put("darkGray", Color.darkGray);
        colorByName.put("black", Color.black);
        colorByName.put("red", Color.red);
        colorByName.put("pink", Color.pink);
        colorByName.put("orange", Color.orange);
        colorByName.put("yellow", Color.yellow);
        colorByName.put("green", Color.green);
        colorByName.put("magenta", Color.magenta);
        colorByName.put("cyan", Color.cyan);
        colorByName.put("blue", Color.blue);

        return Collections.unmodifiableMap(colorByName);
    }


    public static Color fromString(String name) throws NoSuchColorException {
        Color c = colorByName.get(name);
        if (c == null) {
            throw new NoSuchColorException(name);
        }
        return c;
    }

    public static String fromColor(Color color) {
        for (Map.Entry<String, Color> entry : colorByName.entrySet()) {
            if (entry.getValue().equals(color)) {
                return entry.getKey();
            }
        }

        return null;
    }

}
