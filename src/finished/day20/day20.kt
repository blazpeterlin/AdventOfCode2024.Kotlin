package finished.day20

import common.*
import kotlin.math.abs

data class Input(val arng: String, val constraint: List<Long>)

fun parseInput(): ParsedMap {
    val r =
        Parsing().parseLns("day20", "input.txt")
            .filter { it.isNotEmpty() }
            .toList()

    return parseMap(r)
}

val UP = Coord2(0, -1)
val DOWN = Coord2(0, +1)
val LEFT = Coord2(-1, 0)
val RIGHT = Coord2(+1, 0)

data class State(val c: Coord2, val picos: Long, val cheated: Boolean, val cheatRemaining: Int, val cheatList: List<Coord2>)


val cache: HashMap<Coord2, Pair<Long, Coord2>> = hashMapOf()

fun cacheDists(inps: Map<Coord2, Char>, finish: Coord2) {
    var edge = setOf(finish)
    var dist = 0L
    while(edge.any()) {
        val nextEdge: MutableList<Coord2> = mutableListOf()
        for(e in edge) {
            if (cache.containsKey(e)) { continue }
            cache[e] = Pair(dist, finish)

            val neighbours = to4ways(e).filter{ inps[it] != '#' }

            nextEdge.addAll(neighbours)
        }
        edge = nextEdge.toSet()

        dist++
    }
}

fun solveGeneralized(st: State): List<State> {
    val cached = cache[st.c]
    if (cached == null) {
        return listOf()
    }
    val cPicos = cached.first
    val cFinal = cached.second
    val resPicos = st.picos + cPicos
    if (resPicos < 84) {
        val a = 1
    }
    val r: State = State(cFinal, resPicos, st.cheated, st.cheatRemaining, st.cheatList)
    return listOf(r)
}

fun getNeighbours(st: State, inps: Map<Coord2, Char>): List<State> {

    if (st.cheatRemaining == -1) {
        return solveGeneralized(st)
    }

    val res: MutableList<State> = mutableListOf()

    val moves = to4ways(st.c)
    val moveRes = moves.filter{ c2 ->
        inps.containsKey(c2) && inps[c2]!! != '#'
    }.map{ c2 ->
        State(c2, st.picos+1, st.cheated, st.cheatRemaining, listOf())
    }

    res.addAll(moveRes)

    if (!st.cheated) {
        // add a range of all cheat jumps
        val targetCs = inps
            .filter{ it.value != '#' }
            .filter { manhattanDist(it.key, st.c) <= st.cheatRemaining }
            .map{ it.key }
        val targetSts = targetCs.map { c2 ->
            val dist = manhattanDist(c2, st.c)
            State(c2, st.picos + dist, true, -1, listOf(st.c, c2))
        }
        res.add(State(st.c, st.picos, true, st.cheatRemaining, listOf()))
        res.addAll(targetSts)
    }

    return res
}

fun manhattanDist(c1: Coord2, c2: Coord2): Long {
    return abs(c1.x-c2.x) + abs(c1.y-c2.y)
}

fun runFor(inps: Map<Coord2, Char>, s: Coord2, e: Coord2, totalCheats: Int): List<State> {


    val st0 = State(s, 0L, false, totalCheats, listOf())

    val endState = Graph<State>().TraverseWithIds(
        starts = listOf(st0),
        getNeighbours = { st -> getNeighbours(st, inps) },
        getScore = {  st -> st.picos },
        getId = { st -> Triple(st.c, st.cheatList, st.cheated) }
    ).filter{
        st ->st.c == e
    }
        .toList()

    return endState
}

fun part1(): Long {
    val inps = parseInput().m

    val s = inps.filter{it.value == 'S'}.toList().first().first
    val e = inps.filter{it.value == 'E'}.toList().first().first

    cacheDists(inps, e)

    val res0 = runFor(inps, s, e, 0).first().picos

    val resRlist = runFor(inps, s, e, 2)
        .filter{it.picos <= res0 - 100}.toList()

    val resR = resRlist.size
    return resR.toLong()
}


fun part2(): Long {
    val inps = parseInput().m

    val s = inps.filter{it.value == 'S'}.toList().first().first
    val e = inps.filter{it.value == 'E'}.toList().first().first

    cacheDists(inps, e)

    val res0 = runFor(inps, s, e, 0).first().picos

    val resRlist = runFor(inps, s, e, 20)
        .filter{it.picos <= res0 - 100}.toList()

    val resR = resRlist.size
    return resR.toLong()
}

