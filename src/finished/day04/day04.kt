package finished.day04

import common.diagonalize
import common.extractImgs
import common.transpose

data class InputTerm(val levels: List<Long>)

fun parseInput(): List<String> {
    val inputTerms =
        common.Parsing().parseLns("day04")
            .filter { it.isNotEmpty() }

            .toList()

    return inputTerms
}

fun countXMAS(input: List<String>): Int {


    val xmases: Int = input
        .map{ ln -> ln.windowed(size = 4) }
        .flatten()
        .filter{ str -> str == "XMAS" || str == "SAMX" }
        .count()

    return xmases
}

fun part1(): Int {
    val input = parseInput()
    val inputT = transpose(input)
    val inputDg1 = diagonalize(input, true)
    val inputDg2 = diagonalize(input, false)

    val res1 = countXMAS(input)
    val res2 = countXMAS(inputT)
    val res3 = countXMAS(inputDg1)
    val res4 = countXMAS(inputDg2)


    return res1+res2+res3+res4
}

val acceptableResultsPart2 = listOf("""
        M M
         A 
        S S
    """.trimIndent(),"""
        M S
         A 
        M S
    """.trimIndent(),"""
        S S
         A 
        M M
    """.trimIndent(),"""
        S M
         A 
        S M
    """.trimIndent(),

    )

fun part2Accepts(img: String): Boolean {

    return acceptableResultsPart2.any{
        it.zip(img).all{ (a, b) ->
            a == b || a == ' '
        }
    }
}

fun part2(): Int {
    val inputTerms = parseInput()

    val imgs =
        extractImgs(inputTerms, 3, 3)
            .toList()


    val res = imgs.filter { img ->
        part2Accepts(img)
    }.count()

    // not 1847
    return res
}