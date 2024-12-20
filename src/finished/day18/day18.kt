package finished.day18

import common.Coord2
import common.Graph
import common.Parsing
import common.to4ways

//data class Input(val arng: String, val constraint: List<Long>)

fun parseInput(): List<Coord2> {
    val r =
        Parsing().parseLns("day18")
            .filter { it.isNotEmpty() }
            .map{it.split(',').map{it.toLong()}}
            .map{ (a, b) -> Coord2(a, b) }
            .toList()

    return r
}

data class State(val c:Coord2, val stepsTaken: Long)

fun getNeighbours(spaceMax: Long, set: Set<Coord2>, state: State): List<State> {
    val candidates =
        to4ways(state.c)
            .filter { !set.contains(it) }
            .filter{ it.x >= 0 && it.y >= 0 && it.x <= spaceMax && it.y <= spaceMax}

    return candidates.map{ c -> State(c, state.stepsTaken+1 )}.toList()
}

fun solve(inps: List<Coord2>, spaceMax: Long, limitedTo: Int): Long? {


    val set = inps.take(limitedTo).toSet()

    val step0 = State(Coord2(0,0), 0)
    val tgt = Coord2(spaceMax, spaceMax)

    val resState = Graph<State>().TraverseWithIds(
        starts = listOf(step0),
        getNeighbours = { st -> getNeighbours(spaceMax, set, st) },
        getScore = { st -> st.stepsTaken },
        getId = { st -> st.c }
    ).filter{ it.c == tgt }
        .firstOrNull()

    val res = resState?.stepsTaken

    return res
}

fun part1(): Long {
    val inps = parseInput()

    val spaceMax = if (inps.maxOf{it.x} <= 6) 6L else 70L
    val limitedTo = if(spaceMax == 6L) 12 else 1024

    val res = solve(inps, spaceMax, limitedTo)

    return res!!
}


fun part2(): String? {
    val inps = parseInput()

    val spaceMax = if (inps.maxOf{it.x} <= 6) 6L else 70L

    for(limitedTo in 0..inps.size) {
        val res = solve(inps, spaceMax, limitedTo+1)

        if (res == null) {
            val r = inps[limitedTo]
            return "" + r.x + "," + r.y
        }
    }


    return null
}

