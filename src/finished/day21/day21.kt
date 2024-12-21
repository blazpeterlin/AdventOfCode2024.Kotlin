package finished.day21

import common.*

fun parseInput(): List<String> {
    val r =
        Parsing().parseLns("day21", "input.txt")
            .filter { it.isNotEmpty() }
            .toList()

    return r
}

enum class RTy { Dir, Num }
data class State(val r: RobotBU, val path: String, val output: String)
data class RobotBU(val pos: Coord2, val ty: RTy, val upR: RobotBU?)

val d_c2ch = listOf(
    Coord2(1,0) to '^',
    Coord2(2,0) to 'A',
    Coord2(0,1) to '<',
    Coord2(1,1) to 'v',
    Coord2(2,1) to '>',
)
//
    .toMap()
val d_ch2c = d_c2ch.map{it.value to it.key}.toMap()

val n_c2ch = listOf(
    Coord2(0,0) to '7',
    Coord2(1,0) to '8',
    Coord2(2,0) to '9',
    Coord2(0,1) to '4',
    Coord2(1,1) to '5',
    Coord2(2,1) to '6',
    Coord2(0,2) to '1',
    Coord2(1,2) to '2',
    Coord2(2,2) to '3',
    Coord2(1,3) to '0',
    Coord2(2,3) to 'A',
)
    .toMap()

val n_ch2c = n_c2ch.map{it.value to it.key}.toMap()

val moves = listOf(
    '<' to Coord2(-1, 0),
    '>' to Coord2(+1, 0),
    'v' to Coord2(0, +1),
    '^' to Coord2(0, -1),
    ).toMap()


data class RobotMoveState(val c: Coord2, val path: String)

fun getRobotMoveNbrs(st: RobotMoveState, c2ch: Map<Coord2, Char>): List<RobotMoveState> {
    val candidates = listOf('<','v','^','>').map{ mv -> Pair(mv, moves[mv]!! + st.c) }
    val properMoves = candidates.filter{it -> c2ch.containsKey(it.second)}
    val res = properMoves.map{ RobotMoveState(it.second,st.path + it.first) }
    return res
}

fun getShortestMoves(r: RobotBU, ch: Char): List<String> {
    val ch2c = if(r.ty == RTy.Dir) d_ch2c else n_ch2c
    val c2ch = if(r.ty == RTy.Dir) d_c2ch else n_c2ch

    val finalPos = ch2c[ch]!!

    var bestLen: Long? = null
    val traversals = Graph<RobotMoveState>().TraverseWithIds(
        starts = listOf(RobotMoveState(r.pos, "")),
        getNeighbours = { st -> getRobotMoveNbrs(st, c2ch) },
        getScore = { st -> st.path.length.toLong() },
        getId = { st -> st }
    ).filter{ it.c == finalPos }
        .takeWhile{
            bestLen = bestLen ?: it.path.length.toLong()
            it.path.length.toLong() == bestLen
        }
        .toList()

    val res = traversals.map{ it.path }.toList()

    return res
}

data class RobotPath(val str: String, val len: Long) {
    fun increasePath(other: RobotPath): RobotPath {
        return RobotPath(/*str + other.str*/ "", len+other.len)
//        return RobotPath(str + other.str, len+other.len)
    }
}


val cache: MutableMap<Pair<String, RobotBU>, RobotPath> = mutableMapOf()
fun solveRobotString(req: String, initialR: RobotBU): RobotPath {
    val cached = cache[Pair(req, initialR)]
    if (cached != null) {
        return cached
    }

    val ch2c = if(initialR.ty == RTy.Dir) d_ch2c else n_ch2c

    var res = RobotPath("", 0)
    var movedR = initialR
    for(ch in req) {
        val chCoord = ch2c[ch]!!
        val shortestMoves: List<String> = getShortestMoves(movedR, ch)
        movedR = RobotBU(chCoord, initialR.ty, initialR.upR)

        if (movedR.upR == null) {
            val fst = shortestMoves.first()
            res = res.increasePath(RobotPath(fst, fst.length.toLong())).increasePath(RobotPath("A", 1))
            continue
        }

        val shortestInner =
            shortestMoves
                .map{ sm -> solveRobotString(sm+"A", movedR.upR!!) }
                .sortedBy { it.len }
                .first()

        res = res.increasePath(shortestInner)
    }

    cache[Pair(req, initialR)]=res
    return res
}

fun part1(): Long {
    val inps = parseInput()

    val startD = Coord2(2,0)
    val startN = Coord2(2,3)

    val robotD2 = RobotBU(startD, RTy.Dir, null)
    val robotD1 = RobotBU(startD, RTy.Dir, robotD2)
    val robotN = RobotBU(startN, RTy.Num, robotD1)

    val listRes: MutableList<Pair<Long, Long>>  = mutableListOf()
    for ( req in inps) {

        val resStr: RobotPath = solveRobotString(req, robotN)
        val reqNum = req.filter{ it.isDigit() }.toLong()
        listRes.add(Pair(resStr.len.toLong(), reqNum))
    }

    val resL = listRes.map{ it.first * it.second }
    val res = resL.sum()
    return res
}

fun part2(): Long {
    val inps = parseInput()

    val startD = Coord2(2,0)
    val startN = Coord2(2,3)

    var latestDirRobot = RobotBU(startD, RTy.Dir, null)
    for(i in 1..<25){
        latestDirRobot = RobotBU(startD, RTy.Dir, latestDirRobot)
    }
    val robotN = RobotBU(startN, RTy.Num, latestDirRobot)

    val listRes: MutableList<Pair<Long, Long>>  = mutableListOf()
    for ( req in inps) {

        val resStr: RobotPath = solveRobotString(req, robotN)
        val reqNum = req.filter{ it.isDigit() }.toLong()
        listRes.add(Pair(resStr.len.toLong(), reqNum))
    }

    val resL = listRes.map{ it.first * it.second }
    val res = resL.sum()
    return res
}

