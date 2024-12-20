package practice_23_15

import common.Parsing


fun parseInput(): List<String> {
    val r =
        Parsing().parseLns("practice")
            .filter { it.isNotEmpty() }
            .first().split(',')
            .toList()

    return r
}

fun hash(chars: List<Char>): Int {
    var res = 0
    for(ch in chars) {
        res = ((res + ch.code) * 17) % 256
    }
    return res
}

fun part1(): Long {
    val inps = parseInput()

    val h = hash("HASH".toCharArray().toList())

    val res = inps.map{ hash(it.toCharArray().toList()) }.sum()

    return res.toLong()
}

data class Instruction(val label: String, val operation: Char, val operand: Int?)

fun toIns(inp: String): Instruction {
    if (inp.contains("-")) {
        return Instruction(inp.take(inp.length-1), '-', null)
    } else {
        val (lbl, v) = inp.split('=')
        return Instruction(lbl, '=', v.toInt())
    }
}

data class Lens(val lbl: String, val value: Int)

fun part2(): Long {
    val inps = parseInput()

    val listIns = inps.map{ toIns(it) }.toList()

    val boxes = (0..255).map{ it to mutableListOf<Lens>() }.toMap()

    for(ins in listIns) {
        val h = hash(ins.label.toCharArray().toList())
        val lbl = ins.label

        val b = boxes[h]!!
        val match = b.firstOrNull { it.lbl == ins.label }

        if (ins.operation == '-') {
            if (match != null) {
                b.remove(match)
            }
        }

        if (ins.operation == '=') {
            val lens = Lens(ins.label, ins.operand!!)

            if (match != null) {
                b[b.indexOf(match)] = lens
            } else {
                b.add(lens)
            }
        }
    }

    val boxScores =
        boxes
            .map { entry -> (entry.key+1) * entry.value.mapIndexed{ idx, lens -> (idx+1) * lens.value }.sum() }
            .toList()

    val r = boxScores.sum()
    return r.toLong()
}

