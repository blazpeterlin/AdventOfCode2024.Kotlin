package common

import java.io.File
data class ParsedMap(val m: Map<Coord2, Char>)

class Parsing {
    fun parseLns(dir: String): List<String> {
        val lns: List<String> = file(dir).readLines()
        return lns
    }

    fun parseTxt(dir: String): String {
        return file(dir).readText()
    }

    private fun file(dir: String) = File("src/$dir/input.txt")




}

public fun parseMap(lns: List<String>): ParsedMap {
    val res =
        lns.mapIndexed { y, ln: String ->
            ln.toCharArray().toList().mapIndexed { x: Int, ch: Char ->
                Coord2(x.toLong(),y.toLong()) to ch
            }
        }.flatten().toMap()
    return ParsedMap(res);
}

public fun parseMapLong(lns: List<String>): Map<Coord2, Long> {
    val res =
        lns.mapIndexed { y, ln: String ->
            ln.toCharArray().toList().mapIndexed { x: Int, ch: Char ->
                Coord2(x.toLong(),y.toLong()) to ch.toString().toLong()
            }
        }.flatten().toMap()
    return res;
}