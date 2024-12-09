package day10

data class Input(val nums: List<Long>)


fun parseInput(): Input {
    val r: List<Long> =
        common.Parsing().parseLns("day10")
            .filter { it.isNotEmpty() }
            .map { it.toCharArray().map{ s -> s.toString().toLong()} }
            .flatten()
            .toList()
    return Input(r);
}

fun part1(): Long {
    val inp = parseInput()

    return 0
}


fun part2(): Long {
    val inp = parseInput()

    return 0
}