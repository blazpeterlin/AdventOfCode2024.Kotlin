package practice

import common.Coord2
import common.GrabbedNumber
import common.grabNumbers
import kotlin.streams.toList

//

data class ParsedMap(val m: Map<Coord2, Char>)
data class ParsedNumber(val num:Long, val coords: List<Coord2>)



fun parseNumbers(lns: List<String>): List<ParsedNumber> {
    return lns.mapIndexed { y: Int, ln: String ->
        grabNumbers(ln).map { gn ->
            val xs: Sequence<Int> = sequence { for (x in gn.idx .. gn.lastIdx) yield(x) }
            val coords = xs.map { x -> Coord2(x.toLong(), y.toLong()) }.toList()
            ParsedNumber(gn.num, coords)
        }.toList()
    }.toList().flatten().toList()
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

operator fun Coord2.plus(other: Coord2): Coord2 { return Coord2(this.x + other.x, this.y + other.y) }

fun to8ways(c: Coord2) : List<Coord2> {
    return listOf(
        Coord2(-1,-1),
        Coord2(0,-1),
        Coord2(+1,-1),
        Coord2(-1,0),
        Coord2(+1,0),
        Coord2(-1,+1),
        Coord2(0,+1),
        Coord2(+1,+1),
    ).map { it + c }.toList()
}

fun parseInput(): List<Long>  {
    val inputLns = common.Parsing().parseLns("practice").filter { it.isNotEmpty() }
    val inputMap: ParsedMap = parseMap(inputLns)

    val partCoordsSet = inputMap.m.filter{ kvp -> !kvp.value.isDigit() && kvp.value != '.' }.keys
        .flatMap { c -> to8ways(c) }
        .distinct()
        .toSet()

    val numbersWithCoords = parseNumbers(inputLns)
    val applicableNums = numbersWithCoords.filter{ pn -> pn.coords.any { c -> partCoordsSet.contains(c) }}.toList()

    return applicableNums.map{ pn -> pn.num }
}


fun part1(): Long {
    val applicableNums: List<Long> = parseInput()

    return applicableNums.sum()
}

fun part2(): Long {
    val inputTerms = parseInput()

    return 0
}