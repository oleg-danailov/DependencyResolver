package org.example;

import java.util.Collection;
import java.util.SortedMap;


public class Main {

    public static void main(String[] args) {

        String[] input = {"A B C", "B C E", "C G", "D A F", "E F", "F H"};
        DependenciesResolver dependenciesResolver = new DependenciesResolver();

        SortedMap<String, Collection<String>> inputDependencies = dependenciesResolver.parseDependencies(input);

        SortedMap<String, Collection<String>> fullDependencies = dependenciesResolver.resolveDependencies(inputDependencies);

        SortedMap<String, Collection<String>> inverseDependencies = dependenciesResolver.createInverseDependenciesMap(fullDependencies);

        printDependencyTree(fullDependencies);
        System.out.println("--------------");
        printDependencyTree(inverseDependencies);

    }

    private static void printDependencyTree(final SortedMap<String, Collection<String>> fullDependencies) {
        fullDependencies.forEach((s, strings) -> {
            String current = combineDependencies(strings);
            System.out.println(s + current);
        });
    }

    private static String combineDependencies(final Collection<String> allDependencies) {
        StringBuilder output = new StringBuilder();
        allDependencies.forEach(output::append);
        return output.toString();
    }
}