package practice_23_02


data class CubeSubset(val red: Long, val green: Long, val blue: Long)

fun parseCubeSubset(str: String): CubeSubset {
    var chunks: MutableList<List<String>> = str.split(Regex("[, ]")).filter{ it.isNotEmpty()}.chunked(2).toMutableList()

    val existingColors = chunks.map{ it[1] }
    if (!("red" in existingColors)) { chunks.add(listOf("0", "red")) }
    if (!("green" in existingColors)) { chunks.add(listOf("0", "green")) }
    if (!("blue" in existingColors)) { chunks.add(listOf("0", "blue")) }

    val sortedChunks = chunks.sortedBy { it[1] }

    val red = sortedChunks[2][0].toLong()
    val green = sortedChunks[1][0].toLong()
    val blue = sortedChunks[0][0].toLong()

    return CubeSubset(red,green,blue)
}

data class Game(val id: Long, val cubeSubsets: List<CubeSubset>)

fun parseGame(tkns: List<String>): Game {
    val id = tkns[0].split(" ")[1].toLong()
    val subsetStrs = tkns.drop(1)
    val subsets = subsetStrs.map{ parseCubeSubset(it) }.toList()

    return Game(id, subsets)
}

fun parseInput(): List<Game> {
    val inputTerms: List<Game> =
        common.Parsing().parseLns("practice")
            .filter { it.isNotEmpty() }
            .map { ln -> ln.split(Regex("[:;]")) }
            .map { parseGame(it) }

    return inputTerms
}


fun part1(): Long {
    val inputTerms: List<Game> = parseInput()

    val validGames =
        inputTerms
        .filter{ it.cubeSubsets.all{ cs -> cs.red <= 12 && cs.green <= 13 && cs.blue <= 14 } }
    return validGames.sumOf { gm -> gm.id }
}

fun part2(): Long {
    val inputTerms = parseInput()

    val maxReds = inputTerms.map { it.cubeSubsets.maxOf { cs -> cs.red } }
    val maxGreens = inputTerms.map { it.cubeSubsets.maxOf { cs -> cs.green } }
    val maxBlues = inputTerms.map { it.cubeSubsets.maxOf { cs -> cs.blue } }

    return maxReds.zip(maxGreens).zip(maxBlues).sumOf { x -> x.first.first * x.first.second * x.second }
}