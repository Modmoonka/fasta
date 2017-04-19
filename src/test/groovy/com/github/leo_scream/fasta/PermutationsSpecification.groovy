package com.github.leo_scream.fasta

import spock.lang.Specification

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
class PermutationsSpecification extends Specification {
    Permutations<String> permutations

    def setup() {
        permutations = new Permutations()
    }

    def "Permutations throws NPE if constructed with null choices"() {
        when:
        permutations.of(null)

        then:
        thrown(NullPointerException)
    }

    def "Permutations throws IAE if constructed with negative positions"() {
        when:
        permutations.using(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def "Size of permutations without choices and positions is 0"() {
        expect:
        permutations.size() == 0
    }

    def "Size calculates correctly"() {
        setup:
        permutations = permutations.of(alphabet as SortedSet).using(length as int)

        expect:
        permutations.size() == size

        where:
        alphabet             | length || size
        ["A", "C", "G", "T"] | 4      || 256
        ["A", "C", "G", "T"] | 5      || 1024
        ["X", "Y"]           | 3      || 8
    }

    def "i-th permutation of negative index throws AIOBE"() {
        when:
        permutations.get(-1)

        then:
        thrown(ArrayIndexOutOfBoundsException)
    }

    def "i-th permutation of index greater than size throws AIOBE"() {
        when:
        permutations.get(permutations.size() + 1)

        then:
        thrown(ArrayIndexOutOfBoundsException)
    }

    def "i-th permutation of index works correctly"() {
        setup:
        permutations = permutations.of(["A", "C", "G", "T"] as SortedSet).using(3)

        expect:
        permutations.get(index) == permutation as String[]

        where:
        permutation     || index
        ["A", "A", "A"] || 0
        ["A", "A", "C"] || 1
        ["C", "A", "A"] || 16
        ["C", "A", "G"] || 18
        ["T", "T", "T"] || 63
    }
}
