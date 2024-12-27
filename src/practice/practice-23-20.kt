package practice_23_20

import common.Parsing
import java.util.*

data class Input(val ty: Char, val name: String, val targets: List<String>)





fun parseInput(): List<Input> {
    val r =
        Parsing().parseLns("practice", "input.txt")
            .filter { it.isNotEmpty() }
            .map{it.split(Regex("[\\s,->]")).filter{it.isNotEmpty()}}
            .map{ tkns ->
                val name0 = tkns[0]
                val tgts = tkns.drop(1)
                val (ty, name) = when {
                    name0.startsWith("%") -> Pair('F', name0.drop(1))
                    name0.startsWith("&") -> Pair('C', name0.drop(1))
                    name0 == "broadcaster" -> Pair('B', name0)
                    else -> TODO("what is " + name0)
                }

                Input(ty, name, tgts)
            }
            .toList()

    return r
}


interface IMod {
    val name: String
    fun receive(from: String, sig: Boolean)
    var tgts: List<IMod>
    fun processOnce()
}


class ModFlipFlop(override val name: String, var state: Boolean, override var tgts: List<IMod>,
                    private val received: Queue<Pair<String, Boolean>>) : IMod {
    override fun receive(from: String, sig: Boolean) {
        received.add(Pair(from, sig))
    }

    override fun processOnce() {
        val (from, sig) = received.poll()

        if (sig) { return }

        this.state = !this.state

        for(tgt in tgts) {
            tgt.receive(name, state)
        }

        for(tgt in tgts) {
            tgt.processOnce()
        }
    }
}

class ModConj(override val name: String, val recent: MutableMap<String, Boolean>, override var tgts: List<IMod>
              , private val received: Queue<Pair<String, Boolean>>) : IMod {
    override fun receive(from: String, sig: Boolean) {
        received.add(Pair(from, sig))
    }

    override fun processOnce() {
        val (from, sig) = received.poll()
        recent[from] = sig
        val sigToSend = !recent.all{ it.value }
        for(tgt in tgts) {
            tgt.receive(name, sigToSend)
        }

        for(tgt in tgts) {
            tgt.processOnce()
        }
    }
}

class ModBroadcast(override val name: String, override var tgts: List<IMod>
                   , private val received: Queue<Pair<String, Boolean>>): IMod {
    override fun receive(from: String, sig: Boolean) {
        received.add(Pair(from, sig))
    }

    override fun processOnce() {
        val (from, sig) = received.poll()
        for(tgt in tgts) {
            tgt.receive(name, sig)
        }

        for(tgt in tgts) {
            tgt.processOnce()
        }
    }
}

class ModOutput(override val name: String, override var tgts: List<IMod>, val output: MutableList<Boolean>): IMod {
    override fun receive(from: String, sig: Boolean) {
        if (name == "rx" && sig == false) {
            val end = true
        }
        output.add(sig)
    }

    override fun processOnce() {
    }
}

fun part1(): Long {
    val inps = parseInput().toMutableList()

    val out = ModOutput("output", listOf(), mutableListOf())
    val counter = ModOutput("counter", listOf(), mutableListOf())
    val knownModules: MutableMap<String, IMod> =
        listOf(out.name to out).toMap().toMutableMap()

    for (inp in inps) {
        val inpMod = when(inp.ty) {
            'F' -> ModFlipFlop(inp.name, false, emptyList(), LinkedList())
            'B' -> ModBroadcast(inp.name, emptyList(), LinkedList())
            'C' -> ModConj(inp.name, mutableMapOf(), emptyList(), LinkedList())
            else -> TODO("oops")
        }
        knownModules[inp.name] = inpMod
    }

    for (inp in inps) {

        val inpTargets = inp.targets.toMutableList()
        val tgts =  inpTargets.map{ knownModules[it] ?: ModOutput(it, listOf(), mutableListOf()) }.toMutableList()
        val tgtsSize = tgts.size
        for(i in 1..tgtsSize) {
            tgts.add(counter)
        }
        knownModules[inp.name]!!.tgts = tgts
    }

    for(conj in knownModules.values.filterIsInstance<ModConj>()) {
        conj.recent.putAll(
            knownModules.values
                .filter{ it.tgts.contains(conj) }
                .map{ it.name to false }
            )
    }

    val bc = knownModules["broadcaster"]!!



    for(i in 1 .. 1000) {
        bc.receive("-", false)
        counter.receive("-", false)
        bc.processOnce()
    }

    val lst = counter.output
//    lst.addAll(out2.output)


    val trues = lst.count{ it == true }.toLong()
    val falses = lst.count{ it == false }.toLong()

    val res = trues * falses

    return res
}

fun part2(): Long {
    val inps = parseInput().toMutableList()

    val out = ModOutput("output", listOf(), mutableListOf())
    val counter = ModOutput("counter", listOf(), mutableListOf())
    val rx = ModOutput("rx", listOf(), mutableListOf())

    val knownModules: MutableMap<String, IMod> =
        listOf(out.name to out).toMap().toMutableMap()
    knownModules["rx"] = rx

    for (inp in inps) {
        val inpMod = when(inp.ty) {
            'F' -> ModFlipFlop(inp.name, false, emptyList(), LinkedList())
            'B' -> ModBroadcast(inp.name, emptyList(), LinkedList())
            'C' -> ModConj(inp.name, mutableMapOf(), emptyList(), LinkedList())
            else -> TODO("oops")
        }
        knownModules[inp.name] = inpMod
    }

    for (inp in inps) {

        val inpTargets = inp.targets.toMutableList()
        val tgts =  inpTargets.map{ knownModules[it] ?: ModOutput(it, listOf(), mutableListOf()) }.toMutableList()
        val tgtsSize = tgts.size
        for(i in 1..tgtsSize) {
            tgts.add(counter)
        }
        knownModules[inp.name]!!.tgts = tgts
    }

    for(conj in knownModules.values.filterIsInstance<ModConj>()) {
        conj.recent.putAll(
            knownModules.values
                .filter{ it.tgts.contains(conj) }
                .map{ it.name to false }
        )
    }

    val bc = knownModules["broadcaster"]!!



    for(i in 1 .. 99999999999999999) {
        bc.receive("-", false)
        counter.receive("-", false)
        bc.processOnce()
    }

    val lst = counter.output

    val trues = lst.count{ it == true }.toLong()
    val falses = lst.count{ it == false }.toLong()

    val res = trues * falses

    return res
}

