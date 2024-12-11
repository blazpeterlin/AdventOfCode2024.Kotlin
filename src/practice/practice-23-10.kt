package practice_23_10

import common.Coord2
import common.minus
import common.parseMap
import common.to4ways
import kotlin.math.absoluteValue
import kotlin.math.roundToLong

data class Input(val nums: List<Long>)

fun parseInput(): Map<Coord2, Char> {
    val r =
        common.Parsing().parseLns("practice")
            .filter { it.isNotEmpty() }
            .toList()

    val map = parseMap(r)

    return map.m
}

fun connects(ch: Char, dir: Coord2): Boolean {

    val R = Coord2(1, 0)
    val L = Coord2(-1, 0)
    val U = Coord2(0, -1)
    val D = Coord2(0, +1)

    val r = when (Pair(ch, dir)) {
        Pair('|', U) -> true
        Pair('|', D) -> true

        Pair('-', L) -> true
        Pair('-', R) -> true

        Pair('L', U) -> true
        Pair('L', R) -> true

        Pair('J', L) -> true
        Pair('J', U) -> true

        Pair('7', L) -> true
        Pair('7', D) -> true

        Pair('F', D) -> true
        Pair('F', R) -> true

        Pair('S', L) -> true
        Pair('S', R) -> true
        Pair('S', U) -> true
        Pair('S', D) -> true

        else -> false
    }

    return r
}

fun traverseLoop(start: Coord2, map: Map<Coord2, Char>) : List<Coord2> {
    val visited = mutableSetOf(start)
    val edge = mutableSetOf(start)

    var isFirst = true

    while(true) {
        val visC = visited.size

        val neighbours: List<Coord2> = edge.map { e ->
            val ns = to4ways(e).filter{ n -> !visited.contains(n) }
                .filter {n ->
                    map[n] != null
                            && connects(map[n]!!, e - n)
                            && connects(map[e]!!, n - e)
                }
                .toList()

            ns
        }.flatten()
            .take(if(isFirst) 1 else 999)
            .toList()

        isFirst = false

        visited.addAll(neighbours)
        edge.clear()
        edge.addAll(neighbours)

        if (visited.size == visC) { break }
    }

    return visited.toList()
}

fun polygonArea(loop: List<Coord2>): Long {

    val loopEnclosed = loop.toMutableList()
    loopEnclosed.add(loop[0])

    val crossProducts = loopEnclosed
        .windowed(2)
        .map { (a, b) -> a.x * b.y - b.x * a.y }

    val realWorldArea = crossProducts.sum().absoluteValue / 2L
    val adjustAreaBy =
        1L - loopEnclosed
            .windowed(2)
            .map{ (a, b) -> (a.x - b.x).absoluteValue / 2.0 + (a.y - b.y).absoluteValue / 2.0 }
            .sum()
            .roundToLong()

    return realWorldArea + adjustAreaBy
}

fun part1(): Long {
    val inp = parseInput()

    val start = inp.filter{ it.value=='S'}.toList()[0]
    val posS = start.first

    val loop = traverseLoop(posS, inp)
    val res1 = loop.size / 2

    return res1.toLong()
}

fun part2(): Long {
    val inp = parseInput()

    val start = inp.filter{ it.value=='S'}.toList()[0]
    val posS = start.first

    val loop = traverseLoop(posS, inp)
    val enclosedSize: Long = polygonArea(loop)

    return enclosedSize
}

