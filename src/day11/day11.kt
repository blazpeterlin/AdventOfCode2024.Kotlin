package day11

import common.*

data class Input(val nums: List<Long>)


fun parseInput(): Map<Coord2, Long> {
    val r: List<String> =
        common.Parsing().parseLns("day10")
            .filter { it.isNotEmpty() }
            .toList()
    return parseMapLong(r);
}

fun part1(): Long {
    val inp = parseInput()

    return 0
}


fun part2(): Long {
    val inp = parseInput()

    return 0
}