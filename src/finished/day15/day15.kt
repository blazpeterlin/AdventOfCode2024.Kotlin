package finished.day15

import common.*
import kotlin.math.min

data class Input(val map: ParsedMap, val instructions: String)


fun parseInput(): Input {
    val r =
        common.Parsing().parseLns("day15")
            .toList()

    val splitIdx = r.indexOf("")

    val mapLines = r.take(splitIdx)
    val insStr = r.drop(splitIdx+1).joinToString(separator = "")


    return Input(parseMap(mapLines), insStr);
}

fun parseInput2(): Input {
    val r =
        common.Parsing().parseLns("day15")
            .toList()

    val splitIdx = r.indexOf("")

    val mapLines = r.take(splitIdx)
    val insStr = r.drop(splitIdx+1).joinToString(separator = "")

    val mapLinesPart2 = mapLines.map { ln ->
        ln.toCharArray().map{ ch -> when(ch) {
            '#' -> "##"
            '.' -> ".."
            '@' -> "@."
            'O' -> "[]"
            else -> TODO("parse bug")
        } }.joinToString (separator = "")
    }

    return Input(parseMap(mapLinesPart2), insStr);
}



fun takeStepPart1(st: State, ins: Char) : State {

    val desiredMove = when(ins) {
        '<' -> Coord2(-1,0)
        '>' -> Coord2(1, 0)
        '^' -> Coord2(0, -1)
        'v' -> Coord2(0, 1)
        else -> TODO("mv")
    }

    val nextLoc = st.pos + desiredMove
    var checkLoc = nextLoc

    if (st.mm[checkLoc] == '#') {
        return st
    }
    while(st.mm[checkLoc] != '.') {
        checkLoc += desiredMove
        if (st.mm[checkLoc] == '#') {
            return st
        }
    }

    st.mm[nextLoc] = '.'
    if (nextLoc != checkLoc) {
        st.mm[checkLoc] = 'O'
    }

//    printMap(st.mm)

    return State(st.mm, nextLoc)
}

data class State(val mm: MutableMap<Coord2, Char>, val pos: Coord2)

fun part1(): Long {
    val inps = parseInput()

    val m = inps.map.m
    val pos0 = m.filter{ it.value == '@'}.map{ it.key}.first()

    val mm = m.toMutableMap()
    mm[pos0] = '.'

    val state0 = State(mm, pos0)
    val stateN = inps.instructions.fold(state0){ st, ins -> takeStepPart1(st, ins) }

    val res = stateN.mm.filter{ it.value == 'O' }.map{ it.key}.sumOf{ it.x + 100 * it.y }

    printMap(stateN.mm)

    return res.toLong()
}


val LEFT = Coord2(-1,0)
val RIGHT = Coord2(1, 0)
val UP = Coord2(0, -1)
val DOWN = Coord2(0, 1)

val prevRobotPositionsToDisplay = emptyList<Coord2>().toMutableList()
fun takeStepPart2(st: State, ins: Char) : State {

    val move = when (ins) {
        '<' -> LEFT
        '>' -> RIGHT
        '^' -> UP
        'v' -> DOWN
        else -> TODO("mv")
    }

    val nextLoc = st.pos + move
    var checkLocs = listOf(nextLoc)


    if (checkLocs.any { checkLoc -> st.mm[checkLoc] == '#' }) {
        return st
    }

    val mmPrev = st.mm.toMap()
    val mmNext = mmPrev.map{ it.key to it.value }.toMap().toMutableMap()

    val collision = checkLocs.any { checkLoc -> mmPrev[checkLoc] != '.' }
    while (checkLocs.any { checkLoc -> mmPrev[checkLoc] != '.' }) {

        val checkLocsNext = checkLocs.map {
            var res = mutableListOf(it)
            if (move == DOWN || move == UP) {
                if (mmPrev[it] == '[') {
                    res.add(it + RIGHT)
                } else if (mmPrev[it] == ']') {
                    res.add(it + LEFT)
                }
            }
            res
        }.flatten().distinct()

        val movingWall = checkLocsNext.filter{mmPrev[it] != '.'}
        for (cl in movingWall) {
            mmNext[cl + move] = mmPrev[cl]!!
            if (!checkLocs.contains(cl))
                mmNext[cl] = '.'
        }

        checkLocs = movingWall.map { cl -> cl + move }

        if (checkLocs.any { checkLoc -> mmPrev[checkLoc] == '#' }) {
            return st
        }
    }

    if (collision) {
        println(ins)

        mmNext[nextLoc] = '@'
        for(prv in prevRobotPositionsToDisplay) {
            if (mmNext[prv] == '.') {
                mmNext[prv] = 'o'
            }
        }
//        printMap(mmNext)
        mmNext[nextLoc] = '.'

        for(prv in prevRobotPositionsToDisplay) {
            if (mmNext[prv] == 'o') {
                mmNext[prv] = '.'
            }
        }
        prevRobotPositionsToDisplay.clear()
    } else {
        prevRobotPositionsToDisplay.add(nextLoc)
        print(ins)
    }

    return State(mmNext, nextLoc)
}



fun part2(): Long {
    val inps = parseInput2()

    val m = inps.map.m
    val pos0 = m.filter{ it.value == '@'}.map{ it.key}.first()

    val mm = m.toMutableMap()
    mm[pos0] = '.'

    val state0 = State(mm, pos0)
    val stateN = inps.instructions.fold(state0){ st, ins -> takeStepPart2(st, ins) }

    val res = stateN.mm.filter{ it.value == '[' }.map{ it.key }.sumOf {
        val c1 = it
        val c2 = it + RIGHT

        val dist1 = getDistance(m, c1)
        val dist2 = getDistance(m, c2)

        min(dist1, dist2)
    }

    printMap(stateN.mm)

    return res.toLong()
}

fun getDistance(m: Map<Coord2, Char>, c: Coord2): Long {

    val closestEdgeDelta = getClosestEdgeDelta(m, c)
    val res = closestEdgeDelta.x + 100*closestEdgeDelta.y
    return res
}

fun getClosestEdgeDelta(m: Map<Coord2, Char>, it: Coord2): Coord2 {
    val minX = m.minOf{it.key.x}
//    val maxX = m.maxOf{it.key.x}
    val minY = m.minOf{it.key.y}
//    val maxY = m.maxOf{it.key.y}
//
//    val dx = min(abs(it.x - minX), abs(it.x - maxX))
//    val dy = min(abs(it.y - minY), abs(it.y - maxY))
//
//    return Coord2(dx,dy)

    return Coord2(it.x-minX, it.y-minY)
}
