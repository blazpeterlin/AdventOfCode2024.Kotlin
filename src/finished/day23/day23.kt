package finished.day23

data class Input(val a: String, val b: String)

fun parseInput(): List<Input> {
    val r =
        common.Parsing().parseLns("day23", "input.txt")
            .filter { it.isNotEmpty() }
            .map{it.split(Regex("[-]"))}
            .map{ (a, b) -> Input(a, b) }
            .toList()

    return r
}


//data class SlnN(val ns: Set<String>)

fun part1(): Long {
    val inps = parseInput()

    val mp = listOf("1" to mutableListOf("2")).toMap().toMutableMap()
    mp.clear()

    for(inp in inps) {
        mp[inp.a] = mp[inp.a] ?: mutableListOf()
        mp[inp.b] = mp[inp.b] ?: mutableListOf()

        mp[inp.a]!!.add(inp.b)
        mp[inp.b]!!.add(inp.a)
    }

    val mpThrees : MutableSet<Set<String>> = mutableSetOf()
    for(a in mp.keys) {
        val mpa = mp[a]!!
        for(bi in 0..<mpa.size-1)
            for(ci in bi+1..<mpa.size) {
                val b = mpa[bi]
                val c = mpa[ci]

                if (mp[b]!!.contains(c)) {
                    mpThrees.add(setOf(a,b,c))
                }
            }
    }

    val fitting = mpThrees.filter{it.any{n -> n.startsWith('t')}}.toList()



    return fitting.size.toLong()
}


fun <T> getSubsetsInner(set: Set<T>, size: Int): Sequence<Set<T>> {
    if (size == 0 || set.isEmpty()) return sequenceOf(emptySet())

    val element = set.first()
    val rest = set - element
    val subsetsWithoutElement = getSubsets(rest, size)
    val subsetsWithElement = getSubsets(rest, size-1).map { it + element }

    return subsetsWithoutElement + subsetsWithElement
}

fun <T> getSubsets(set: Set<T>, size: Int): Sequence<Set<T>> {
    return getSubsetsInner(set, size).filter{ it.size == size }
}


fun part2(): String {
    val inps = parseInput()

    val mp = listOf("1" to mutableListOf("2")).toMap().toMutableMap()
    mp.clear()

    val st: HashMap<String, HashSet<String>> = hashMapOf()

    for(inp in inps) {
        mp[inp.a] = mp[inp.a] ?: mutableListOf()
        mp[inp.b] = mp[inp.b] ?: mutableListOf()

        mp[inp.a]!!.add(inp.b)
        mp[inp.b]!!.add(inp.a)


        st[inp.a] = st[inp.a] ?: hashSetOf()
        st[inp.b] = st[inp.b] ?: hashSetOf()
        st[inp.a]!!.add(inp.a)
        st[inp.b]!!.add(inp.b)

        st[inp.a]!!.add(inp.b)
        st[inp.b]!!.add(inp.a)
    }


    var bestBuildup: List<String> = listOf()
    for(potentialSize in 1..mp.keys.size){
        if (bestBuildup.size < potentialSize - 1) { break }
        for(a in mp.keys) {
            if (bestBuildup.size == potentialSize) { break }
            val sta = st[a]!!

            for(buildUp in getSubsets(sta, potentialSize)) {
                if (buildUp.size < bestBuildup.size) { continue }
                if (buildUp.all { bu ->
                        val stb = st[bu]!!
                        buildUp.all { inner ->
                            inner == bu || stb.contains(inner)
                        }
                    }) {
                    bestBuildup = buildUp.toList()
                    break
                }
            }
        }

    }

    val res = bestBuildup.sorted().joinToString(separator = ",")
    return res
}

