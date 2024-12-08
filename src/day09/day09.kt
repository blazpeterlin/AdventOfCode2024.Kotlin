package day09

import common.*

data class Input(val nums: List<Long>)


fun parseInput(): List<Input> {
    val r =
        common.Parsing().parseLns("day09")
            .filter { it.isNotEmpty() }
            .map { it.split(Regex("[\\s]")).filter{ it.isNotEmpty() }.map{ s -> s.toLong()} }
            .toList()
    return r.map{ Input(it) }.toList();
}

fun part1(): Long {
    val inp = parseInput()

    return 0
}


fun part2(): Long {
    val inp = parseInput()

    return 0
}