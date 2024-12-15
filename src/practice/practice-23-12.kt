package practice_23_12

data class Input(val arng: String, val constraint: List<Long>)

fun parseInput(): List<Input> {
    val r =
        common.Parsing().parseLns("practice")
            .filter { it.isNotEmpty() }
            .map{it.split(' ')}
            .map{ (a, b) -> Input(a, b.split(',').map{it.toLong()}) }
            .toList()

    return r
}

fun isValid(pr: String, constraint: Regex): Boolean {
    return constraint.matches(pr)
}


fun getPossibleRealities(arng: String/*, constraint: List<Long>*/): List<String> {
    var builtStrings = listOf("")
    for(idx in 0..<arng.length) {
        builtStrings =
            builtStrings
                .map {bs -> listOf(bs + if(arng[idx] == '?') '.' else arng[idx], bs + if(arng[idx] == '?') '#' else arng[idx]) }
                .flatten()
                .distinct()
    }

    return builtStrings
}

fun part1(): Long {
    val inps = parseInput()

    val validRealities = inps.map{ inp ->
        val prs = getPossibleRealities(inp.arng)
        val regexStr = inp.constraint.map{"#{" + it + "}"}.joinToString ( separator="[.]+" )
        val regex = Regex("[.]*" + regexStr + "[.]*")
        val valid = prs.filter{ pr -> isValid(pr, regex) }.toList()
        valid.size
    }

    return validRealities.sum().toLong()
}

fun mutate5x(origInp: Input): Input {
    val arngX5 = ("?" + origInp.arng).repeat(5).drop(1)
    val cns = origInp.constraint
    val cnsX5: List<Long> = cns + cns + cns + cns + cns

    return Input(arngX5, cnsX5)
}

fun getRemainingConstraint(ch: Char, con: List<Long>, enteredConstraint: Boolean): Pair<Boolean, List<Long>> {

        if (ch == '.' && enteredConstraint) { return Pair(false, con) }
        if (ch == '.' && con.isNotEmpty() && con[0] == 0L) { return Pair(true, con.drop(1)) }
        if (ch == '.') { return Pair(true, con) }

        // ch = '#'
        if (con.isEmpty()) { return Pair(false, con) }
        if (con[0] == 0L) { return Pair(false, con) }
        return Pair(true, listOf(con[0]-1) + con.drop(1))
}

val cache = hashMapOf(Pair("", listOf(0L)) to 1L)
fun getPossibleRealitiesSmart(arng: String, constraint: List<Long>, enteredConstraint: Boolean): Long {
    if (arng.isEmpty()) { return 1L }
    val cached = cache[Pair(arng, constraint)]
    if (cached != null) { return cached }

    val ch0 = arng[0]
    val arngNext = arng.drop(1)
    val nextChars = listOf(if(ch0 == '?') '.' else ch0, if(ch0 == '?') '#' else ch0).distinct()
    return sequence {
        for (nextCh in nextChars) {

            val (ok, innerCons) = getRemainingConstraint(nextCh, constraint, enteredConstraint)
            if (!ok) {
                continue
            }

            if (innerCons.sum() + innerCons.size - 2 > arngNext.length) {
                continue
            }

            if (arngNext.length==0 && innerCons.sum()>0L) {
                continue
            }

            val enteredConstraint = innerCons.isNotEmpty() && innerCons[0] != 0L && nextCh == '#'

            val resultsToRight: Long = getPossibleRealitiesSmart(arngNext, innerCons, enteredConstraint)
            cache[Pair(arngNext, innerCons)] = resultsToRight
            yield(resultsToRight)
        }
    }.sum()
}

fun part2(): Long {
    val origInps = parseInput()

    val inps = origInps.map{mutate5x(it)}.toList()

//    val inps = origInps

    val validRealities = inps.map{ inp ->
        println(inp.arng)
        cache.clear()
        val prs = getPossibleRealitiesSmart(inp.arng, inp.constraint, false)
        prs
    }

    return validRealities.sum()
}

