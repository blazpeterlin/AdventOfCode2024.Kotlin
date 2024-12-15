package finished.day13_again

import common.Coord2
import common.Graph
import common.Parsing
import common.plus
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.roundToLong

data class Input(val buttonA: Coord2, val buttonB: Coord2, val target: Coord2)

fun parseInput(): List<Input> {
    val r: List<Input> =
        Parsing().parseLns("finished/day13")
            .filter { it.isNotEmpty() }
            .map { ln -> ln.split(Regex("[\\s+,AB:=XYutonPrize]"))
                            .filter{it.isNotEmpty()}
                            .map{ n->n.toLong() }}
            .map{ (x,y) -> Coord2(x,y) }
            .chunked(3)
            .map{chnk -> Input(chnk[0], chnk[1], chnk[2])}
            .toList()
    return r;
}

data class Solution(val a: Long, val b: Long)

fun getSolution(inp: Input): Solution? {
    val ax = inp.buttonA.x
    val ay = inp.buttonA.y
    val bx = inp.buttonB.x
    val by = inp.buttonB.y
    val xR = inp.target.x
    val yR = inp.target.y

    // A*ax*ay + B*bx*ay = xR*ay
    // A*ay*ax + B*by*ax = yR*ax
    // B(bx*ay-by*ax)=xR*ay-yR*ax
    // B = (xR*ay-yR*ax) / (bx*ay-by*ax)

    val resB = (xR*ay - yR*ax) / (bx*ay - by*ax)
    val resA = (xR - resB * bx) / ax

    if (resA * ax + resB * bx == xR
        && resA * ay + resB * by == yR) {
        return Solution(resA, resB)
    } else {
        return null
    }
}

fun part1(): Long {
    val inps = parseInput()

    // a*94 + b*22 = 8400
    // a*34 + b*67 = 5400

    // a*94*34 + b*22*34 = 8400*34
    // a*34*94 + b*67*94 = 5400*94
    // b * ? = !

    val solutions = inps.map { inp -> getSolution(inp) }.toList()
    val res = solutions.filter{ sln -> sln != null }.sumOf { sln -> sln!!.a * 3L + sln.b * 1L }

    return res
}

fun part2(): Long {
    val inps = parseInput().map{ inp -> Input(
        buttonA = inp.buttonA,
        buttonB = inp.buttonB,
        target = inp.target + Coord2(10000000000000L,10000000000000L)
    ) }.toList()

    val solutions = inps.map { inp -> getSolution(inp) }.toList()
    val res = solutions.filter{ sln -> sln != null }.sumOf { sln -> sln!!.a * 3L + sln.b * 1L }

    return res
}