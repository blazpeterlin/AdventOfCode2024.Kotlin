package practice_23_01

data class InputTerm(val str: String)

fun parseInput(): List<InputTerm> {
    val inputTerms: List<InputTerm> =
        common.Parsing().parseLns("practice")
            .filter { it.isNotEmpty() }
//            .map { ln ->
//                ln.split(Regex("\\s+"))
//                    .map{ tkn -> tkn.toLong() }
//            }
            .map { InputTerm(it) }

    return inputTerms
}

fun findFirstLastNum(str: String): Long {
    val digits = str.filter{ ch -> ch.isDigit()}.map{d -> (""+d).toLong()}
    return digits.first()*10+digits.last();
}

fun numStrsToNums(str: String): String {
    return str
        .replace("twoneight", "218")
        .replace("oneight", "18")
        .replace("eightree", "83")
        .replace("eightwo", "82")
        .replace("sevenine", "79")
        .replace("twone", "21")
        .replace("one", "1")
        .replace("two", "2")
        .replace("three", "3")
        .replace("four", "4")
        .replace("five", "5")
        .replace("six", "6")
        .replace("seven", "7")
        .replace("eight", "8")
        .replace("nine", "9")
//        .replace("zero", "0")
}

fun part1(): Long {
    val inputTerms = parseInput()

    val nums: List<Long> = inputTerms.map{ findFirstLastNum(it.str) }
    return nums.sum()
}

fun part2(): Long {
    val inputTerms = parseInput()

    val transformedStrs = inputTerms.map{ numStrsToNums(it.str) }

    val nums: List<Long> = transformedStrs.map{ findFirstLastNum(it) }
    return nums.sum()
}