package practice_23_11

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

fun getDist(emptyFactor: Long, xs: Set<Long>, ys: Set<Long>, a: Coord2, b: Coord2): Long {
    var res = 0L

    for(y in min(a.y, b.y)+1..max(a.y, b.y)) {
        res += if (ys.contains(y)) 1 else emptyFactor
    }

    for(x in min(a.x, b.x)+1..max(a.x, b.x)) {
        res += if (xs.contains(x)) 1 else emptyFactor
    }

    return res
}

fun part1(): Long {
    val inp = parseInput()

    val xs = inp.keys.map{it.x}.toSet()
    val ys = inp.keys.map{it.y}.toSet()

    val dists: List<Long> = allPairs(inp.keys.toList(), inp.keys.toList())
        .filter{(a,b) -> a != b}
        .map{(a, b) -> getDist(2L, xs, ys, a, b)}
        .toList()

    return dists.sum() / 2L
}

fun part2(): Long {
    val inp = parseInput()

    val xs = inp.keys.map{it.x}.toSet()
    val ys = inp.keys.map{it.y}.toSet()

    val dists: List<Long> = allPairs(inp.keys.toList(), inp.keys.toList())
        .filter{(a,b) -> a != b}
        .map{(a, b) -> getDist(1000000L, xs, ys, a, b)}
        .toList()

    return dists.sum() / 2L
}

