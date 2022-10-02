package org.example;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DependenciesResolverTest {

    private DependenciesResolver dependenciesResolver;

    @BeforeAll
    void setUp(){
        dependenciesResolver = new DependenciesResolver();
    }
    @Test
    void parseDependenciesValidTest() {
        // given
        String[] input = {"A B C", "B C E", "C G", "D A F", "E F", "F H"};

        // when
        final SortedMap<String, Collection<String>> parsedDependencies = dependenciesResolver.parseDependencies(input);

        // then
        assertEquals(6, parsedDependencies.size());
        assertIterableEquals(Arrays.asList("A", "B", "C", "D", "E", "F"), parsedDependencies.keySet());
        assertCorrectDataParsed(parsedDependencies);
    }

    @Test
    void resolveDependenciesValidTest() {
        // given
        final SortedMap<String, Collection<String>> parsedDependencies = prepareParsedDependencies();
        // when
        final SortedMap<String, Collection<String>> fullDependencies = dependenciesResolver.resolveDependencies(parsedDependencies);

        // then
        assertEquals(6, fullDependencies.size());
        //todo add all other assertions
    }

    @Test
    void createInverseDependenciesMapValidTest() {
        // given
        final SortedMap<String, Collection<String>> fullDependencies = prepareFullDependenciesMap();
        // when
        final SortedMap<String, Collection<String>> inverseDependenciesMap =
                dependenciesResolver.createInverseDependenciesMap(fullDependencies);
        // then
        assertEquals(7, inverseDependenciesMap.size());
        //todo add all other assertions
    }

    private SortedMap<String, Collection<String>> prepareParsedDependencies(){
        String[] input = {"A B C", "B C E", "C G", "D A F", "E F", "F H"};
        return dependenciesResolver.parseDependencies(input);

    }

    private SortedMap<String, Collection<String>> prepareFullDependenciesMap(){
        final SortedMap<String, Collection<String>> parsedDependencies = prepareParsedDependencies();
        return dependenciesResolver.resolveDependencies(parsedDependencies);
    }

    private void assertCorrectDataParsed(final SortedMap<String, Collection<String>> parsedDependencies) {
        parsedDependencies.forEach((s, strings) -> {
            switch (s) {
                case "A":
                    assertIterableEquals(Arrays.asList("B", "C"), strings);
                    break;
                case "B":
                    assertIterableEquals(Arrays.asList("C", "E"), strings);
                    break;
                case "C":
                    assertIterableEquals(Collections.singletonList("G"), strings);
                    break;
                case "D":
                    assertIterableEquals(Arrays.asList("A", "F"), strings);
                    break;
                case "E":
                    assertIterableEquals(Collections.singletonList("F"), strings);
                    break;
                case "F":
                    assertIterableEquals(Collections.singletonList("H"), strings);
                    break;
                default:
                    throw new AssertionError("None of keys is present, parsing failed");
            }
        });
    }
}