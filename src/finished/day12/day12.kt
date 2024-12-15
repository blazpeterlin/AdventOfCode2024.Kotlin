package finished.day12

import common.*

data class Input(val nums: List<Long>)


fun parseInput(): Map<Coord2, Char> {
    val r =
        common.Parsing().parseLns("finished/day12")
            .filter { it.isNotEmpty() }
            .toList()

    val map = parseMap(r);
    return map.m;
}

fun eatRegion(mm: MutableMap<Coord2, Char>): List<Coord2> {
    val fst = mm.keys.first()

    val explored = mutableSetOf(fst)
    val open = mutableSetOf(fst)
    while(true) {

        if (!open.any()) { break }

        val next = open.map{ to4ways(it) }.flatten()
            .filter { !explored.contains(it) && mm.containsKey(it) && mm[it]==mm[fst] }
            .toList()

        open.clear()
        open.addAll(next)

        explored.addAll(next)
    }

    for(expl in explored) {
        mm.remove(expl)
    }

    return explored.toList()
}

fun getPrice(region: List<Coord2>): Long {
    val area = region.size.toLong()

    val perimeterX =
        region.map{c ->
            listOf(c, Coord2(c.x-1, c.y))}
        .flatten()
        .groupBy{it}
        .filter{grp -> grp.value.size == 1}
        .keys.size.toLong()
    val perimeterY =
        region.map{c -> listOf(c, Coord2(c.x, c.y-1))}
            .flatten()
            .groupBy{it}
            .filter{grp -> grp.value.size == 1}
            .keys.size.toLong()
    val perimeter = perimeterX + perimeterY

    return area * perimeter
}

fun getPrice2(region: List<Coord2>, m: Map<Coord2, Char>): Long {
    val area = region.size.toLong()

    val perimetersX =
        region.map{c ->
            listOf( Pair(c,'L'), Pair(Coord2(c.x-1, c.y),'R'))
        }
            .flatten()
            .groupBy{it.first}
            .filter{grp -> grp.value.size == 1}
            .values
            .map{it.single()}
            .toList()
    val perimetersY =
        region.map{c ->
            listOf(Pair(c, 'D'), Pair(Coord2(c.x, c.y-1), 'U'))
        }
            .flatten()
            .groupBy{it.first}
            .filter{grp -> grp.value.size == 1}
            .values
            .map{it.single()}
            .toList()

    val grpsX = perimetersY.groupBy{ Pair(it.first.y, it.second) }
    val actualPlotLinesX = grpsX.map { gx ->
        numPlots(gx.value.map{ it.first.x })
    }.sum()

    val grpsY = perimetersX.groupBy{ Pair(it.first.x, it.second) }
    val actualPlotLinesY = grpsY.map { gy ->
        numPlots(gy.value.map{ it.first.y })
    }.sum()

    val plotLines = actualPlotLinesX + actualPlotLinesY

    return area * plotLines
}

fun numPlots(nums: List<Long>): Long {
    val sorted = nums.sorted()
    var res = 1L;
    for((a,b) in sorted.windowed(2)) {
        if (b > a+1) {res++}
    }
    return res
}

fun part1(): Long {
    val m = parseInput()

    val mm = m.toMutableMap()
    val regs : MutableList<List<Coord2>> = mutableListOf()
    while(mm.any()) {
        regs.add(eatRegion(mm))
    }

    val prices = regs.map{ getPrice(it) }

    return prices.sum()
}


data class Region(val id: Char, val coords: List<Coord2>)

fun part2(): Long {
    val m = parseInput()

    val mm = m.toMutableMap()
    val regs : MutableList<List<Coord2>> = mutableListOf()
    while(mm.any()) {
        regs.add(eatRegion(mm))
    }

    val regsSorted = regs.sortedByDescending { it.size }
    println()
    for(reg in regsSorted) {
        printMap(reg.map{ it to FULLBLOCK}.toMap())
        println(reg.size)
    }

    val regsTyped = regs.map{coords -> Region(m[coords[0]]!!, coords)}

    val prices = regsTyped.map{ Pair(it.id, getPrice2(it.coords, m)) }.sortedByDescending{ it.second }.toList()


    return prices.map{it.second}.sum()
}