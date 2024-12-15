package day16

data class Input(val arng: String, val constraint: List<Long>)

fun parseInput(): List<Input> {
    val r =
        common.Parsing().parseLns("day16")
            .filter { it.isNotEmpty() }
            .map{it.split(' ')}
            .map{ (a, b) -> Input(a, b.split(',').map{it.toLong()}) }
            .toList()

    return r
}


fun part1(): Long {
    val inps = parseInput()

    return 0L
}

fun part2(): Long {
    val inps = parseInput()

    return 0L
}

