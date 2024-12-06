package practice

import practice_23_05.Transformation

//

data class Input(val nums: List<Long>)
//
//fun parseInput(): Input {
//    val inputLns = common.Parsing().parseLns("practice").filter { it.isNotEmpty() }
//
//    return Input(listOf())
//
//}


fun part1(): Int {
    val times = listOf(53,83,72,88)
    val dists = listOf(333,1635,1289,1532)

    val goodTimes = times.zip(dists).map { (t,d) ->
        var numBeats = 0
            for (i in 1..t-1) {
                val runUpTime = i
                val goTime = t - i
                if (runUpTime * goTime > d) {
                    numBeats++
                }
            }
            numBeats
        }


    return goodTimes.reduce { a,b -> a*b }
}


fun part2(): Int {
    val times = listOf(53837288L)
    val dists = listOf(333163512891532L)

    val goodTimes = times.zip(dists).map { (t,d) ->
        var numBeats = 0
        for (i in 1L..t-1L) {
            val runUpTime = i
            val goTime = t - i
            if (runUpTime == 53837288L/2) {
                numBeats += 0
            }
            if (runUpTime * goTime > d) {
                numBeats++
            }
        }
        numBeats
    }


    return goodTimes.reduce { a,b -> a*b }
}