package practice_23_14

import common.Coord2
import common.ParsedMap
import common.parseMap

data class Input(val arng: String, val constraint: List<Long>)

fun parseInput(): ParsedMap {
    val r =
        common.Parsing().parseLns("practice")
            .filter { it.isNotEmpty() }
//            .map{it.split(' ')}
//            .map{ (a, b) -> Input(a, b.split(',').map{it.toLong()}) }
            .toList()

    return parseMap(r)
}

fun moveNorth(m: Map<Coord2, Char>): Map<Coord2, Char> {
    val mm = m.toMutableMap()
    val xs = mm.keys.map{it.x}.distinct().sorted()
    val ys = mm.keys.map{it.y}.distinct().sorted()

    for (x in xs) {

        for(y in ys) {
            if (mm[Coord2(x,y)] != 'O') { continue }

            for(backy in y-1 downTo 0) {
                if (mm[Coord2(x, backy)] != '.') { break }

                mm[Coord2(x,backy)] = 'O'
                mm[Coord2(x,backy+1)] = '.'
            }
        }
    }

    return mm
}

fun calcLoad(m: Map<Coord2, Char>): Long {
    val maxHeight = m.keys.maxOf{it.y}+1
    return m.filter{ it.value == 'O' }.map{ kvp -> maxHeight - kvp.key.y }.sum()
}

fun part1(): Long {
    val m = parseInput().m

    val nm = moveNorth(m)

    val load = calcLoad(nm)

    return load
}

fun rotate(m: Map<Coord2, Char>): Map<Coord2, Char> {
    val maxX = m.keys.maxOf{it.x}
    val maxY = m.keys.maxOf{it.y}
    return m.map{ kvp -> Coord2(maxY-kvp.key.y, kvp.key.x) to kvp.value}.toMap()
}

fun part2(): Long {
    val m0 = parseInput().m

    var memory = hashMapOf(m0 to 0L)

    var mIter = m0

    var actualIter = 0L

    for(rep in 1L..4000000000L) {
//        println(rep-1L)
//        printMap(mIter)

        mIter = rotate(moveNorth(mIter))

        if (memory.containsKey(mIter)) {
            val prevRep = memory[mIter]!!
            val diff = rep-prevRep

            val bonusRepetitions = (4000000000L - rep) / diff
            actualIter = rep + bonusRepetitions * diff
            break
        }

        memory[mIter] = rep
    }

    for(rep in (actualIter+1L)..4000000000L) {
        mIter = rotate(moveNorth(mIter))
    }

//    val properlyRotated = rotate(rotate(rotate(mIter)))

    return calcLoad(mIter)
}

