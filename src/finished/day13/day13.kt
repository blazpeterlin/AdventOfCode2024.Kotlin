package finished.day13

import common.Coord2
import common.Graph
import common.Parsing
import common.plus
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.roundToLong

data class Input(val buttonA: Coord2, val buttonB: Coord2, val target: Coord2)
data class Move(val a: Long, val b: Long)
data class State(val pos: Coord2, val move: Move)

fun parseInput(): List<Input> {
    val r: List<Input> =
        Parsing().parseLns("day13")
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



fun part1(): Long {
    val inps = parseInput()

    val totalRes = mutableListOf<State>()
    for(inp in inps) {

        fun getNeighbours(state: State): List<Pair<State, Long>> {
//            if (state.move.a + state.move.b == 100L) { return emptyList<Pair<State, Long>>() }

            val r = listOf(
                Pair(State(
                    state.pos + inp.buttonA,
                    move = Move(state.move.a+1, state.move.b)
                ), 3L),

                Pair(State(
                    state.pos + inp.buttonB,
                    move = Move(state.move.a, state.move.b+1)
                ), 1L),
            ).filter{ x -> x.first.move.a <= 100L && x.first.move.b <= 100L}
            return r
        }


        val state0 = State(Coord2(0, 0), Move(0,0))
        val res = Graph<State>().Traverse(
            starts = listOf(state0),
            getNeighboursCosts = { st -> getNeighbours(st) },
            getScore = { st -> st.move.a + st.move.b }
        )

        val fst = res.find { r ->
            r.pos == inp.target
        }

        if(fst != null) {
            totalRes.add(fst)
        }
    }



    return totalRes.sumOf { r -> r.move.a * 3 + r.move.b }
}

fun findRatio(inp: Input): BigDecimal {
    val y = inp.target.y.toDouble().toBigDecimal(MathContext.DECIMAL128)
    val len = inp.target.lenManhattan().toDouble().toBigDecimal(MathContext.DECIMAL128)
    val ratioTargetY = y.divide(len, MathContext.DECIMAL128)

    val ratioA = inp.buttonA.y.toBigDecimal().divide(inp.buttonA.lenManhattan().toBigDecimal(), MathContext.DECIMAL128)
    val ratioB = inp.buttonB.y.toBigDecimal().divide(inp.buttonB.lenManhattan().toBigDecimal(), MathContext.DECIMAL128)


    // x * ratioA + (1-x) * ratioB = ratioTarget
    // x * ratioA - x * ratioB = ratioTarget - ratioB
    // x = (ratioTarget - ratioB) / (ratioA - ratioB)

    return (ratioTargetY - ratioB).divide(ratioA - ratioB, MathContext.DECIMAL128)
}

data class Solution (val howManyA: Long, val howManyB: Long)

fun part2(): Long {
    val inps = parseInput().map{ inp -> Input(
        buttonA = inp.buttonA,
        buttonB = inp.buttonB,
        target = inp.target + Coord2(10000000000000L,10000000000000L)
    ) }.toList()

    val totalRes = mutableListOf<Solution>()
    for (inp in inps) {
        val ratioAunit = findRatio(inp)
        if (ratioAunit < BigDecimal.ZERO || ratioAunit > BigDecimal.ONE) { continue }

        val ratioBunit = BigDecimal.ONE - ratioAunit

        val howManyUnits = inp.target.lenManhattan().toDouble().toBigDecimal()
        val unitsA = howManyUnits * ratioAunit
        val unitsB = howManyUnits * ratioBunit

        val actualResA = unitsA.divide(inp.buttonA.lenManhattan().toBigDecimal(), MathContext.DECIMAL128).toDouble().roundToLong().toBigDecimal()
        val actualResB = unitsB.divide(inp.buttonB.lenManhattan().toBigDecimal(), MathContext.DECIMAL128).toDouble().roundToLong().toBigDecimal()

        val coordComponentA = Coord2((actualResA * inp.buttonA.x.toBigDecimal()).toDouble().roundToLong(), (actualResA * inp.buttonA.y.toBigDecimal()).toDouble().roundToLong())
        val coordComponentB = Coord2((actualResB * inp.buttonB.x.toBigDecimal()).toDouble().roundToLong(), (actualResB * inp.buttonB.y.toBigDecimal()).toDouble().roundToLong())
        val totalCoord = coordComponentA + coordComponentB

        if(totalCoord == inp.target) {
            totalRes.add(Solution(actualResA.toDouble().roundToLong(), actualResB.toDouble().roundToLong()))
        }

    }



    return totalRes.sumOf { r -> r.howManyA * 3 + r.howManyB }
}