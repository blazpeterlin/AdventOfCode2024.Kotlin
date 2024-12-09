package day09

import kotlin.math.max

data class Input(val nums: List<Long>)
data class DiskFragment(val size: Long, val filled: Boolean, val idNum: Long)

fun parseInput(): Input {
    val r: List<Long> =
        common.Parsing().parseLns("day09")
            .filter { it.isNotEmpty() }
            .map { it.toCharArray().map{ s -> s.toString().toLong()} }
            .flatten()
            .toList()
    return Input(r);
}

fun fragmentsToFileSystem(inputFragments: List<DiskFragment>): List<Long?> {
    val r = sequence<Long?> {
        var idx = 0L

        for (frag in inputFragments) {
            for (i in 0..frag.size-1) {
                if (!frag.filled)
                    yield(null)
                else
                    yield(frag.idNum)
            }

            if (!frag.filled)
                idx++
        }
    }.toList()

    return r
}

fun toFileSystem(inputNums: List<Long>): List<Long?> {
    val r = sequence<Long?> {
        var isEmpty = false
        var idx = 0L

        for (n in inputNums) {
            for (i in 0..n-1) {
                if (isEmpty)
                    yield(null)
                else
                    yield(idx)
            }

            isEmpty = !isEmpty
            if (isEmpty)
                idx++
        }
    }.toList()

    return r
}

fun defragment(fileSystemNums: List<Long?>): List<Long> {
    val mutFsNums = fileSystemNums.toMutableList()

    var i = 0
    while(i < mutFsNums.size) {
        if (mutFsNums[i] != null){
            i++
            continue
        }

        mutFsNums[i] = mutFsNums.last()
        mutFsNums.removeAt(mutFsNums.size-1)
    }

    return mutFsNums.map { mn -> mn!! }.toList()
}

fun defragment2ButFast(inputNums: List<Long>): List<DiskFragment> {
    val diskFragments = inputNums.mapIndexed{ idx, n -> DiskFragment(n, idx % 2 == 0, idx/2L) }.toMutableList()

    for(droppingIndex in diskFragments.size-1 downTo 0) {
        val fragmentToMove = diskFragments[droppingIndex]
        if (!fragmentToMove.filled) { continue }

        val matchingSpace =
            diskFragments
                .take(droppingIndex)
                .firstOrNull { !it.filled && it.size >= fragmentToMove.size }

        if (matchingSpace != null) {
            val filledSpace = DiskFragment(fragmentToMove.size, true, fragmentToMove.idNum)
            val emptyRemainingSpace = DiskFragment(matchingSpace.size - fragmentToMove.size, false, 0)

            val indexToPlace = diskFragments.indexOf(matchingSpace)

            diskFragments.removeAt(droppingIndex)
            diskFragments.add(droppingIndex, DiskFragment(fragmentToMove.size, false, 0))

            diskFragments.removeAt(indexToPlace)

            diskFragments.add(indexToPlace, filledSpace)
            if (emptyRemainingSpace.size > 0) {
                diskFragments.add(indexToPlace+1, emptyRemainingSpace)
            }
        }
    }

    return diskFragments.toList()
}

fun checksum(nums: List<Long>): Long {
    val r = nums.mapIndexed { idx, n -> idx * n }.sum()
    return r
}

fun part1(): Long {
    val inp = parseInput()

    val fs = toFileSystem(inp.nums)
    val defragged = defragment(fs)
    val cs = checksum(defragged)

    return cs
}


fun part2(): Long {
    val inp = parseInput()

    val defragged2faster = defragment2ButFast(inp.nums)
    val defraggedFs = fragmentsToFileSystem(defragged2faster)
    val cs = checksum(defraggedFs.map{ it ?: 0L }.toList())

    return cs
}