package day07

data class InputOrdering(val n1: Long, val n2: Long)
data class InputPrint(val ns: List<Long>)

fun parseInput(): List<String> {
    val r =
        common.Parsing().parseLns("day05")
            .filter { it.isNotEmpty() }
            .toList()
    return r
}

fun part1(): Long {
    val inp = parseInput()

    return 0
}


fun part2(): Long {
    val inp = parseInput()

    return 0
}