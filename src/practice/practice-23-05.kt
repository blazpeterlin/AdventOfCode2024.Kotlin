package practice_23_05

//

data class Transformation(val toStart: Long, val fromStart: Long, val len: Long);
data class Input(val seeds: List<Long>, val transformGroups: List<List<Transformation>>)

fun parseInput(): Input {
    val inputLns = common.Parsing().parseLns("practice").filter { it.isNotEmpty() }

    val splitterLns = inputLns
        .mapIndexed { idx, ln -> if (ln.contains("map")) idx else -1  }
        .filter { num -> num >= 0 }
        .toList()

    val seeds = inputLns[0].split(' ').drop(1).map{ it.toLong() }

    val transformGroups: List<List<Transformation>> = inputLns
        .mapIndexed { idx, ln -> Pair(idx, ln)}
        .drop(1)
        .groupBy { (idx, ln) -> splitterLns.filter { spl -> spl < idx }.maxOrNull() ?: -1 }
        .map { grp ->
            grp.value.map{ x -> x.second }
                .filter { ln -> !ln.contains("map")}
                .map { ln -> ln.split(' ').map{it.toLong()}}
                .map { (a,b,c) ->Transformation (a,b,c)}
        }
        .toList()

    return Input(seeds, transformGroups)

}

fun transform (num: Long, trGrps : List<Transformation>): Long {
    val applicableTransform = trGrps.find { tr -> tr.fromStart <= num && num < tr.fromStart + tr.len }
    if (applicableTransform == null) {
        return num
    }

    return applicableTransform.toStart + num - applicableTransform.fromStart
}

fun transformAndReportNextStep (num: Long, trGrps : List<Transformation>): Pair<Long,Long> {
    val applicableTransform = trGrps.find { tr -> tr.fromStart <= num && num < tr.fromStart + tr.len }
    if (applicableTransform == null) {
        val nextTransform = trGrps.filter { tr -> tr.fromStart > num }.sortedBy { tr -> tr.fromStart }.firstOrNull()
        val nextStep = nextTransform?.fromStart ?: Long.MAX_VALUE
        return Pair(num, nextStep)
    }

    val mappedVal = applicableTransform.toStart + num - applicableTransform.fromStart
    val nextStep = applicableTransform.fromStart + applicableTransform.len - num
    return Pair(mappedVal, nextStep)
}

fun part1(): Long {
    val inp = parseInput()

    val locs = inp.seeds.map { seed -> inp.transformGroups.fold(seed) { acc, trGrps -> transform(acc, trGrps) } }

    return locs.min()
}


fun part2(): Long {
    val inp = parseInput()

    val seedGroups = inp.seeds.chunked(2)
    val seedGroupsWithMinLocs = seedGroups.map { (seedGrpFrom, seedGrpLen) ->
        var currSeed = seedGrpFrom
        var minLoc = Long.MAX_VALUE
        while(currSeed < seedGrpFrom + seedGrpLen) {
            val (resLoc, seedStep) = inp.transformGroups.fold(Pair(currSeed, Long.MAX_VALUE)) {
                (accSeed, minNextStep), trGrps ->
                    val (nextSeed, suggestedStep) = transformAndReportNextStep(accSeed, trGrps)
                    Pair(nextSeed, minOf(minNextStep, suggestedStep))
            }
            if (resLoc < minLoc) {
                minLoc = resLoc
            }
            currSeed += seedStep
        }
        minLoc
    }

    return seedGroupsWithMinLocs.min()
}