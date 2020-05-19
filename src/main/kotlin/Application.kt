fun main() {
    // this bloom filter when initialised should generate 7 hash functions based on the
    // equation  mln2/n and false positive should be around 1% based on (1- e^-kn/m)^k
    // (see further in tests)
    // where k = no. of hashes, n = number of elements, m = size

    val bloomFilter = BloomFilter(350000)
    val words = readFilesFromResource("wordlist.txt")

    bloomFilter.loadValues(words)

    //should return false
    println(bloomFilter.isWordValid("wordwhichisnotthere"))

    // should return true
    println(bloomFilter.isWordValid("money"))
    println(bloomFilter.isWordValid("heist"))
}