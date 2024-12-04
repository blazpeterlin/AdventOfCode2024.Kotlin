package day05

data class InputTerm(val str: List<String>)

fun parseInput(): List<InputTerm> {
    val r =
        common.Parsing().parseLns("day05")
            .filter { it.isNotEmpty() }
            .map { ln ->
                ln.split(Regex("\\s+"))
//                    .map{ tkn -> tkn.toLong() }
            }
            .map { InputTerm(it) }
            .toList()

    return r
}


fun part1(): Int {
    val inputTerm = parseInput()

    return 0
}

fun part2(): Int {
    val inputTerm = parseInput()

    return 0
}