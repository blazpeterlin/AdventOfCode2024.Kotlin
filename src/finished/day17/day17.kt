package finished.day17

import common.Parsing
import kotlin.math.roundToInt


fun parseInput(): State {
    val r =
        Parsing().parseLns("day17")
            .filter { it.isNotEmpty() }
            .toList()

    val rs = r.take(3).map{it.split(' ').last().toLong()}

    val ins = r.last().split(' ').last().split(',').map{it.toLong()}
    val regs = mutableMapOf('A' to rs[0], 'B' to rs[1], 'C' to rs[2])

    val res = State(
        pointer = 0,
        ins = ins,
        regs = regs,
        halt = false,
        output = mutableListOf()
    )
    return res
}

data class State(val pointer: Int, val ins: List<Long>, val regs: MutableMap<Char, Long>, val halt: Boolean, val output: MutableList<Long>)


enum class OperandType { LITERAL, COMBO }

fun op(ot: OperandType, v: Long, regs: Map<Char, Long>): Long {
    if (ot == OperandType.LITERAL) {
        return v
    }
    else {
        val r: Long = when (v) {
            0L -> 0L
            1L -> 1L
            2L -> 2L
            3L -> 3L
            4L -> regs['A']!!
            5L -> regs['B']!!
            6L -> regs['C']!!
            7L -> TODO("oops")
            else -> TODO("huh")
        }

        return r
    }
}
fun step(st: State): State {
    if (st.pointer < 0L || st.pointer >= st.ins.size) {
        return State(pointer = st.pointer, ins = st.ins, regs = st.regs, halt = true, st.output)
    }

    val ptr = st.pointer
    val pval = st.ins[ptr]
    val r = st.regs
    val oprnd: Long = st.ins[ptr+1]!!
    val nextPointer: Int? =
        when {
            // adv
            pval == 0L -> {
                val nextA = r['A']!! / (Math.pow(2.0, op(OperandType.COMBO, oprnd, r).toDouble()).roundToInt())
                r['A'] = nextA
                null
            }

            // bxl
            pval == 1L -> {
                r['B'] = r['B']!!.xor(op(OperandType.LITERAL, oprnd, r))
                null
            }

            // bst
            pval == 2L -> {
                r['B'] = op(OperandType.COMBO, oprnd, r) % 8L
                null
            }

            // jnz
            pval == 3L -> {
                if (r['A']!! == 0L)
                    null
                else {
                    val jumpTo = op(OperandType.LITERAL, oprnd, r).toInt()
                    jumpTo
                }
            }

            // bxc
            pval == 4L -> {
                r['B'] = r['B']!!.xor(r['C']!!)
                null
            }

            // out
            pval == 5L -> {
                val comboVal = op(OperandType.COMBO, oprnd, r)%8L
                st.output.add(comboVal)
                null
            }

            // bdv
            pval == 6L -> {
                val nextA = r['A']!! / (Math.pow(2.0, op(OperandType.COMBO, oprnd, r).toDouble()).roundToInt())
                r['B'] = nextA
                null
            }

            // cdv
            pval == 7L -> {
                val nextA = r['A']!! / (Math.pow(2.0, op(OperandType.COMBO, oprnd, r).toDouble()).roundToInt())
                r['C'] = nextA
                null
            }

            else -> null
        }


    val next = State(pointer = (nextPointer ?: (ptr + 2)), ins = st.ins, regs = st.regs, halt = false, output = st.output)

    return next
}

fun part1(): String {
    val st0 = parseInput()

    var st = st0
    while(!st.halt) {
        st = step(st)
    }

    val output = st.output.joinToString (separator = ",")
    val r = st.regs

    val r2 = r
    return output
}

fun part2(): Long? {
    val res = part2_backtrack()
    val check = part2_decompiled(res)
    val check2 = part2_before(res)
    if (res == check && res == check2) {
        return res
    }

    return null
}

fun part2_backtrack_rec(aSoFar: Long, ins: List<Long>): Long? {
    for(candidateAbonus in 0..<8) {
        val a = candidateAbonus + aSoFar * 8L
        var b = 0L
        var c = 0L

        b = a % 8
        b = b.xor(2L)
        c = a.shr(b.toInt())
        b = b.xor(c)
        b = b.xor(3)

        if (ins.last() == (b % 8)) {
            val nextIns =   ins.take(ins.size-1).toList()
            if (nextIns.isEmpty()) { return a }

            val innerRes = part2_backtrack_rec(a, nextIns)
            if (innerRes != null) {
                return innerRes
            }
        }
    }

    return null
}

fun part2_backtrack(): Long {
    val st = parseInput()
    
    val ins: List<Long> = st.ins

    val finalA = part2_backtrack_rec(0L, ins)

    return finalA!!
}

fun part2_decompiled(inputA: Long): Long? {
    val st = parseInput()
    val output = mutableListOf(0L)

//    for(iterA in 1L .. Long.MAX_VALUE) {
        output.clear()

//        var a = 35200350L
//        var a = iterA
        var a = inputA
        var b = 0L
        var c = 0L

        while(a != 0L) {
            b = a % 8
            b = b.xor(2L)
            c = a.shr(b.toInt())
            b = b.xor(c)
            b = b.xor(3)
            output.add(b % 8)
            a = a / 8L
        }

        if (st.ins.size != output.size) {
            return null
        }
        if (st.ins.zip(output).all { (a,b) ->a == b}) {
            return inputA
        }
//    }

    return null
}

fun part2_before(inputA: Long): Long? {
    val st0 = parseInput()
    val initR = st0.regs.toMap()



    var st = st0

//    for (a in 100000000L..9999999999L) {
    for(a in inputA..inputA) {
        val regsA = initR.toMutableMap()
        regsA['A'] = a
        st = State(
            pointer = 0,
            ins = st0.ins,
            regs = regsA,
            halt = false,
            output = mutableListOf()
        )

        while (!st.halt) {
            st = step(st)
        }

        //val output = st.output.joinToString(separator = ",")
        if (st.ins.size != st.output.size) {
            continue
        }
        if (st.ins.zip(st.output).all { (a,b) ->a == b}) {
            return inputA
        }
    }

    return null
}

