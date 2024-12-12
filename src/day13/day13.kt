package day13

data class Input(val nums: List<Long>)


fun parseInput(): List<Long> {
    val r: List<Long> =
        common.Parsing().parseLns("day11")
            .filter { it.isNotEmpty() }
            .map { ln -> ln.split(" ").map{ n->n.toLong() }}
            .first()
            .toList()
    return r;
}

fun part1(): Long {
    val inp = parseInput()

    return 0L
}


fun part2(): Long {
    val inp = parseInput()

    return 0L
}