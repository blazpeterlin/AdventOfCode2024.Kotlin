package practice_23_13

import common.Coord2
import common.ParsedMap
import common.parseMap
import common.printMap
import kotlin.math.min

data class Input(val arng: String, val constraint: List<Long>)

fun parseInput(): List<ParsedMap> {

    val acc0 = emptyList<MutableList<String>>().toMutableList()
    acc0.add(emptyList<String>().toMutableList())

    val r =
        common.Parsing().parseLns("practice")
            .fold(acc0) { acc, ln ->
                if (ln.isEmpty()) {
                    acc.add(emptyList<String>().toMutableList())
                    acc
                } else {
                    acc.last().add(ln)
                    acc
                }
            }
            .filter { it.isNotEmpty() }
            .map{ grp -> parseMap(grp) }
            .toList()

    return r
}

fun transpose(m: Map<Coord2, Char>): Map<Coord2, Char> {
    val r = m.map { entry -> Coord2(entry.key.y, entry.key.x) to entry.value }.toMap()
    return r
}

fun findMirrorIdx(m: Map<Coord2, Char>, numSmudges: Long): Long {
    val maxX = m.keys.map{ it.x }.max()
    val ys = m.keys.map{it.y}.distinct().sorted().toList()
    outerLoop@ for (i in 1..maxX) {
        val maxOffset = min(i-1, maxX-i)

        var smudgesRemaining = numSmudges

        for(offset in 0..maxOffset) {
            var x1 = i-1-offset
            val x2 = i+offset

            for(y in ys) {
                if (m[Coord2(x1,y)] != m[Coord2(x2,y)]) {
                    smudgesRemaining--

                    if (smudgesRemaining < 0L) {
                        continue@outerLoop
                    }
                }
            }

        }

        if (smudgesRemaining == 0L) {
            return i
        }
    }

    return -1L
}

fun score(m: Map<Coord2, Char>, numSmudges: Long): Long {
    val idxHorizontal = findMirrorIdx(m, numSmudges)
    val idxVertical = findMirrorIdx(transpose(m), numSmudges)

    if (idxHorizontal>=0) {
        return idxHorizontal
    }

    return 100L * idxVertical
}

fun part1(): Long {
    val inps = parseInput()

    val scores = inps.map { score(it.m, 0) }
    val res = scores.sum()

    return res
}

fun part2(): Long {
    val inps = parseInput()

    val scores = inps.map { score(it.m, 1) }
    val res = scores.sum()

    return res
}

