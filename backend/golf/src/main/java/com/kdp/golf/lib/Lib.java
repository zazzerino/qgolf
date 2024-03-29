package com.kdp.golf.lib;

import java.util.*;

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

    /**
     * Takes the first element of `lhm` and moves it to the end.
     */
    public static <K, V> void cycleLinkedHashMap(LinkedHashMap<K, V> lhm) {
        if (lhm.isEmpty()) return;

        var firstEntry = lhm.entrySet().stream().findFirst().orElseThrow();
        var key = firstEntry.getKey();
        var val = firstEntry.getValue();

        lhm.remove(key);
        lhm.put(key, val);
    }

    /**
     * Returns an Optional from List::indexOf.
     */
    public static <T> Optional<Integer> findIndex(List<T> list, T elem) {
        var index = list.indexOf(elem);
        return index == -1
                ? Optional.empty()
                : Optional.of(index);
    }

//    public static <T> boolean indicesEqual(List<T> items, List<Integer> indices) {
//        var pickedItems = pickItems(items, indices);
//        return allEqual(pickedItems);
//    }
//
//    public static <K, V> Map<K, V> removeKeys(Map<K, V> map, Collection<K> keys) {
//        return map.entrySet().stream()
//                .filter(e -> !keys.contains(e.getKey()))
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue));
//    }
//
//    public static <K, V> Map<K, V> removeKeysDestructive(Map<K, V> map, Collection<K> keys) {
//        for (var k : keys) {
//            map.remove(k);
//        }
//
//        return map;
//    }

//    public static <K, V> Map<K, V> updateMap(Map<K, V> map, Map<K, V> changes) {
//        return ImmutableMap.<K, V>builder()
//                .putAll(Maps.difference(map, changes).entriesOnlyOnLeft())
//                .putAll(changes)
//                .build();
//    }

//    public static <T> List<T> listWithElem(List<T> list, T elem) {
//        var copy = new ArrayList<>(list);
//        copy.add(elem);
//        return copy;
//    }
//
//    public static <T> Set<T> setWithElem(Set<T> set, T elem) {
//        var copy = new HashSet<>(set);
//        copy.add(elem);
//        return copy;
//    }
}
