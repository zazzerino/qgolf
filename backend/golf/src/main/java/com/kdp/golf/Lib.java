package com.kdp.golf;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Lib {

    public record Pair<A, B>(A a, B b) {
        public static <A, B> Pair<A, B> of(A a, B b) {
            return new Pair<>(a, b);
        }
    }

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
        var pickedItems = pickItems(items, indices);
        return allEqual(pickedItems);
    }

    public static <K, V> Map<K, V> removeKeys(Map<K, V> map, Collection<K> keys) {
        return map.entrySet().stream()
                .filter(e -> !keys.contains(e.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));
    }

    public static <K, V> Map<K, V> removeKeysDestructive(Map<K, V> map, Collection<K> keys) {
        for (var k : keys) {
            map.remove(k);
        }

        return map;
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

    public static <K, V> ImmutableMap<K, V> updateMap(Map<K, V> map, Map<K, V> changes) {
        return ImmutableMap.<K, V>builder()
                .putAll(Maps.difference(map, changes).entriesOnlyOnLeft())
                .putAll(changes)
                .build();
    }

    public static <T> Stream<T> cycle(Supplier<Stream<T>> stream) {
        return Stream.generate(stream)
                .flatMap(s -> s);
    }
}
