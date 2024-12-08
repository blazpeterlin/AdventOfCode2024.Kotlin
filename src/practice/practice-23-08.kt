package practice_23_08

import common.chineseRemainderWithStarts
import java.math.BigInteger


data class Input(val ins: List<Char>, val mappings: List<Mapping>)
data class Mapping(val from: String, val left: String, val right: String)

fun parseInput(): Input {
    val r =
        common.Parsing().parseLns("practice")
            .filter { it.isNotEmpty() }
            .toList()

    val ins = r[0].toCharArray().toList()
    val mappings = r.drop(1)
        .map{ it.split(Regex("[ =(),]")).filter{ x -> x.isNotEmpty()}}
        .map{ (a,b,c) -> Mapping(a,b,c)}

    return Input(ins, mappings.toList())
}

fun step (singleIns: Char, m: Mapping): String {
    return if (singleIns == 'L') { m.left } else { m.right }
}

fun part1(): Long {
    val inp = parseInput()

    val mapDict: Map<String, Mapping> = inp.mappings.associateBy { it.from }
    val insInf = generateSequence(0) { it+1 }.map { inp.ins[it % inp.ins.size] }

    val insState = insInf.scan("AAA", { acc, singleIns -> step(singleIns, mapDict[acc]!!) })

    val statesIndexed = insState.mapIndexed { idx, state -> Pair(idx, state) }
    val finalState = statesIndexed.first { it.second == "ZZZ" }
    val numSteps = finalState.first

    return numSteps.toLong()
}

data class ModuloMatch(val offset: Long, val mod: Long)

fun part2(): BigInteger {
    val inp = parseInput()

    val mapDict: Map<String, Mapping> = inp.mappings.associateBy { it.from }
    val insInf = generateSequence(0) { it+1 }.map { inp.ins[it % inp.ins.size] }

    val starts = mapDict.keys.filter { it.endsWith('A') }
    val listMM = starts.map { start ->
        val insState = insInf.scan(start, { acc, singleIns -> step(singleIns, mapDict[acc]!!) })


        val statesIndexed = insState.mapIndexed { idx, state -> Pair(idx, state) }
        val wut = statesIndexed.take(10).toList()
        val (firstOffset, secondOffset) = statesIndexed.filter { it.second.endsWith("Z") }.take(2).map{ it.first }.toList()
        ModuloMatch(firstOffset.toLong(), secondOffset.toLong() - firstOffset.toLong())
    }.toList()

    val res = chineseRemainderWithStarts(listMM.map{ it.offset }, listMM.map{ it.mod })

    // 10018053955852106556583608 too high

    return res
}