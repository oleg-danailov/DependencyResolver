package org.example;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class DependenciesResolver {

    /**
     * Method, which creates map from given strings with dependencies
     * @param  input array of strings, similar to {"A B C", "B C E",...
     * @return SortedMap with dependencies
     */
    public SortedMap<String, Collection<String>> parseDependencies(final String[] input){

        SortedMap<String, Collection<String>> dependencies = new TreeMap<>();
        for (String current : input){
            final String[] strings = current.split(" ");
            List<String> dependants = new ArrayList<>();

            // start from second element. first one is key
            for (int i = 1; i < strings.length; i++) {
                dependants.add(strings[i]);
            }
            dependencies.put(strings[0], dependants);
        }
        return dependencies;
    }

    /**
     * Method, which creates map with resolved dependencies from partial map.
     * Example:  {@code [{A -> B, C},
     *         {B -> C, E},
     *         {C -> D}]}
     * will be transformed to:
     *          {@code [{A -> B, C, D, E},
     *          {B -> C, D, E},
     *          {C -> D}]}
     */
    public SortedMap<String, Collection<String>> resolveDependencies(final SortedMap<String,
            Collection<String>> partialDependencies) {
        SortedMap<String, Collection<String>> fullDependencies = new TreeMap<>();
        for (final String className : partialDependencies.keySet()) {
            SortedSet<String> allDependencies = new TreeSet<>();
            Deque<String> stack = new ArrayDeque<>();
            stack.add(className);

            while (stack.size() > 0){
                final String current = stack.pop();
                final Collection<String> currentDependents = getDependents(current, partialDependencies);
                if (currentDependents != null){
                    stack.addAll(currentDependents);
                    allDependencies.addAll(currentDependents);
                }
            }
            fullDependencies.put(className, new ArrayList<>(allDependencies));
        }
        return fullDependencies;
    }

    /**
     * This method uses full dependencies map to create inverse map -
     * collection of items, which depend on each item
     */
    public SortedMap<String, Collection<String>> createInverseDependenciesMap(final SortedMap<String,
            Collection<String>> fullDependencies) {
        SortedMap<String, Collection<String>> output = new TreeMap<>();
        fullDependencies.forEach((key, collection) -> collection.forEach(s -> {
            SortedSet<String> dependencies = (SortedSet<String>) output.computeIfAbsent(s, k -> new TreeSet<>());
            dependencies.add(key);
        }));
        return output;
    }

    private Collection<String> getDependents(String className, Map<String, Collection<String>> map){
        return map.get(className);
    }
}
