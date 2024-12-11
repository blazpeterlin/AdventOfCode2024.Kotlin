package day12

import java.math.BigInteger

data class Input(val nums: List<Long>)


fun parseInput(): List<Long> {
    val r: List<Long> =
        common.Parsing().parseLns("day12")
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