package com.emanuel.fridolin.util;

import com.sun.tools.javac.util.ArrayUtils;

import java.util.*;

public class RandomStringPool {

    private final Map<String, String[]> valueMapping = new HashMap<>();
    private final Random random = new Random();

    public void put(String key, String... values){
        if (valueMapping.containsKey(key)){
            String[] oldMapping = valueMapping.remove(key);

            List<String> list =  new ArrayList<>(Arrays.asList(oldMapping));
            list.addAll(Arrays.asList(values));
            values = new HashSet<>(list).toArray(new String[0]);
        }

        valueMapping.put(key, values);
    }

    public String randomForKey(String key){
        String[] choices = valueMapping.getOrDefault(key, new String[]{""});
        int index = random.nextInt(choices.length);
        return choices[index];
    }




}
