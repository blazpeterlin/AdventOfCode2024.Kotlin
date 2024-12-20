package practice_23_18

import common.Coord2
import common.Parsing
import common.plus
import kotlin.math.absoluteValue
import kotlin.math.roundToLong

data class Instruction(val move: Char, val len: Long, val color: String)

fun parseInput1(): List<Instruction> {
    val r =
        Parsing().parseLns("practice")
            .filter { it.isNotEmpty() }
            .map{it.split(Regex("[\\s()]")).filter{it.isNotEmpty()}}
            .map{ (a, b, c) -> Instruction(a[0], b.toLong(), c) }
            .toList()

    return r
}
fun parseInput2(): List<Instruction> {
    val r =
        Parsing().parseLns("practice")
            .filter { it.isNotEmpty() }
            .map{it.split(Regex("[\\s()#]")).filter{it.isNotEmpty()}}
            .map{ (a, b, c) -> Instruction(moveFromChar(c.last()), fromHex(c.substring(0, 5)), c) }
            .toList()

    return r
}

fun moveFromChar(last: Char): Char {
    val r = when(last) {
        '0' -> 'R'
        '1' -> 'D'
        '2' -> 'L'
        '3' -> 'U'
        else -> TODO("nop")
    }
    return r
}

fun fromHex(substring: String): Long {
    return substring.toLong(radix = 16)
}

fun polygonAreaTiled(loop: List<Coord2>, includeOuter: Boolean): Long {

    val loopEnclosed = loop.toMutableList()
    loopEnclosed.add(loop[0])

    val crossProducts = loopEnclosed
        .windowed(2)
        .map { (a, b) -> a.x * b.y - b.x * a.y }

    val realWorldArea = crossProducts.sum().absoluteValue / 2L
    val adjustAreaBy =
        +1L + (if(includeOuter) +1 else -1) * loopEnclosed
            .windowed(2)
            .map{ (a, b) -> (a.x - b.x).absoluteValue / 2.0 + (a.y - b.y).absoluteValue / 2.0 }
            .sum()
            .roundToLong()

    return realWorldArea + adjustAreaBy
}

fun move(ch: Char, howFar: Int): Coord2 {
    val move1 = when(ch) {
        'L' -> Coord2(-1,0)
        'R' -> Coord2(+1,0)
        'U' -> Coord2(0, -1)
        'D' -> Coord2(0, +1)
        else -> TODO("oopsMove1")
    }
    return move1 * howFar
}

fun part1(): Long {
    val inps = parseInput1()

    val coords = inps.scan(Coord2(0,0)){ acc: Coord2, instruction: Instruction ->
        acc + move(instruction.move, instruction.len.toInt())
    }.toList()

    val res = polygonAreaTiled(coords, includeOuter = true)

    return res
}

fun part2(): Long {
    val inps = parseInput2()

    val coords = inps.scan(Coord2(0,0)){ acc: Coord2, instruction: Instruction ->
        acc + move(instruction.move, instruction.len.toInt())
    }.toList()

    val res = polygonAreaTiled(coords, includeOuter = true)

    return res
}

