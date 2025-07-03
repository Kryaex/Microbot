package net.runelite.client.plugins.microbot.util.inventory;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface ItemProvider {
    int getCapacity();

    Stream<Rs2ItemModel> getAll();
    default Stream<Rs2ItemModel> getAll(Predicate<Rs2ItemModel> filter) {
        return getAll().filter(filter);
    }
    default Stream<Rs2ItemModel> getAll(boolean exact, String ... names) {
        return getAll(item -> matches(item, exact, toLowerCase(names)));
    }
    default Stream<Rs2ItemModel> getAll(String ... names) {
        return getAll(false, names);
    }
    default Stream<Rs2ItemModel> getAll(int ... ids) {
        return getAll(item -> matches(item, ids));
    }

    default Rs2ItemModel get() {
        return getAll().findFirst().orElse(null);
    }
    default Rs2ItemModel get(Predicate<Rs2ItemModel> filter) {
        return getAll(filter).findFirst().orElse(null);
    }
    default Rs2ItemModel get(boolean exact, String ... names) {
        return getAll(exact, names).findFirst().orElse(null);
    }
    default Rs2ItemModel get(String ... names) {
        return getAll(names).findFirst().orElse(null);
    }
    default Rs2ItemModel get(int ... ids) {
        return getAll(ids).findFirst().orElse(null);
    }

    default boolean isEmpty() {
        return getAll().findAny().isEmpty();
    }
    default boolean isFull() {
        return getAll().count() == getCapacity();
    }

    default boolean containsOnly(Predicate<Rs2ItemModel> filter) {
        return getAll().allMatch(filter);
    }
    default boolean containsOnly(boolean exact, String ... names) {
        return containsOnly(item -> matches(item, exact, toLowerCase(names)));
    }
    default boolean containsOnly(String ... names) {
        return containsOnly(false, names);
    }
    default boolean containsOnly(int ... ids) {
        return containsOnly(item -> matches(item, ids));
    }

    default boolean containsAny(Predicate<Rs2ItemModel> filter) {
        return getAll().anyMatch(filter);
    }
    default boolean containsAny(boolean exact, String ... names) {
        return containsOnly(item -> matches(item, exact, toLowerCase(names)));
    }
    default boolean containsAny(String ... names) {
        return containsOnly(false, names);
    }
    default boolean containsAny(int ... ids) {
        return containsOnly(item -> matches(item, ids));
    }

    default boolean containsAll(boolean exact, String ... names) {
        return Arrays.stream(names).allMatch(name -> containsAny(exact, name));
    }
    default boolean containsAll(String ... names) {
        return containsAll(false, names);
    }
    default boolean containsAll(int ... ids) {
        return Arrays.stream(ids).allMatch(this::containsAny);
    }

    private static String[] toLowerCase(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            if (strings[i] == null) continue;
            strings[i] = strings[i].toLowerCase();
        }
        return strings;
    }
    private static boolean matches(Rs2ItemModel item, int ... ids) {
        return Arrays.stream(ids).anyMatch(id -> item.getId() == id);
    }
    private static boolean matches(Rs2ItemModel item, boolean exact, String ... lowerCaseNames) {
        final String itemNameLowerCase = item.getName().toLowerCase();
        return Arrays.stream(lowerCaseNames).anyMatch(exact ? itemNameLowerCase::equals : itemNameLowerCase::contains);
    }
}
