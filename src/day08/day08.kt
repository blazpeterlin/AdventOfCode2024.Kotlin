package day08

import common.*
import kotlin.math.roundToLong

data class Input(val ns: List<Long>)

fun parseInput(): ParsedMap {
    val r =
        common.Parsing().parseLns("day08")
            .filter { it.isNotEmpty() }
            .toList()
    return parseMap(r);
}

fun getAntennaGroups(m: Map<Coord2, Char>): List<List<Coord2>> {

    val antennaGrps = m.filter{ it.value != '.' }
        .toList()
        .groupBy { it.second }
        .map { grp -> grp.value.map { it.first } }
        .toList()

    return antennaGrps
}

fun part1(): Long {
    val inp = parseInput()
    val m = inp.m

    val antennaGrps = getAntennaGroups(m)

    val antinodes = antennaGrps.map { grp ->
        allPairs(grp, grp)
            .filter{ (a,b) -> a != b }.map { (a,b) ->
            val diff = Coord2(b.x - a.x, b.y - a.y)
            b + diff
        }.distinct().toList()
    }.flatten().distinct().toList()
    
    val antinodesWithinMap = antinodes.filter { a -> m.containsKey(a) }.toList()

    return antinodesWithinMap.size.toLong()
}


fun part2(): Long {
    val inp = parseInput()
    val m = inp.m

    val antennaGrps = getAntennaGroups(m)

    val antinodes = antennaGrps.map { grp ->
        allPairs(grp, grp)
            .filter{ (a,b) -> a != b }.map { (a,b) ->
                val diff = Coord2(b.x - a.x, b.y - a.y)
                sequence<Coord2> {
                    yield(b)
                    var res: Coord2 = b + diff
                    while(m.containsKey(res)) {
                        yield(res)
                        res = res + diff
                    }
                }
            }.flatten().distinct().toList()
    }.flatten().distinct().toList()

    val antinodesWithinMap = antinodes.filter { a -> m.containsKey(a) }.toList()

    return antinodesWithinMap.size.toLong()
}