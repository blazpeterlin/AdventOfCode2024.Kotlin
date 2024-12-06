package practice_23_04

import common.Coord2
import common.GrabbedNumber
import common.grabNumbers
import common.to8ways
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.streams.toList

//

data class InputTerm(val w: List<Long>, val n: List<Long>)

fun parseInput(): List<InputTerm>  {
    val inputLns = common.Parsing().parseLns("practice").filter { it.isNotEmpty() }

    val ts = inputLns.map{ ln -> ln.split(Regex("[:|]")) }
        .map{ (_, ws, ns) -> InputTerm(grabNumbers(ws).map{ it.num }.toList(), grabNumbers( ns).map{ it.num }.toList()) }

    return ts

}


fun part1(): Int {
    val inputTerms = parseInput()

    val r =  inputTerms.map{ t -> t.w.intersect(t.n).count() }
        .map { n ->
            when(n)  {
                0 -> 0
                1 -> 1
                else -> 2.toDouble().pow(n.toDouble()-1).roundToInt()
            }
        }.toList()

    return r.sum()
}


fun part2(): Long {
    val inputTerms = parseInput()

    val r =  inputTerms.map{ t -> t.w.intersect(t.n).count() }
        .map { n ->
            when(n)  {
                0 -> 0
                else -> n
            }
        }.toList()

    val totalCards = List(r.size) { 1L }.toMutableList()
    for (i in 0..r.size-1) {
        if (r[i] > 0) {
            for(j in i+1..i+r[i]) {
                if (j < totalCards.size) {
                    totalCards[j] += totalCards[i]
                }
            }
        }
    }

    return totalCards.sum()
}