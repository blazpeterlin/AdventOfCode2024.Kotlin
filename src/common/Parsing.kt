package common

import java.io.File

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