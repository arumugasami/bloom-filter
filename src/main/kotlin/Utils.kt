import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.absoluteValue
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToInt

fun String.index(seed: Int, size: Int): Int {
    val md: MessageDigest = MessageDigest.getInstance("MD5")
    md.update(this.toByteArray())
    val digest = md.digest(this.toByteArray() + seed.toByte())
    return BigInteger(1, digest).remainder(BigInteger.valueOf(size.toLong()))
        .toInt()
}

fun calculateNumberOfHashes(size: Int, expectedElements: Int): Int {
    // Based on the calculation mln2/n , m=size, n= number of elements
    val numberOfHash = ((size * ln(2.0)) / expectedElements).roundToInt()
    return if (numberOfHash != 0) numberOfHash else 1
}

fun calculateSizeOfTheArray(fpr: Double, expectedElements: Int): Int {
    // Based on the calculation m = -((n*ln(p))/(ln(2)^2))
    val size = (expectedElements * ln(fpr).absoluteValue) / (ln(2.0).pow(2))
    return size.roundToInt()
}

fun readFilesFromResource(fileName: String): List<String> {
    val resource = object {}.javaClass.getResource(fileName).readText()
    return resource.split("\n");
}
