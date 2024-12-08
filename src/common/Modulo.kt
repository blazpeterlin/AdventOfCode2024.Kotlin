package common

import java.math.BigInteger
import kotlin.math.abs


fun extendedGcd(a: BigInteger, b: BigInteger): Pair<BigInteger, BigInteger> {
    if (a == BigInteger.ZERO) return Pair(BigInteger.ZERO, BigInteger.ONE)
    val (x, y) = extendedGcd(b % a, a)
    return Pair(y - (b / a) * x, x)
}

fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}

// Function to calculate LCM
fun lcm(a: Long, b: Long): Long {
    return abs(a * b) / gcd(a, b)
}

fun gcdBig(a: BigInteger, b: BigInteger): BigInteger {
    return if (b == BigInteger.ZERO) a else gcdBig(b, a % b)
}
fun lcmBig(a: BigInteger, b: BigInteger): BigInteger {
    return (a * b) / gcdBig(a, b)
}

fun modInverse(a: BigInteger, m: BigInteger): BigInteger {
    val (x, _) = extendedGcd(a, m)
    return (x % m + m) % m
}

fun chineseRemainder(remainders: List<BigInteger>, moduli: List<BigInteger>): BigInteger {
    val prod = moduli.reduce { acc, m -> lcmBig(acc, m) /* acc * m */ }  // Product of all moduli
    var result = BigInteger.ZERO

    for (i in remainders.indices) {
        val partialProd = prod / moduli[i]
        val inverse = modInverse(partialProd, moduli[i])

//        result = result.plus(remainders[i] * partialProd * inverse)
        result += remainders[i] * partialProd * inverse
    }

    return result % prod
}

fun chineseRemainderWithStarts(startOffsets: List<Long>, moduli: List<Long>): BigInteger {
    val globalStart = startOffsets.max()
    val remaindersBig = moduli.mapIndexed { idx, mod -> (startOffsets[idx] + mod - globalStart) % mod }.map { BigInteger(it.toString()) }
    val moduliBig = moduli.map { BigInteger(it.toString()) }
    val chineseRes = chineseRemainder(remaindersBig, moduliBig)
    val res = BigInteger(globalStart.toString()) + chineseRes
    return res
}