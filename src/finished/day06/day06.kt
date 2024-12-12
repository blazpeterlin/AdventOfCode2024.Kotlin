package finished.day06

import common.Coord2
import common.plus
import practice_23_03.ParsedMap

data class InputOrdering(val n1: Long, val n2: Long)
data class InputPrint(val ns: List<Long>)
data class ParsedMap(val m: Map<Coord2, Char>)

fun parseInput(): ParsedMap {
    val r =
        common.Parsing().parseLns("day06")
            .filter { it.isNotEmpty() }
            .toList()

    return parseMap(r)
}

fun parseMap(lns: List<String>): ParsedMap {
    val res =
        lns.mapIndexed { y, ln: String ->
            ln.toCharArray().toList().mapIndexed { x: Int, ch: Char ->
                Coord2(x.toLong(),y.toLong()) to ch
            }
        }.flatten().toMap()
    return ParsedMap(res);
}

fun turnRight(c: Coord2): Coord2 {
    if (c.x == -1L && c.y == 0L) {
        return Coord2(0, -1)
    } else if (c.x == 0L && c.y == -1L) {
        return Coord2(1, 0)
    } else if (c.x == 1L && c.y == 0L) {
        return Coord2(0, +1)
    } else if (c.x == 0L && c.y == 1L) {
        return Coord2(-1, 0)
    } else {
        TODO()
    }
}

fun getPart1Visited(m: Map<Coord2, Char>): List<Coord2>
{

    val maxX = m.keys.maxOf { x -> x.x }
    val maxY = m.keys.maxOf { x -> x.y }

    var gPos = m.filter{ x -> x.value == '^' }.toList()[0].first
    var gDir = Coord2(0,-1)

    val visited = listOf(gPos).toMutableSet()

    while(gPos.x <= maxX && gPos.y <= maxY && gPos.x >= 0 && gPos.y >= 0) {
        visited.add(gPos)

        var nextPos = gPos + gDir
        if (m[nextPos] == '#') {
            gDir = turnRight(gDir)
            nextPos = gPos + gDir
        }

        gPos = nextPos
    }

    return visited.toList()
}

fun part1(): Long {
    val inp = parseInput()
    val m = inp.m

    val visited = getPart1Visited(m)
    return visited.size.toLong()
}


fun isInfiniteLoop(m: Map<Coord2, Char>): Boolean {

    val maxX = m.keys.maxOf { x -> x.x }
    val maxY = m.keys.maxOf { x -> x.y }


    var gPos = m.filter{ x -> x.value == '^' }.toList()[0].first
    var gDir = Coord2(0,-1)

    val visited = listOf(Pair(gPos, gDir)).toMutableSet()

    while(gPos.x <= maxX && gPos.y <= maxY && gPos.x >= 0 && gPos.y >= 0) {


        if (m[gPos + gDir] == '#') {
            gDir = turnRight(gDir)
        } else{
            gPos += gDir
        }


        if (visited.contains(Pair(gPos, gDir))) {
            return true
        }
        visited.add(Pair(gPos, gDir))
    }

    return false;
}

fun part2(): Long {
    val inp = parseInput()

    val possibleMaps = getPart1Visited(inp.m)
        .filter { c -> inp.m[c] != '^'}
        .map { c ->
            val newMap = inp.m.toMutableMap()
            newMap[c] = '#'
            newMap
        }

    val infiniteLoopMaps = possibleMaps.filter{ m -> isInfiniteLoop(m) }

    return infiniteLoopMaps.size.toLong()
}