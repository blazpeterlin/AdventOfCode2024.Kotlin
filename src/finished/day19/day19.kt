package finished.day19

data class Input(val towels: List<String>, val flags: List<String>)

fun parseInput(): Input {
    val r =
        common.Parsing().parseLns("day19")
            .filter { it.isNotEmpty() }
            .toList()

    val ts = r[0].split(Regex("[, ]")).filter{it.isNotEmpty()}.map{it.toString()}.toList()
    val fls = r.drop(1).toList()
    return Input(ts, fls)
}

data class State(val ts: List<String>, val s: String)

val cache = hashMapOf(State(emptyList(), "") to true)
fun isPossible(st: State): Boolean {
    val cached = cache[st]
    if (cached != null){return cached}
    if (st.s.isEmpty()) { return true }

    for(t in st.ts) {
        if(st.s.startsWith(t)) {
//            val nextSet = st.ts.toMutableList()
//            nextSet.remove(t)
            val nextTs = st.ts
            val nextSt = State(nextTs, st.s.substring(t.length))
            if (isPossible(nextSt)) {
                return true
            }
        }
    }
    return false
}

fun part1(): Int {
    val inps = parseInput()

    val valid = inps.flags.filter{ isPossible(State(inps.towels, it))}
    val res = valid.size

    return res
}

data class State2(val s: String, val idxs: List<Int>)

val cache2 = hashMapOf("" to 1L )
fun countPossible(ts: List<String>, st: State2): Long {
    val cached = cache2[st.s]
    if (cached != null) {
        return cached
    }
    if (st.s.isEmpty()) { return 1L }

    var totalRes = 0L
    for(idxTs in 0..<ts.size) {
        val t = ts[idxTs]
        if(st.s.startsWith(t)) {
            val nextIdxs = st.idxs.toMutableList()
            nextIdxs.add(idxTs)
            val nextSt = State2(st.s.substring(t.length), nextIdxs.toList())

            totalRes += countPossible(ts, nextSt)
        }
    }

    cache2[st.s] = totalRes
    return totalRes
}


fun part2(): Long {
    val inps = parseInput()

    val valid = inps.flags.map{ countPossible(inps.towels, State2(it, listOf()))}.toList()
    val res = valid.sum()

    return res
}

