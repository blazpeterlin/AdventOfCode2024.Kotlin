package practice_23_03

import common.Coord2
import common.grabNumbers

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

fun parseInput(): InputTerms  {
    val inputLns = common.Parsing().parseLns("practice").filter { it.isNotEmpty() }
    val inputMap: ParsedMap = parseMap(inputLns)
    val numbersWithCoords = parseNumbers(inputLns)

    return InputTerms(inputMap, numbersWithCoords)

}

data class InputTerms(val inputMap: ParsedMap, val numbersWithCoords: List<ParsedNumber>)

fun getPart1ApplicableNums(inputTerms: InputTerms): List<Long> {

    val partCoordsSet = inputTerms.inputMap.m.filter{ kvp -> !kvp.value.isDigit() && kvp.value != '.' }.keys
        .flatMap { c -> to8ways(c) }
        .distinct()
        .toSet()

    val applicableNums = inputTerms.numbersWithCoords.filter{ pn -> pn.coords.any { c -> partCoordsSet.contains(c) }}.toList()

    return applicableNums.map{ pn -> pn.num }
}

fun getPart2GearRatios(inputTerms: InputTerms): List<Long> {
    val distinctNumAssociations: Map<Coord2, List<Long>> =
        inputTerms.numbersWithCoords
            .map{ nwc -> nwc.coords.map{ c -> to8ways(c).map { mappedc -> mappedc to nwc.num } }.flatten() }
            .flatten()
            .distinct()
            .groupBy { it.first }
            .map { it.key to it.value.map{ pnv -> pnv.second }.toList() }
            .toMap()

    val gearRatios = inputTerms.inputMap.m
        .filter { x -> x.value == '*' }
        .keys.filter { c -> distinctNumAssociations[c]?.size==2 }
        .map { c -> distinctNumAssociations[c]!!.reduce{ a, b -> a*b } }
        .toList()

    return gearRatios
}

fun part1(): Long {
    val inputTerms = parseInput()

    val applicableNums = getPart1ApplicableNums(inputTerms)
    return applicableNums.sum()
}


fun part2(): Long {
    val inputTerms = parseInput()

    val gearRatios = getPart2GearRatios(inputTerms)

    return gearRatios.sum()
}