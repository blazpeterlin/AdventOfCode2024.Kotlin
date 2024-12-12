package day10again

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
    val m = parseInput()

    val starts = m.filter{ it.value == 0L }.map{it.key}.toList()

    val traverses = starts.map { start ->
        Graph<Coord2>().Traverse(
            listOf(start),
            { c -> to4ways(c).filter { m.containsKey(it) && m[it] == (m[c]!! + 1) }.map { Pair(it, 0L) }.toList() },
            { c -> 0L }
        ).toList()
    }.flatten()

    val found = traverses.filter{ c -> m[c] == 9L }

    val res = found.count()
    return res.toLong()
}

data class Node(val coord: Coord2, val pathBack: Node?)

fun part2(): Long {
    val m = parseInput()

    val starts = m.filter{ it.value == 0L }.map{it.key}.map{ s -> Node(s, null) }.toList()

    val traverses = starts.map { start ->
        Graph<Node>().Traverse(
            listOf(start),
            { c -> to4ways(c.coord).filter { m.containsKey(it) && m[it] == (m[c.coord]!! + 1) }.map { Pair(Node(it, c), 0L) }.toList() },
            { c -> 0L }
        ).toList()
    }.flatten()

    val found = traverses.filter{ c -> m[c.coord] == 9L }

    val res = found.count()
    return res.toLong()
}