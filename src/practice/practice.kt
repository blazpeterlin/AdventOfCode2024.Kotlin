package practice

import common.*

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToLong

data class Input(val nums: List<Long>)

fun parseInput(): Map<Coord2, Char> {
    val r =
        common.Parsing().parseLns("practice")
            .filter { it.isNotEmpty() }
            .toList()

    val map = parseMap(r)

    return map.m.filter{ k -> k.value == '#' }
}

fun part1(): Long {
    val inp = parseInput()

    return 0L
}

fun part2(): Long {
    val inp = parseInput()

    return 0L
}

