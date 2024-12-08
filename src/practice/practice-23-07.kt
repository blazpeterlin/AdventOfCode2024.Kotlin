package practice_23_07


data class Input(val hand: List<Card>, val bid: Long)


var isSpecialJolly: Boolean = false;

enum class Card(val kind: Char) {
    Two('2'),
    Three('3'),
    Four('4'),
    Five('5'),
    Six('6'),
    Seven('7'),
    Eight('8'),
    Nine('9'),
    Ten('T'),
    Jack('J'),
    Queen('Q'),
    King('K'),
    Ace('A');

    fun strength(): Long {
        return if (this.kind.toString().toLongOrNull() is Long) {
            this.kind.toString().toLong()
        } else if (this == Ace) {
            14
        } else if (this == King) {
            13
        } else if (this == Queen) {
            12
        } else if (this == Jack) {
            if (isSpecialJolly)
                1
            else
                11
        } else if (this == Ten) {
            10
        } else {
            TODO()
        }
    }
}
enum class HandType(val strength: Long) {
    HighCard(1),
    OnePair(2),
    TwoPair(3),
    ThreeOfAKind(4),
    FullHouse(5),
    FourOfAKind(6),
    FiveOfAKind(7);
}

fun parseInput(): List<Input> {
    val r =
        common.Parsing().parseLns("practice")
            .filter { it.isNotEmpty() }
            .map { ln -> ln.split(" ") }
            .map { (a,b) -> Input(a.toCharArray().map{ ch -> Card.entries.find { c -> c.kind == ch }!! }.toList(), b.toLong()) }
            .toList()

    return r.toList()
}

fun getTypeNormalJolly(hand: List<Card>): HandType {
    val grps: List<List<Card>> =
        hand.groupBy { it }
            .toList()
            .sortedByDescending { (_, v) -> v.size }
            .map { it.second }
            .toList()

    if (grps[0].size == 5) {
        return HandType.FiveOfAKind;
    } else if (grps[0].size == 4) {
        return HandType.FourOfAKind;
    } else if (grps[0].size == 3 && grps[1].size == 2) {
        return HandType.FullHouse;
    } else if (grps[0].size == 3) {
        return HandType.ThreeOfAKind;
    } else if (grps[0].size == 2 && grps[1].size == 2) {
        return HandType.TwoPair;
    } else if (grps[0].size == 2) {
        return HandType.OnePair;
    } else {
        return HandType.HighCard;
    }
}

fun getType(hand: List<Card>): HandType {
    if (!isSpecialJolly) {
        return getTypeNormalJolly(hand)
    } else {
        val possibleTypes =
            Card.entries.filter{ c -> c.kind != 'J'}
                .map{ jReplacement -> hand.map{ c -> if (c == Card.Jack) jReplacement else c }.toList() }
                .map{ h -> getTypeNormalJolly(h) }
                .sortedByDescending { handType -> handType.strength }
        return possibleTypes.first()
    }
}

val handComparator = Comparator<Input> { i1, i2 ->
    val ht1 = getType(i1.hand)
    val ht2 = getType(i2.hand)

    if (ht1.strength != ht2.strength)
    {
        ht1.strength.compareTo(ht2.strength)
    }
    else
    {
        var result = 0;
        for(i in 0..i1.hand.size-1)
        {
            if (i1.hand[i].strength() != i2.hand[i].strength())
            {
                result = i1.hand[i].strength().compareTo(i2.hand[i].strength())
                break
            }
        }
        result
    }
}

fun part1(): Long {
    val inp = parseInput()

    isSpecialJolly = false;
    val rankedHands = inp.sortedWith(handComparator).mapIndexed { idx, h -> Pair(idx+1, h) }.toList()

    val score = rankedHands.map { (rank, input) -> rank * input.bid }.sum()

    return score
}


fun part2(): Long {
    val inp = parseInput()

    isSpecialJolly = true;
    val rankedHands = inp.sortedWith(handComparator).mapIndexed { idx, h -> Pair(idx+1, h) }.toList()

    val score = rankedHands.map { (rank, input) -> rank * input.bid }.sum()

    return score
}