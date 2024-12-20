package common

import java.io.File
data class ParsedMap(val m: Map<Coord2, Char>)

class Parsing {
    fun parseLns(dir: String, name: String = "input.txt"): List<String> {
        val lns: List<String> = file(dir, name).readLines()
        return lns
    }

    fun parseTxt(dir: String): String {
        return file(dir, "input.txt").readText()
    }

    private fun file(dir: String, name: String) = File("src/$dir/$name")




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