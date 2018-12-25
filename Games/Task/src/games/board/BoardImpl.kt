package games.board

import java.lang.IllegalArgumentException
import java.util.function.Consumer


open class SquareBoardImpl(final override val width: Int) : SquareBoard {

    var squareCells: Array<Array<Cell>> = arrayOf()

    init {
        squareCells += createArray(width)
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        try {
            verifyArguments(i, j)
        } catch (ex: IllegalArgumentException) {
            return null
        }

        return squareCells[i - 1][j - 1]
    }

    override fun getCell(i: Int, j: Int): Cell {
        verifyArguments(i, j)

        return squareCells[i - 1][j - 1]
    }

    override fun getAllCells(): Collection<Cell> {
        val listOfCells: ArrayList<Cell> = ArrayList(width * width)
        for (ii in 1..width) {
            for (jj in 1..width) {
                listOfCells.add(squareCells[ii - 1][jj - 1])
            }
        }
        return listOfCells
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {

        verifyArguments(i)
        val listOfCells: ArrayList<Cell> = ArrayList(width * width)
        jRange.forEach(Consumer { it ->
            if (it <= width) {
                listOfCells.add(squareCells[i - 1][it - 1])
            }
        })

        return listOfCells
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        verifyArguments(j)
        val listOfCells: ArrayList<Cell> = ArrayList(width * width)
        iRange.forEach(Consumer { it ->
            if (it <= width) {
                listOfCells.add(squareCells[it - 1][j - 1])
            }
        })

        return listOfCells
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        val cell: Cell?
        if (direction == Direction.DOWN) {
            if (this.i + 1 > width || this.j > width) {
                cell = null
            } else {
                cell = squareCells[i][j - 1]
            }
        } else if (direction == Direction.UP) {

            if (this.i <= 1 || this.j > width) {
                cell = null
            } else {
                cell = squareCells[this.i - 2][this.j - 1]
            }
        } else if (direction == Direction.RIGHT) {
            cell = if (this.i > width || this.j + 1 > width) {
                null
            } else {
                squareCells[i - 1][j]
            }
        } else {
            cell = if (this.i > width || this.j <= 1) {
                null
            } else {
                squareCells[i - 1][j - 2]
            }
        }

        return cell
    }

    private fun verifyArguments(i: Int, j: Int) {
        if (i > width || j > width) {
            throw IllegalArgumentException("incorrect values of i and j")
        }
    }

    private fun verifyArguments(i: Int) {
        if (i > width) {
            throw IllegalArgumentException("incorrect values of i and j")
        }
    }

    private fun createArray(width: Int): Array<Array<Cell>> {
        var cellsArray = arrayOf<Array<Cell>>()
        for (i in 1..width) {
            var array = arrayOf<Cell>()
            for (j in 1..width) {
                array += Cell(i, j)
            }
            cellsArray += array
        }

        return cellsArray
    }
}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {

    private val boardCellsMap: MutableMap<Cell, T?> = mutableMapOf()

    init {
        for (i in 1..width) {
            for (j in 1..width) {
                val cell = squareCells[i - 1][j - 1]
                boardCellsMap.put(cell, null)
            }
        }
    }

    override fun get(cell: Cell): T? {
        return boardCellsMap[cell]
    }

    override fun set(cell: Cell, value: T?) {
        boardCellsMap[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return boardCellsMap.keys
            .filter { it ->
                boardCellsMap[it]
                    .let(predicate)
            }
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return boardCellsMap.keys
            .find { it ->
                boardCellsMap.get(it)
                    .let(predicate)
            }

    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return boardCellsMap.values
            .any(predicate)
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return boardCellsMap.values.all(predicate)
    }

}

fun createSquareBoard(width: Int): SquareBoard {
    return SquareBoardImpl(width)
}

fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)