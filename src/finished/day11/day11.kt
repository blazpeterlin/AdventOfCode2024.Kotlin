package finished.day11

import java.math.BigInteger

data class Input(val nums: List<Long>)


fun parseInput(): List<Long> {
    val r: List<Long> =
        common.Parsing().parseLns("day11")
            .filter { it.isNotEmpty() }
            .map { ln -> ln.split(" ").map{ n->n.toLong() }}
            .first()
            .toList()
    return r;
}

data class Stone(val n: Long, val size: BigInteger)
val memory = hashMapOf(0L to listOf(Stone(1L, BigInteger.ONE)))

private fun sum(bis: List<BigInteger>): BigInteger {
    return bis.reduce{ bi1, bi2 -> bi1 + bi2}
}

fun blink(stones: List<Stone>): List<Stone> {

    val r2 = stones.map{ s ->
        var r = listOf(Stone(0L, BigInteger.ZERO))
        val mem = memory[s.n]
        if (mem != null) {
            r = mem.map { m -> Stone(m.n, m.size * s.size) }
        } else {
            val str = s.n.toString()
            if (str.length % 2 == 0) {
                val left = str.substring(0, str.length / 2).toLong()
                val right = str.substring(str.length / 2, str.length).toLong()
                r = listOf(left, right).groupBy { it }
                    .map { grp -> Stone(grp.key, s.size * grp.value.size.toBigInteger())}
            } else {
                r = listOf(Stone(s.n * 2024L, s.size))
            }
            memory[s.n] = r.map{ Stone(it.n, it.size / s.size) }
        }

        r
    }.flatten()
        .groupBy { it.n }
        .map {grp -> Stone(grp.key, sum(grp.value.map{grv -> grv.size}))}

    return r2
}

fun part1(): BigInteger {
    val stones = parseInput()
    val grouped = stones.groupBy { it }.map{ grp -> Stone(grp.key, grp.value.size.toBigInteger()) }.toList()

    val rep25 = (1..25).toList()

    val blink25 = rep25.fold(grouped) { acc, i -> blink(acc) }

    return sum(blink25.map{ it.size })
}


fun part2(): BigInteger {
    val stones = parseInput()
    val grouped = stones.groupBy { it }.map{ grp -> Stone(grp.key, grp.value.size.toBigInteger()) }.toList()

    val rep75 = (1..75).toList()

    val blink75 = rep75.fold(grouped) { acc, i -> blink(acc) }

    return sum(blink75.map{ it.size })
}