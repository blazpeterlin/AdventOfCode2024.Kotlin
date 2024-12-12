package finished.day02

import kotlin.math.abs

data class InputTerm(val levels: List<Long>)

fun parseInput(): List<InputTerm> {
    val inputTerms: List<InputTerm> =
        common.Parsing().parseLns("day02")
            .filter { it.isNotEmpty() }
            .map { ln ->
                ln.split(Regex("\\s+"))
                    .map{ tkn -> tkn.toLong() }
            }
            .map { InputTerm(it) }
            .toList()

    return inputTerms
}

fun isSafeReport(inputTerm: InputTerm): Boolean {
    val isSorted =
        inputTerm.levels.sorted() == inputTerm.levels
        || inputTerm.levels.sorted() == inputTerm.levels.reversed()

    val isSteadilyAdjacent =
            inputTerm.levels
                .windowed(size = 2, step = 1, partialWindows = false)
                .all { abs(it[0] - it[1]) in 1 .. 3 }


    return isSorted && isSteadilyAdjacent
}

fun getPermutations(inputTerm: InputTerm): Sequence<InputTerm> {
    val len = inputTerm.levels.size

    return sequence {
        for (i in 0..<len) {
            val r = InputTerm(
                inputTerm.levels.subList(0, i)
                        +
                        inputTerm.levels.subList(i + 1, len)
            )
            yield(r)
        }
    }
}

fun part1(): Int {
    val inputTerms = parseInput()

    return inputTerms.map { isSafeReport(it) }.count { it }
}

fun part2(): Int {
    val inputTerms = parseInput()

    val alreadySafe = inputTerms.map { isSafeReport(it) }.count { it }

    val furtherCheckTerms = inputTerms.filter{ !isSafeReport(it) }
    val tolerated = furtherCheckTerms.count { inputTerm ->
        val permutations = getPermutations(inputTerm)
        val okPerm = permutations.find { isSafeReport(it) }
        okPerm != null
    }

    return alreadySafe + tolerated
}