<h1 align="center">Bloom Filter</h1>

### Simple bloom filter implementation in kotlin
Uses standard formulas to allocate the size and number of hash functions

#### Initialization
Can be initialized as following

1)BloomFilter(size, numberOfElements) when size of the filter can be provided by the user

2)BloomFilter(numberOfElements) - Here the size for the filter is calculated on the formula - m = -((n*ln(p))/(ln(2)^2))
where m = size, n = number of elements, p-false positive rate

#### Hash Function
The number of hash functions needed are calculated by the filter itself using the formula mln2/n

#### SizeExceededException
Throws SizeExceededException when the first element is added after 70% utilization of the actual size.
This is to reduce the false positives with increasing size. user has option to reset the filter

