import Exceptions.SizeExceededException
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.math.roundToInt

internal class BloomFilterTest {

    @Test
    fun `hashCount gets Set on initialization of bloom filter`() {
        // given
        val size = 10000000
        val expectedSize = 1000000
        val expectedhash = ((size * 0.693) / expectedSize).roundToInt()

        // when
        val bloomFilter = BloomFilter(size, expectedSize)

        // then
        assertThat(bloomFilter.hashCount).isEqualTo(expectedhash)
    }

    @Test
    fun `all added elements are returned true on calling the filter`() {
        // given
        val bloomFilter = BloomFilter(100000, 10000)
        val list: MutableList<String> = arrayListOf()
        val minWordLength = 1
        for (i in 0..10000) {
            val toInt = ((Math.random() * 10) + minWordLength).toInt()
            list.add(randomAlphabetic(toInt))
        }

        // when
        bloomFilter.loadValues(list)

        // then
        for (i in 0 until list.size) {
            val isPresent = bloomFilter.isWordValid(list[i])
            assertThat(isPresent).isEqualTo(true)
        }
    }

    @Test
    fun `number of false positives should be less than 2%`() {
        // given
        // bloom filter when initialised should generate 7 hash functions based on the
        // equation  mln2/n and false positive should be around 1% based on (1- e^-kn/m)^k
        // where k = no. of hashes, n = number of elements, m = size

        val bloomFilter = BloomFilter(100000)
        val minWordLength = 1

        val setToLoadValuesToFilter: MutableSet<String> = HashSet()
        for (i in 0..100000) {
            val toInt = ((Math.random() * 10) + minWordLength).toInt()
            setToLoadValuesToFilter.add(randomAlphabetic(toInt))
        }

        val setForCheckingFalsePositives: MutableSet<String> = HashSet()
        for (i in 0..1000) {
            val toInt = ((Math.random() * 10) + minWordLength).toInt()
            setForCheckingFalsePositives.add(randomAlphabetic(toInt))
        }

        // to make sure that none of the random added elements in set2 are not already present in set1
        val checkingSet = setForCheckingFalsePositives.minus(setToLoadValuesToFilter)

        // when
        bloomFilter.loadValues(setToLoadValuesToFilter.toList())

        // then
        var elementsNotPresent = 0
        for (element in checkingSet) {
            val isPresent = bloomFilter.isWordValid(element)
            if (isPresent) continue
            elementsNotPresent++
        }

        val percentage: Double = (elementsNotPresent.toDouble() / checkingSet.size.toDouble())
        assertThat(percentage * 100).isGreaterThan(98.0)
    }

    @Test
    fun `should Throw Exception when size exceed 80%`() {
        // given
        val bloomFilter = BloomFilter(100000, 10000)
        val list: MutableList<String> = arrayListOf()
        val minWordLength = 1
        for (i in 0..70001) {
            val toInt = ((Math.random() * 10) + minWordLength).toInt()
            list.add(randomAlphabetic(toInt))
        }

        // then
        Assertions.assertThatThrownBy { bloomFilter.loadValues(list) }
            .isInstanceOf(SizeExceededException::class.java)
    }
}