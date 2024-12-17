package day16

import common.*
import java.util.*

data class Input(val arng: String, val constraint: List<Long>)

fun parseInput(): ParsedMap {
    val r =
        Parsing().parseLns("day17")
            .filter { it.isNotEmpty() }
            .toList()

    return parseMap(r)
}

val LEFT = Coord2(-1,0)
val RIGHT = Coord2(1, 0)
val UP = Coord2(0, -1)
val DOWN = Coord2(0, 1)

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

data class State(val pos: Coord2, val dir: Coord2, val dist: Long, val pathBack: State?)

fun getNeighbours(m: Map<Coord2, Char>, st: State): List<Pair<State, Long>> {
    val l = State(st.pos, turnLeft(st.dir), st.dist+1000L, st)
    val r = State(st.pos, turnRight(st.dir), st.dist+1000L, st)
    val f = State(st.pos + st.dir, st.dir, st.dist+1L, st)
    val res = mutableListOf(Pair(l, 1000L),Pair(r, 1000L))
    if (m[f.pos] != '#') {
        res.add(Pair(f, 1L))
    }
    return res
}

fun part1(): Long {
    val m0 = parseInput().m


    val s0 = m0.filter{ kvp -> kvp.value == 'S' }.keys.first()
    val st0 = State(s0, RIGHT, 0L, null)

    val end = m0.filter{ kvp -> kvp.value == 'E' }.keys.first()

    val res = Graph<State>().TraverseWithIds(
        listOf(st0),
        getNeighboursCosts = { n -> getNeighbours(m0, n) },
        getScore = { x -> x.dist },
        getId = { st -> Pair(st.pos, st.dir) }
    ).filter{ st -> st.pos == end }
        .first()

    return res.dist
}


fun<T,U> traverseWithIds(starts: List<T>, getNeighboursCosts: (T)->List<Pair<T, Long>>, getScore: (T)->Long, getId: (T)->U)
    : Sequence<Pair<T, List<T>>> {
    val pq = PriorityQueue<GraphNode<T>>(compareBy { it.score })
    pq.addAll(starts.map{ s -> GraphNode(s, getScore(s))})

    val bestVisited = HashMap<U, MutableList<T>>()

    return sequence {
        while(pq.any()) {
            val node = pq.poll()
            val nodeId = getId(node.elt)
            if (bestVisited.containsKey(nodeId)) {
                if (getScore(bestVisited[nodeId]!!.first()) != getScore(node.elt)) {
                    continue
                }
//                continue
            }

            bestVisited[nodeId] = mutableListOf(node.elt)
            yield(Pair(node.elt, bestVisited[nodeId]!!))


            val ns = bestVisited[nodeId]!!.map{ n -> getNeighboursCosts(n) }.flatten().toList() // .filter{ (n, cost) -> !visited.contains(getId(n)) }


            pq.addAll(ns.map{ (n, cost) -> GraphNode(n, getScore(n))})
        }
    }
}


fun getAllCoords(it: Pair<State, List<State>>): List<Coord2> {
    var s: State? = it.first

    val res = mutableListOf(Coord2(0,0))
    res.clear()

    while(s != null) {
        res.add(s.pos)
        s = s.pathBack
    }

    return res
}

fun part2(): Long {
    val m0 = parseInput().m


    val s0 = m0.filter{ kvp -> kvp.value == 'S' }.keys.first()
    val st0 = State(s0, RIGHT, 0L, null)

    val end = m0.filter{ kvp -> kvp.value == 'E' }.keys.first()

    var best = -1L

    val resList = traverseWithIds(
        listOf(st0),
        getNeighboursCosts = { n -> getNeighbours(m0, n) },
        getScore = { x -> x.dist },
        getId = { st -> Pair(st.pos, st.dir) }
    ).filter{ st -> st.first.pos == end }
        .takeWhile { st ->
            if (best == -1L) {
                best = st.first.dist
            }

            st.first.dist == best
        }
        .toList()

    val allCoords = resList.map{ getAllCoords(it) }.flatten().distinct()
    val res = allCoords.size.toLong()
    return res
}

