package practice_23_19

import common.Graph
import common.Parsing

data class Rule(val ch: Char, val op: Char, val opnd: Long, val tgt: String)
data class Workflow(val name: String, val rules: List<Rule>, val final: String)
data class Part(val ratings: Map<Char, Long>)


fun parseInput(): Pair<List<Workflow>, List<Part>>{
    val wfs =
        Parsing().parseLns("practice", "input.txt")
            .filter { it.isNotEmpty() }
            .map{it.split(Regex("[{}]"))}
                .map{ (a, rulesStr) ->
                    val rs = rulesStr.split(',').toList()
                    val rules =
                        rs
                        .take(rs.size-1)
                        .map{ it.split(':') }
                        .map{ (cnd, tgt) -> Rule(cnd[0], cnd[1], cnd.drop(2).toLong(), tgt)}
                    val final = rs.last()

                    Workflow(a, rules, final)
                }

            .toList()

    val parts = Parsing().parseLns("practice", "input2.txt")
        .filter { it.isNotEmpty() }
        .map{it.split(Regex("[{xmas=},]")).filter{it.isNotEmpty()}.map{it.toLong()}}
        .map{(x,m,a,s) ->
            Part(mapOf('x' to x, 'm' to m, 'a' to a, 's' to s))
        }


    return Pair(wfs, parts)
}


fun mapWf(wf: Workflow, p: Part): String {
    for(r in wf.rules) {
        val rating = p.ratings[r.ch]!!
        if (r.op == '<' && rating < r.opnd) {
            return r.tgt
        }
        if (r.op == '>' && rating > r.opnd) {
            return r.tgt
        }
    }
    return wf.final
}

fun part1(): Long {
    val (wfs, parts) = parseInput()
    val wfsMap = wfs.map{wf -> wf.name to wf }.toMap()

    val finalStates =
        parts.map { p ->
            var state = "in"
            while(state != "A" && state != "R") {
                val wf =wfsMap[state]!!
                state = mapWf(wf, p)
            }
            Pair(state, p)
        }

    val res = finalStates.filter{ it.first == "A" }.sumOf{it.second.ratings.values.sum()}
    return res.toLong()
}

data class Range(val beg: Long, val end: Long)
data class State(val ranges: Map<Char, Range>, val pos: String) {
    fun isValid(): Boolean {
        return ranges.all { it.value.beg <= it.value.end }
    }

    fun getScore(): Long {
        val res: Long = ranges.values.fold(1L) { acc, rng ->
            acc * (rng.end - rng.beg + 1)
        }

        return res
    }
}

fun getNs(st: State, wfs: Map<String, Workflow>): List<State> {
    if (st.pos == "A" || st.pos == "R") { return listOf() }

    val wf = wfs[st.pos]!!

    var remaining = State(st.ranges, st.pos)

    val res: MutableList<State> = mutableListOf()

    for(r in wf.rules) {
        val rng = remaining.ranges[r.ch]!!

        if (r.op == '<' && rng.beg < r.opnd) {
            val map2 = remaining.ranges.toMutableMap()
            map2[r.ch] = Range(rng.beg, r.opnd - 1)
            res.add(State(map2, r.tgt))


            val mapRem = remaining.ranges.toMutableMap()
            mapRem[r.ch] = Range(r.opnd, rng.end)
            remaining = State(mapRem, remaining.pos)

            if (!remaining.isValid()) { break }
        }

        if (r.op == '>' && rng.end > r.opnd) {
            val map2 = remaining.ranges.toMutableMap()
            map2[r.ch] = Range(r.opnd + 1, rng.end)
            res.add(State(map2, r.tgt))


            val mapRem = remaining.ranges.toMutableMap()
            mapRem[r.ch] = Range(rng.beg, r.opnd)
            remaining = State(mapRem, remaining.pos)

            if (!remaining.isValid()) { break }
        }
    }

    if (remaining.isValid()) {
        res.add(State(remaining.ranges, wf.final))
    }

    return res
}


fun part2(): Long {
    val (wfs, _) = parseInput()
    val wfsMap = wfs.map{wf -> wf.name to wf }.toMap()

    val initial = listOf(
        'x' to Range(1, 4000),
        'm' to Range(1, 4000),
        'a' to Range(1, 4000),
        's' to Range(1, 4000),
        ).toMap()
    val st0 = State(initial, "in")

    val endstates = Graph<State>().TraverseWithIds(
        starts = listOf(st0),
        getNeighbours = { getNs(it, wfsMap) },
        getScore = { 0L },
        getId = { it }
    ).filter{ it.pos == "A" }
        .toList()

    val endscores = endstates.map{ it.getScore() }
    val res = endscores.sum()
    return res
}
