package practice_23_16

import common.Coord2
import common.parseMap
import common.plus

data class Input(val arng: String, val constraint: List<Long>)

fun parseInput(): Map<Coord2, Char> {
    val r =
        common.Parsing().parseLns("practice")
            .filter { it.isNotEmpty() }
            .toList()

    return parseMap(r).m
}

data class State(val c:Coord2, val dir: Coord2)

val UP = Coord2(0, -1)
val DOWN = Coord2(0, +1)
val LEFT = Coord2(-1, 0)
val RIGHT = Coord2(+1, 0)

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

fun step(st: State, m: Map<Coord2, Char>): List<State> {
    val c2 = st.c + st.dir

    if (!m.containsKey(c2)) { return listOf() }

    val dir = st.dir
    val nextDirs = when(m[c2]!!) {
        '.' -> listOf(dir)
        '|' -> if (dir.x != 0L) listOf(UP, DOWN) else listOf(dir)
        '-' -> if (dir.y != 0L) listOf(LEFT, RIGHT) else listOf(dir)
        '\\' -> listOf(when(dir) { RIGHT -> DOWN; UP -> LEFT; DOWN -> RIGHT; LEFT -> UP; else -> TODO("\\")  })
        '/' -> listOf(when(dir) { RIGHT -> UP; UP -> RIGHT; DOWN -> LEFT; LEFT -> DOWN; else -> TODO("/")  })
        else -> TODO("faildir")
    }

    return nextDirs.map{nextDir -> State(c2, nextDir) }.toList()
}

fun getEnergized(st0: State, inps: Map<Coord2, Char>): Long {
    val s = mutableSetOf(st0)
    var edge = s.toList()
    while(true) {
        val newS = edge.map{ step(it, inps) }.flatten()

        edge = newS.filter{ !s.contains(it) }.toList()
        s.addAll(edge)

        if (!edge.any()) { break }
    }

    val cs = s.map{it.c}.distinct().filter{ inps.containsKey(it) }.toList()
    val res =cs.size
    return res.toLong()
}

fun part1(): Long {
    val inps = parseInput()

    val st0 = State(Coord2(-1,0), Coord2(1, 0))

    val res = getEnergized(st0, inps)
    return res
}

fun part2(): Long {
    val inps = parseInput()

    val maxX = inps.keys.maxOf{it.x}
    val maxY = inps.keys.maxOf{it.y}

    var best = 0L
    for(x in 0 .. maxX) {
        val st0 = State(Coord2(x,-1), Coord2(0, 1))
        val res = getEnergized(st0, inps)
        if (res > best) {
            best = res
        }
    }
    for(x in 0 .. maxX) {
        val st0 = State(Coord2(x,maxY+1), Coord2(0, -1))
        val res = getEnergized(st0, inps)
        if (res > best) {
            best = res
        }
    }
    for(y in 0 .. maxY) {
        val st0 = State(Coord2(-1,y), Coord2(1, 0))
        val res = getEnergized(st0, inps)
        if (res > best) {
            best = res
        }
    }
    for(y in 0 .. maxY) {
        val st0 = State(Coord2(maxX+1,y), Coord2(-1, 0))
        val res = getEnergized(st0, inps)
        if (res > best) {
            best = res
        }
    }

    return best
}

