package games.gameOfFifteen

/*
 * This function should return the parity of the permutation.
 * true - the permutation is even
 * false - the permutation is odd
 * https://en.wikipedia.org/wiki/Parity_of_a_permutation

 * If the game of fifteen is started with the wrong parity, you can't get the correct result
 *   (numbers sorted in the right order, empty cell at last).
 * Thus the initial permutation should be correct.
 */

lateinit var visited: BooleanArray

lateinit var mappings: IntArray

fun isEven(permutation: List<Int>): Boolean {
    var inversions = 0
    for (i in permutation.indices) {
        for (j in i + 1 until permutation.size) {
            if (isInverse(permutation, i, j)) {
                inversions++
            }
        }
    }
    return inversions % 2 == 0
}


private fun isInverse(permutation: List<Int>, i: Int, j: Int): Boolean {
    return i < j && permutation[i] > permutation[j]
}


fun isEven2(permutation: List<Int>): Boolean {
    visited = BooleanArray(permutation.size + 1)
    mappings = IntArray(permutation.size + 1)

    val transpositions = transpositions(permutation)
    return transpositions % 2 == 0
}

fun transpositions(permutation: List<Int>): Int {
    for (i in 1..permutation.size) {
        visited[i] = false
    }

    for (i in 0 until permutation.size) {
        mappings[permutation[i]] = i + 1
    }

    var transpositionsCount = 0

    for (i in 1..permutation.size) {
        if (!visited[i]) {

            val cycleCalculation = calculationCicle(i)
            transpositionsCount += cycleCalculation - 1
        }
    }
    return transpositionsCount
}

fun calculationCicle(i: Int): Int {
    if (visited[i]) {
        return 0
    }

    visited[i] = true
    val x = calculationCicle(mappings[i])

    return x + 1
}