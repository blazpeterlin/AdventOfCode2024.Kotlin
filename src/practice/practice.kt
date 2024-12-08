package practice

data class Input(val ins: List<String>)

fun parseInput(): Input {
    val r =
        common.Parsing().parseLns("practice")
            .filter { it.isNotEmpty() }
            .toList()
    return Input(r)
}

fun part1(): Long {
    val inp = parseInput()

    return 0
}

fun part2(): Long {
    val inp = parseInput()

    return 0
}