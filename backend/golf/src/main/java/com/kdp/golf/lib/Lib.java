package com.kdp.golf.lib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Lib {

    public static <T> List<T> pickItems(List<T> list, List<Integer> indices) {
        var items = new ArrayList<T>();

        for (var i : indices) {
            items.add(list.get(i));
        }

        return items;
    }

    public static <T> boolean allEqual(List<T> items) {
        return items.stream().distinct().count() == 1;
    }

    public static <T> boolean indicesEqual(List<T> items, List<Integer> indices) {
        var picked = pickItems(items, indices);
        return allEqual(picked);
    }

    public static <T, U> Map<T,U> removeKeys(Map<T, U> map, Collection<T> keys) {
        for (var k : keys) {
            map.remove(k);
        }

        return map;
    }
}
