package com.github.leo_scream.fasta

import spock.lang.Specification

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
class PermutationsSpecification extends Specification {
    Permutations permutations

    def setup() {
        permutations = new Permutations()
    }

    def "Permutations throws NPE if constructed with null alphabet"() {
        when:
        permutations.with(null)

        then:
        thrown(NullPointerException)
    }

    def "Permutations throws IAE if constructed with negative length"() {
        when:
        permutations.with(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def "Size of permutations without alphabet and length is 0"() {
        expect:
        permutations.size() == 0
    }

    def "Size calculates correctly"() {
        setup:
        permutations = permutations.with(alphabet as SortedSet).with(length as int)

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
        permutations = permutations.with(["A", "C", "G", "T"] as SortedSet).with(3)

        expect:
        permutations.get(index) == permutation

        where:
        permutation || index
        "AAA"       || 0
        "AAC"       || 1
        "CAA"       || 16
        "CAG"       || 18
        "TTT"       || 63
    }
}
