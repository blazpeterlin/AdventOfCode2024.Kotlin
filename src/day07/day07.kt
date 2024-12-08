package day07

import kotlin.math.roundToLong

data class Input(val ns: List<Long>)

fun parseInput(): List<Input> {
    val r =
        common.Parsing().parseLns("day07")
            .filter { it.isNotEmpty() }
            .map { it.split(Regex("[\\s:]")).filter{ x -> x.isNotEmpty()} }
            .map { Input(it.map{ x -> x.toLong() }.toList()) }
            .toList()
    return r
}

fun possibleCalculations(includeConcat: Boolean, ns: List<Long>) : List<Long> {
    if (ns.size <= 1) { return ns }

    val inner = possibleCalculations(includeConcat, ns.dropLast(1).toList())

    val lastLen = ns.last().toString().length

    val res =
        inner.map{ inn -> inn + ns.last() }
            .union(inner.map{ inn -> inn * ns.last() })
            .union(if (includeConcat) {
                val multiplier = Math.pow(10.0, lastLen.toDouble()).roundToLong()
                inner.map { inn -> multiplier*inn + ns.last() }
            }
            else listOf())
            .toList()

    return res
}

fun part1(): Long {
    val inp = parseInput()

    val valid = inp.filter{ possibleCalculations(false, it.ns.drop(1)).contains(it.ns[0]) }

    return valid.map{ v -> v.ns[0] }.sum()
}


fun part2(): Long {
    val inp = parseInput()

    val valid = inp.filter{ possibleCalculations(true, it.ns.drop(1)).contains(it.ns[0]) }

    return valid.map{ v -> v.ns[0] }.sum()
}