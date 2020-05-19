<h1 align="center">Bloom Filter</h1>

### Simple bloom filter implementation in kotlin
  Uses standard formulas to deduce space allocation and hash functions

#### Initialization
Can be initialized as following

1)BloomFilter(size, numberOfElements) when space allocation is known

2)BloomFilter(numberOfElements) - Here the space allocation is calculated on the formula - m = -((n*ln(p))/(ln(2)^2))

#### Hash Function
The number of hash functions needed are calculated by the filter itself using the formula mln2/n

#### SizeExceededException
Throws SizeExceededException when the first element is added after 70% utilization of the actual size.
This is to reduce the false positives with increasing size. user has option to reset the filter

