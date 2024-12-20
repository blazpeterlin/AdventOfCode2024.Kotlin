package practice_23_17

import common.*
import practice_23_03.parseMap
import practice_23_16.DOWN
import practice_23_16.LEFT
import practice_23_16.RIGHT
import practice_23_16.UP

fun parseInput(): Map<Coord2, Long> {
    val r =
        Parsing().parseLns("practice")
            .filter { it.isNotEmpty() }
            .toList()

    return parseMapLong(r)

}

fun turnLeft(dir: Coord2): Coord2 {
    val r = when(dir) {
        LEFT -> DOWN
        DOWN -> RIGHT
        RIGHT -> UP
        UP -> LEFT
        else -> TODO("bad L")
    }

    return r
}
fun turnRight(dir: Coord2): Coord2 {
    val r = when(dir) {
        LEFT -> UP
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        else -> TODO("bad R")
    }

    return r
}


data class State(val c: Coord2, val dir: Coord2, val cost: Long, val fwCount: Int, val steps: String)

fun getN1 (st: State, m: Map<Coord2, Long>): List<State> {
    val c = st.c
    val cost = st.cost
    val dirL = turnLeft(st.dir)
    val dirR = turnRight(st.dir)

    val fw1 = st.c + st.dir
    val fw2 = fw1 + st.dir
    val fw3 = fw2 + st.dir

    val res = listOf(
        State(c, dirL, cost, 0, st.steps),
        State(c, dirR, cost, 0, st.steps),
        State(fw1, st.dir, cost+(m[fw1] ?: 0L), st.fwCount+1, st.steps+dirStr(st.dir)),
        State(fw2, st.dir, cost+(m[fw1] ?: 0L)+(m[fw2] ?: 0L), st.fwCount+2, st.steps+dirStr(st.dir)+dirStr(st.dir) ),
        State(fw3, st.dir, cost+(m[fw1] ?: 0L)+(m[fw2] ?: 0L)+(m[fw3] ?: 0L), st.fwCount+3, st.steps+dirStr(st.dir)+dirStr(st.dir)+dirStr(st.dir)),
    ).filter { m.containsKey(it.c) }
    .filter{ (it.c == c && st.fwCount > 0) || it.c != c && it.fwCount <= 3 }

    return res.toList()
}

fun dirStr(dir: Coord2): String {
    return when(dir) {
        LEFT -> "L"
        RIGHT -> "R"
        UP -> "U"
        DOWN -> "D"
        else -> TODO("oops")
    }
}

fun part1(): Long {
    val inps: Map<Coord2, Long> = parseInput()
    
    val gs = Graph<State>().TraverseWithIds(
        starts = listOf(
            State(Coord2(0L,0L), Coord2(+1L, 0L), 0, 0, ""),
            State(Coord2(0L,0L), Coord2(0L, +1L), 0, 0, "")

        ),
        getNeighbours = { st -> getN1(st, inps) },
        getScore = { st -> st.cost.toLong() },
        getId = { st -> State(st.c, st.dir, 0, st.fwCount, "") }
    )

    val finish = inps.keys.sortedByDescending { it.x + it.y }.first()

    val resSt = gs.filter { it.c == finish }.take(100).toList()
    val res = resSt.first().cost
    return res
}

fun getN2 (st: State, m: Map<Coord2, Long>): List<State> {
    val c = st.c
    val cost = st.cost
    val dirL = turnLeft(st.dir)
    val dirR = turnRight(st.dir)

    val fw1 = st.c + st.dir

    val res = listOf(
        State(c, dirL, cost, 0, st.steps),
        State(c, dirR, cost, 0, st.steps),
        State(fw1, st.dir, cost+(m[fw1] ?: 0L), st.fwCount+1, st.steps+dirStr(st.dir)),
    ).filter { m.containsKey(it.c) }
        .filter{ (it.c == c && st.fwCount >= 4) || it.c != c && it.fwCount <= 10 }

    return res.toList()
}

fun part2(): Long {
    val inps: Map<Coord2, Long> = parseInput()

    val gs = Graph<State>().TraverseWithIds(
        starts = listOf(
            State(Coord2(0L,0L), Coord2(+1L, 0L), 0, 0, ""),
            State(Coord2(0L,0L), Coord2(0L, +1L), 0, 0, "")

        ),
        getNeighbours = { st -> getN2(st, inps) },
        getScore = { st -> st.cost.toLong() },
        getId = { st -> State(st.c, st.dir, 0, st.fwCount, "") }
    )

    val finish = inps.keys.sortedByDescending { it.x + it.y }.first()

    val resSt = gs.filter { it.c == finish }.take(100).toList()
    val res = resSt.first().cost
    return res
}

