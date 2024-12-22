package finished.day22

data class Input(val arng: String, val constraint: List<Long>)

fun parseInput(): List<Long> {
    val r =
        common.Parsing().parseLns("day22", "input.txt")
            .filter { it.isNotEmpty() }
//            .map{it.split(Regex("[\\s]"))}
            .map{ it.toLong() }
            .toList()

    return r
}

fun mix(sn: Long, v: Long): Long {
    return sn.xor(v)
}

fun prune(xn: Long): Long {
    return xn % 16777216L
}

fun hash1(sn0: Long): Long {
    val sn1 = prune(mix(sn0, sn0 * 64L))
    val sn2 = prune(mix(sn1, sn1/32))
    val sn3 = prune(mix(sn2, sn2 * 2048L))
    return sn3
}

fun part1(): Long {
    val inps = parseInput()

    val rep = 2000

    var res = 0L
    for(inp in inps) {
        var ires = inp
        for (i in 1..rep) {
            ires = hash1(ires)
        }
        res += ires
    }

    return res
}

fun part2(): Long {
    val inps = parseInput()

    val rep = 2000

    val allPrices: MutableList<MutableList<Long>> = mutableListOf()

    for(inp in inps) {
        var hash = inp
        var price0 = hash%10L
        val prices : MutableList<Long> = mutableListOf()
        prices.add(price0)
        for (i in 1..rep) {
            hash = hash1(hash)
            prices.add(hash%10L)
        }
//        res += ires

        allPrices.add(prices)
        val x = 1
    }

    val allCombinations = allPrices.map {
        ap ->
        ap.mapIndexed { idx, b -> Pair(idx,b) }
        .windowed(5)
        .map { (a,b,c,d,e) ->
            val diffs = listOf(b.second-a.second,c.second-b.second,d.second-c.second,e.second-d.second)
            val priceSum = e.second // TODO?
            Triple(diffs, priceSum, a.first)
        }
        .groupBy { (diffs,price,idx) -> diffs }
        .map { grp ->
            val best = grp.value.sortedBy { it.third }.first()
            Pair(best.first, best.second)
        }
    }.flatten()

    val mostBananas = allCombinations.groupBy{it.first}
        .map{grp -> grp.value.map{ price -> price.second }.sum()}
        .sortedDescending()
        .first()

    // 2423
    return mostBananas
}

