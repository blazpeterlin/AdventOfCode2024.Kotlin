package finished.day14

import common.Coord2
import common.allPairs
import common.plus

data class Robot(val pos: Coord2, val v: Coord2)


fun parseInput(): List<Robot> {
    val r: List<Robot> =
        common.Parsing().parseLns("day14")
            .filter { it.isNotEmpty() }
            .map { ln -> ln.split(Regex("[pv=, ]")).filter{ it.isNotEmpty()}.map{ n->n.toLong() }}
            .map { (px,py,vx,vy) -> Robot(Coord2(px,py), Coord2(vx,vy)) }
            .toList()
    return r;
}

fun move(r: Robot, w: Long, h: Long): Robot {
    val nextPos = r.pos + r.v

    var nextX = nextPos.x
    var nextY = nextPos.y

    while(nextX >= w) {
        nextX -= w
    }
    while (nextX < 0) {
        nextX += w
    }

    while(nextY >= h) {
        nextY -= h
    }
    while (nextY < 0) {
        nextY += h
    }

    val nextPosReal = Coord2(nextX, nextY)
    return Robot(nextPosReal, r.v)
}

fun getQuadrant(r: Robot, w: Long, h: Long): Long? {
    val halfW = w/2
    val halfH = h/2

    if (r.pos.x == halfW || r.pos.y == halfH) {
        return null
    }

    val res = (if(r.pos.x > halfW) 2L else 0L) + (if(r.pos.y > halfH) 1L else 0L)
    return res
}

fun getSafetyFactor(quadrants: List<Long?>): Long {
    val qs = quadrants.filter{ it != null }.map { it!! }
    val grps = qs.groupBy { it }.map{ it.value.size.toLong() }

    val sf = grps.reduce{ a,b -> a*b }
    return sf
}

fun part1(): Long {
    val inps = parseInput()
//
//    val w = 11L
//    val h = 7L
    val w = if (inps.maxOf{ it.pos.x } > 11L) 101L else 11L
    val h = if (inps.maxOf{ it.pos.y } > 7L) 103L else 7L

    val numSteps = 100

    val rsFinal = inps.map{ robot ->
        (1..numSteps).fold(robot){ acc, _ ->
            move(acc, w, h)
        }
    }

    val quadrants = rsFinal.map{ r -> getQuadrant(r,w,h) }


    val res = getSafetyFactor(quadrants)
    return res
}


fun printRobots(rs: List<Robot>, w: Long, h: Long) {
    val robotCoords = rs.map{ it.pos }.toSet()
    for (y in 0..<h) {
        for(x in 0..<w) {
            if (robotCoords.contains(Coord2(x,y))) {
                print("█")
            } else {
                print(" ")
            }
        }
        println()
    }
}

fun printRobotsSmart(rs: List<Robot>, w: Long, h: Long) {
    val robotCoords = rs.map{ it.pos }.toSet()

    val nonLonelyRobotCoords = robotCoords.filter { rc ->
        val candidateCoordsX = (rc.x - 10) .. (rc.x + 10)
        val candidateCoordsY = (rc.y - 10) .. (rc.y + 10)

        val neighbourRobots = allPairs(candidateCoordsX.toList(), candidateCoordsY.toList())
            .filter{ (x,y) -> robotCoords.contains(Coord2(x,y))}
            .count()

        neighbourRobots > 20
    }

    val minH = nonLonelyRobotCoords.minOf{ it.y }
    val maxH = nonLonelyRobotCoords.maxOf{ it.y }

    val minW = nonLonelyRobotCoords.minOf{ it.x }
    val maxW = nonLonelyRobotCoords.maxOf{ it.x }

    for (y in minH..<maxH) {
        for(x in minW..<maxW) {
            if (nonLonelyRobotCoords.contains(Coord2(x,y))) {
                print("█")
            } else {
                print(" ")
            }
        }
        println()
    }
}

fun part2(): Long {
    val inps = parseInput()

    val w = if (inps.maxOf{ it.pos.x } > 11L) 101L else 11L
    val h = if (inps.maxOf{ it.pos.y } > 7L) 103L else 7L

    var stepRobots = inps
    for(step in 1..999999) {
        stepRobots = stepRobots.map{ r -> move(r, w, h) }.toList()

        if (stepRobots.map{it.pos}.distinct().size != stepRobots.size) {
            continue
        }


        printRobotsSmart(stepRobots, w, h)
        // manually check printout with breakpoint below
        val x = 1
    }
    return 0L
}