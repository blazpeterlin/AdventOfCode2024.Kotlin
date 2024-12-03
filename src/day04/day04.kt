package day04

data class InputTerm(val levels: List<Long>)

fun parseInput(): List<InputTerm> {
    val inputTerms: List<InputTerm> =
        common.Parsing().parseLns("day04")
            .filter { it.isNotEmpty() }
            .map { ln ->
                ln.split(Regex("\\s+"))
                    .map{ tkn -> tkn.toLong() }
            }
            .map { InputTerm(it) }
            .toList()

    return inputTerms
}

fun part1(): Int {
    val inputTerms = parseInput()

    return 0
}

fun part2(): Int {
    val inputTerms = parseInput()

    return 0
}