package finished.day10

import common.*

data class Input(val nums: List<Long>)


fun parseInput(): Map<Coord2, Long> {
    val r: List<String> =
        common.Parsing().parseLns("day10")
            .filter { it.isNotEmpty() }
            .toList()
    return parseMapLong(r);
}

fun getTrailheadScore(s: Coord2, m: Map<Coord2, Long>): Long {
    val visited = mutableSetOf(s)
    val edge = mutableSetOf(s)
    while(edge.any()) {
        val next: List<Coord2> = edge.map{ e ->
            val hPrev: Long = m[e]!!
            val ns = to4ways(e).filter{ n ->
                !visited.contains(n)
                && m.containsKey(n)
                && m[n]!! == hPrev+1L }.toList()

            visited.addAll(ns)
            ns
        }.flatten().distinct().toList()

        edge.clear()
        edge.addAll(next)
    }

    return visited.filter{ v -> m[v]!! == 9L }.count().toLong()
}

fun getTrailheadRating(s: Coord2, m: Map<Coord2, Long>): Long {
    val visited: MutableMap<Coord2, Long> = mutableMapOf(s to 1L)
    val edge: MutableMap<Coord2, Long> = mutableMapOf(s to 1L)
    while(edge.any()) {
        val e: Pair<Coord2, Long> = edge.toList().sortedBy { m[it.first] }[0]
        val eKey = e.first
        val eVal = visited[eKey]

        val hPrev: Long = m[eKey]!!

        val nsGrabbed = to4ways(eKey).filter{ n ->
                    m.containsKey(n)
                    && m[n]!! == hPrev+1L
            }.toList()
        val ns: List<Pair<Coord2, Long>> = nsGrabbed
            .groupBy{it}
            .map{grp -> Pair(grp.key, eVal!! * grp.value.size.toLong())}
            .toList()

        for((c2, score) in ns) {
            if (visited[c2] != null) {
                val x = 0
            }
            visited[c2] = (visited[c2] ?: 0L) + score
        }


        val next: List<Pair<Coord2, Long>> = ns.toList()

        edge.remove(eKey)

        for((c2, score) in next) {
//            visited[c2] = score
            edge[c2] = score
        }
    }

    return visited.filter{ kvp -> m[kvp.key]!! == 9L }.map{ kvp -> kvp.value }.sum().toLong()
}

fun part1(): Long {
    val inp = parseInput()
    val starts = inp.filter{ kv -> kv.value == 0L }.map {it.key}.toList()
    val scores = starts.map{ getTrailheadScore(it, inp) }

    return scores.sum()
}


fun part2(): Long {
    val inp = parseInput()
    val starts = inp.filter{ kv -> kv.value == 0L }.map {it.key}.toList()
    val scores = starts.map{ getTrailheadRating(it, inp) }

    return scores.sum()
}