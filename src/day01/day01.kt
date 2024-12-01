package day01

data class InputTerm(val a: Long, val b: Long)

fun parseInput(): List<InputTerm> {
    val inputTerms: List<InputTerm> =
        common.Parsing().parseLns("day01")
            .filter { it.isNotEmpty() }
            .map { ln ->
                ln.split(Regex("\\s+"))
                    .map{ tkn -> tkn.toLong() }
            }
            .map { InputTerm(it[0], it[1]) }

    return inputTerms
}

fun getTotalDistance(listA: List<Long>, listB: List<Long>): Long {
    val sortedA = listA.sorted()
    val sortedB = listB.sorted()

    return sortedA
            .zip(sortedB)
            .map { Math.abs(it.first - it.second) }
            .sum()
}

fun getSimilarityScore(listA: List<Long>, listB: List<Long>): Long {
    val countsOfB: Map<Long, Long> =
        listB.groupBy { it }.mapValues { it.value.size.toLong() }

    val similarities = listA.map{ a -> a * countsOfB.getOrDefault(a, 0) }

    return similarities.sum()
}

fun part1(): Long {
    val inputTerms = parseInput()

    val listA = inputTerms.map{ it.a }
    val listB = inputTerms.map{ it.b }

    return getTotalDistance(listA, listB)
}

fun part2(): Long {
    val inputTerms = parseInput()

    val listA = inputTerms.map{ it.a }
    val listB = inputTerms.map{ it.b }

    return getSimilarityScore(listA, listB)
}