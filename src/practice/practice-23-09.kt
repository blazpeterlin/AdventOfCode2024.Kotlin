package practice_23_09

data class Input(val nums: List<Long>)

fun parseInput(): List<Input> {
    val r =
        common.Parsing().parseLns("practice")
            .filter { it.isNotEmpty() }
            .map { ln -> ln.split(" ").map { it.toLong() }}
            .toList()
    return r.map{Input(it)}.toList()
}

fun getNextNum(nums: List<Long>): Long {
    var cascadedList = listOf(nums).toMutableList()
    while(cascadedList.last().distinct().count() != 1 || cascadedList.last()[0] != 0L) {
        val next = cascadedList.last().windowed(2).map{ it[1] - it[0] }.toList()
        cascadedList.add(next)
    }

    return cascadedList.map{ cl -> cl.last()}.sum()
}

fun getPrevNum(nums: List<Long>): Long {
    var cascadedList = listOf(nums).toMutableList()
    while(cascadedList.last().distinct().count() != 1 || cascadedList.last()[0] != 0L) {
        val next = cascadedList.last().windowed(2).map{ it[1] - it[0] }.toList()
        cascadedList.add(next)
    }

    return cascadedList.map{ cl -> cl.first()}.reversed().fold(0, { acc, next -> next - acc })
}

fun part1(): Long {
    val inp = parseInput()

    val res = inp.map{ getNextNum(it.nums) }

    return res.sum()
}

fun part2(): Long {
    val inp = parseInput()

    val res = inp.map{ getPrevNum(it.nums) }

    return res.sum()
}