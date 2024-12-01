package common

import java.io.File

class Parsing {
    fun parseLns(dir: String): List<String> {
        val lns: List<String> =
            File("src/$dir/input.txt")
                .readLines()
        return lns
    }
}