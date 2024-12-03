package day03

data class InputTerm(val str: String)
data class MulTerm(val a: Int, val b: Int, val idx: Int)
data class DoDontTerm(val isDo: Boolean, val idx: Int)

fun parseInput(): InputTerm {
    val inStr: String =
        common.Parsing().parseTxt("day03")

    return InputTerm(inStr)
}

fun findMulTerms(inputStr: String): List<MulTerm> {

    var str = inputStr
    val res = mutableListOf<MulTerm>()

    while (str.any()) {
        val idxMul = str.indexOf("mul(")

        if (idxMul == -1) {break}

        str = str.substring(idxMul+4)


        val (wasValidNumA, numA, strAfterA) = eatNum(str)
        if (!wasValidNumA){continue}
        if (!strAfterA.startsWith(",")) {continue}

        val (wasValidNumB, numB, strAfterB) = eatNum(strAfterA.substring(1))
        if (!wasValidNumB){continue}
        if (!strAfterB.startsWith(")")) {continue}

        str = strAfterB.substring(1)

        res.add(MulTerm(numA, numB, inputStr.length - str.length - 1))
    }

    return res.toList()
}

fun findAllIndexes(str: String, substr: String): List<Int> {
    val indexes = mutableListOf<Int>()
    var index = str.indexOf(substr)

    while (index >= 0) {
        indexes.add(index)
        index = str.indexOf(substr, index + 1)
    }

    return indexes
}

fun findDosDonts(inputStr: String): List<DoDontTerm> {

    val dos = findAllIndexes(inputStr, "do()")
    val donts = findAllIndexes(inputStr, "don't()")

    val res = listOf(
        dos.map { DoDontTerm(true,  it) }.toList()
        , donts.map { DoDontTerm(false, it) }.toList()
        , listOf(DoDontTerm(true, -1))
    ).flatten().sortedBy { it.idx }


    return res
}

fun eatNum(str: String): Triple<Boolean, Int, String> {

    if (str[0].isDigit().not()) {return Triple(false, 0, str)}

    val aStr = str.takeWhile { it.isDigit() }
    val a = aStr.toInt()

    if (aStr.length > 3) { return Triple(false, 0, str) }

    return Triple(true, a, str.substring(aStr.length))
}


fun part1(): Int {
    val inputTerm = parseInput()

    val muls = findMulTerms(inputTerm.str)

    return muls.sumOf { it.a * it.b }
}

fun part2(): Int {
    val inputTerm = parseInput()

    val muls = findMulTerms(inputTerm.str)
    val dosDonts = findDosDonts(inputTerm.str)

    val filteredMuls = muls.filter { mul -> dosDonts.filter { dd -> dd.idx <= mul.idx }.last().isDo }
    return filteredMuls.sumOf { it.a * it.b }
}