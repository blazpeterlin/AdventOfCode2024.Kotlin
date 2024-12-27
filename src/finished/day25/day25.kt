package finished.day25

import common.transpose

data class LockKey(val isLock: Boolean, val sizes: List<Int>)

fun parseInput(): List<LockKey> {
    val r =
        common.Parsing().parseLns("day25", "input.txt")
            .filter { it.isNotEmpty() }
            .chunked(7)
            .map{chnk ->
                val tps = transpose(chnk)
                val isLock = tps[0][0] == '.'
                val nums = tps.map{tp -> tp.count{ ch -> ch == '#'}}.toList()
                LockKey(isLock, nums)
            }

    return r
}


fun part1(): Int {
    val inps = parseInput()

    val locks = inps.filter{it.isLock}.map{it.sizes}
    val keys = inps.filter{!it.isLock}.map{it.sizes}

    var res = 0
    for(l in locks) {
        for(k in keys) {
            if(k.zip(l).all{ (key,lock) -> key+lock-1-1<=5 }) {
                res++
            }
        }
    }

    return res
}

fun part2(): String {
    return "Christmas is saved!"
}

