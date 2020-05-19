import Exceptions.SizeExceededException
import java.util.BitSet
import kotlin.properties.Delegates

class BloomFilter(
    private val size: Int,
    private val expectedSize: Int
) {
    private val bits = BitSet(size)
    var hashCount by Delegates.notNull<Int>()

    init {
        hashCount = calculateNumberOfHashes(size, expectedSize)
    }

    /**
     * Adds a list of words to the filter and returns void
     * @param values, the words to be added
     */
    fun loadValues(values: List<String>) {
        values.stream().forEach { value -> addSingleWord(value) }
    }

    /**
     * Adds a single word to the filter and returns void
     * @param word, the word to be added
     * @throws SizeExceededException on adding an element when 70% space of the filter size is already used
     */
    fun addSingleWord(word: String) {
        if (sizeUsed > (size * .7)) {
            throw SizeExceededException("80% of the bitset is filled")
        }
        val length = word.length
        for (iteration in 1..hashCount) {
            val findIndexToAdd = word.index(iteration * length + SALT, size)
            bits.set(findIndexToAdd)
        }
        sizeUsed++
    }

    /**
     * Checks for the presence of a word inside the filter and returns a boolean
     * @param word the word to checked
     */
    fun isWordValid(word: String): Boolean {
        val length = word.length
        for (hash in 1..hashCount) {
            val bitIndex = word.index(hash * length + SALT, size)
            if (!bits.get(bitIndex)) {
                return false
            }
        }
        return true
    }

    /**
     * clears the filter and the hash functions count
     */
    fun resetFilter() {
        bits.clear()
        hashCount = 0
    }

    companion object {
        /**
         * random value to be added as Salt for generating hashcode
         */
        const val SALT = 31

        /**
         * default false positive rate set to 1%, for calculating the size of the filter, when it is
         * not provided by the user
         */
        const val fpr = 0.01
        private var sizeUsed = 0

        /**
         * calculates the optimal size of the array based on the elements
         * @param numberOfElements, the estimated number of elements in array
         */
        operator fun invoke(numberOfElements: Int): BloomFilter {
            return BloomFilter(calculateSizeOfTheArray(fpr, numberOfElements), numberOfElements)
        }
    }
}
