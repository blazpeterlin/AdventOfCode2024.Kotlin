package finished.day24

import kotlin.random.Random

data class RegVal(val name:String, val v: Boolean)
data class Gate(val a:String, val b: String, val op: String, val tgt: String)

data class Node(val name: String, val op: String, val ns: List<Node>)


fun parseInput(): Pair<List<RegVal>, List<Gate>> {
    val r1 =
        common.Parsing().parseLns("day24", "input.txt")
            .filter { it.isNotEmpty() }
            .map{it.split(Regex("[:\\s]")).filter{it.isNotEmpty()}}
            .map{ (a, b) -> RegVal(a, b == "1") }
            .toList()
    val r2 =
        common.Parsing().parseLns("day24", "input2.txt")
            .filter { it.isNotEmpty() }
            .map{it.split(Regex("[->\\s]")).filter{it.isNotEmpty()}}
            .map{ (a, op, b, tgt) -> Gate(a,b,op,tgt) }
            .toList()

    return Pair(r1,r2)
}


fun part1(): Long {
    val (regs,gates) = parseInput()

    val mrgs: MutableMap<String, Boolean> = regs.map{ it.name to it.v }.toMap().toMutableMap()
    var mgs = gates.toMutableList()


    val zs = runForInput(mrgs, mgs)

    val zVal = zs.fold(0L){acc, z -> acc*2 + (if(z) 1L else 0L)}

    return zVal
}

fun runForInput(mrgs: MutableMap<String, Boolean>, mgs: MutableList<Gate>): List<Boolean> {

    while(mgs.any()) {
        val mg = mgs.first { mg -> mrgs.containsKey(mg.a) && mrgs.containsKey(mg.b) }
        mgs.remove(mg)
        val a = mrgs[mg.a]!!
        val b = mrgs[mg.b]!!
        mrgs[mg.tgt] = when(mg.op) {
            "AND" -> a.and(b)
            "OR" -> a.or(b)
            "XOR" -> a.xor(b)
            else -> TODO("oops")
        }
    }

    val zs: List<Boolean> = mrgs.filter{ it.key.startsWith("z")}.toList().sortedByDescending { it.first }.map{it.second}.toList()

    return zs
}

fun getNum(s:String): Int {
    return s.filter{it.isDigit()}.toInt()
}

fun getType(s:String): String {
    return s.filter{ it.isLetter() }
}

fun part2(): Long {
    // manually check graph through graphforwiz.txt (manually prepared). Nothing else is worth a damn.
    return 0L
}

fun scoreBadBits(mgs: List<Gate>): Long {
    val mrgs: MutableMap<String, Boolean> = mutableMapOf()

    val rndGen = Random(0)

    var finalPenalty = 0L

    for(attempts in 1 .. 100) {
        val rndX = generateBits(rndGen, 45)
        val rndY = generateBits(rndGen, 45)
        val valX = rndX.reversed().fold(0L) { acc, b -> acc*2L+(if(b)1L else 0L) }
        val valY = rndY.reversed().fold(0L) { acc, b -> acc*2L+(if(b)1L else 0L) }
        var valZ = valX + valY
        val expectedBitsZ = generateSequence(valZ to 0L) {
                (ni, bit) -> if (ni==0L) null else
            ni/2L to (ni % 2L)
        }.drop(1)
            .map{ it.second==1L }
            .toMutableList()
        while(expectedBitsZ.size < 46) { expectedBitsZ.add(false) }

        for (i in 0..<45) {
            mrgs["x"+i.toString().padStart(2, '0')]=rndX[i]
            mrgs["y"+i.toString().padStart(2, '0')]=rndY[i]
        }

        val calculatedBitsZ = runForInput(mrgs, mgs.toMutableList())

        data class BitComparison(val idx: Int, val exp: Boolean, val calc: Boolean)
        val badBits = expectedBitsZ.zip(calculatedBitsZ).mapIndexed { index, (exp,calc) ->
            BitComparison(index,exp,calc)
        }.filter{ bc -> bc.exp!=bc.calc}
            .toList()

        finalPenalty += badBits.size
    }

    return finalPenalty
}

fun generateBits(rndGen: Random, numBits: Int): List<Boolean> {
    val bits: MutableList<Boolean> = mutableListOf()
    for(i in 1..numBits) {
        bits.add(rndGen.nextBoolean())
    }
    return bits
}

