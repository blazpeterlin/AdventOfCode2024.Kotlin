package day05

import common.permutations
import java.lang.Thread.yield

data class InputOrdering(val n1: Long, val n2: Long)
data class InputPrint(val ns: List<Long>)

fun parseInput(): Pair<List<InputOrdering>, List<InputPrint>> {
    val r =
        common.Parsing().parseLns("day05")
            .filter { it.isNotEmpty() }
            .toList()

    val orderings = r.filter{ ln -> ln.indexOf ( "|" ) > 0}
    val prints = r.filter{ ln -> ln.indexOf ( "|" ) == -1}

    val inOrderings: List<InputOrdering> = orderings.map{ ln -> ln.split("|") }
        .map { (a, b) -> InputOrdering(a.toLong(), b.toLong())}
        .toList()
    val inPrints: List<InputPrint> = prints
        .map{ ln -> ln.split(",").map{ x -> x.toLong() }.toList() }
        .map { x -> InputPrint(x) }
        .toList()

    return Pair(inOrderings, inPrints)
}

fun getLaterPairs(ns: List<Long>): List<Pair<Long, Long>> {
    val r2 = sequence {
        for (i in 0..<ns.size) {
            for (j in i+1..<ns.size) {
                val yr = Pair(ns[i], ns[j])

                yield(yr)
            }
        }
    }

    return r2.toList()
}

fun part1(): Long {
    val (ord, pri) = parseInput()

    val ordMap: Map<Long, Set<Long>> =
        ord.groupBy { (a, b) -> a }
            .map { grp -> grp.key to grp.value.map{ (a, b) -> b }.toList().toSet() }
            .toMap()
    val goodPrints = pri
        .filter{ p -> getLaterPairs(p.ns).all {
            (a, b) -> ordMap[b]?.contains(a) != true
        }}

    val goodPrintCenters = goodPrints.map {
        it.ns[it.ns.size/2]
    }.toList()


    return goodPrintCenters.sum()
}


fun part2(): Long {
    val (ord, pri) = parseInput()

    val ordMap: Map<Long, Set<Long>> =
        ord.groupBy { (a, b) -> a }
            .map { grp -> grp.key to grp.value.map{ (a, b) -> b }.toList().toSet() }
            .toMap()

    fun findBadPrint(ns: List<Long>): Pair<Long, Long>? {
        return getLaterPairs(ns).find {
                (a, b) -> ordMap[b]?.contains(a) == true
        }
    }
    fun isBadPrint(ns: List<Long>): Boolean {
        return findBadPrint(ns) != null
    }
    val badPrints = pri.map{ p -> p.ns }.filter{ isBadPrint(it) }

    val correctedPrints: List<List<Long>> = badPrints.map { bp ->
            val iteratedBP = bp.toMutableList();
            while(isBadPrint(iteratedBP)) {
                val (prevN, nextN) = findBadPrint(iteratedBP)!!
                iteratedBP.remove(nextN)
                iteratedBP.add(iteratedBP.indexOf(prevN), nextN)
            }

            iteratedBP
        }
        .toList()

    val goodPrintCenters = correctedPrints.map {
        ns -> ns[ns.size/2]
    }.toList()


    return goodPrintCenters.sum()
}