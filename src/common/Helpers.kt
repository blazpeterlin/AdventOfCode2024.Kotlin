package common

public data class Coord2(val x: Long, val y: Long)
public data class GrabbedNumber(val num:Long, val idx: Int, val lastIdx: Int)

public fun grabNumber(ln: String): GrabbedNumber? {
    return grabNumber(ln, 0)
}

fun grabNumber(ln: String, startIdxOffset: Int): GrabbedNumber? {

    val idxOfNum = ln.indexOfFirst { ch -> ch.isDigit() }

    if (idxOfNum == -1) return null;

    var lastIdxOfNum = idxOfNum+1
    while(lastIdxOfNum < ln.length && ln[lastIdxOfNum].isDigit()) { lastIdxOfNum++ }

    val num = ln.substring(idxOfNum, lastIdxOfNum).toLong()

    return GrabbedNumber(num, startIdxOffset + idxOfNum, startIdxOffset + lastIdxOfNum-1)
}

public fun grabNumbers(ln: String) : Sequence<GrabbedNumber> {
    var remainingLn = ln;
    var grabbedNum = common.grabNumber(remainingLn);
    return sequence {
        while (grabbedNum != null) {
            remainingLn = ln.substring(grabbedNum!!.lastIdx + 1)

            yield(grabbedNum!!)

            grabbedNum = common.grabNumber(remainingLn, ln.length - remainingLn.length);
        }
    }
}



fun transpose(input: List<String>): List<String> {
    var r: MutableList<String> = mutableListOf()

    for (i in 0..<input[0].length) {
        val s = StringBuilder()
        for (j in 0..<input.size) {
            s.append(input[j][i])
        }

        r.add(s.toString())
    }

    return r;
}

fun diagonalize(input: List<String>, dir: Boolean): List<String> {
    var r: MutableList<String> = mutableListOf()

    for (i in -2*input.size..<2*input.size+1) {
        val s = StringBuilder()

        for (j in 0..<input.size*2+1) {

            val x = i + (if(dir) -j else +j)
            val y = j

            if (x < 0 || x >= input.size || y < 0 || y >= input.size) { continue }

            s.append(input[x][y])
        }

        if (s.length > 0) {
            r.add(s.toString())
        }
    }

    return r;
}

public fun extractImgs(input: List<String>, rows: Int, cols: Int): Sequence<String> {
    val w = input[0].length
    val h = input.size

    val res = sequence {

        for (i in 0..<h-rows+1) {

            for (j in 0..<w-cols+1) {

                val img = StringBuilder()

                for (k in i..i+rows-1) {
                    if (img.any()) img.append("\n")

                    for (l in j..j+cols-1) {
                        img.append(input[k][l])
                    }
                }

                yield(img.toString())

            }
        }
    }

    return res;
}