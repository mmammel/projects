# Contrived example demonstrating performance advantage using [String.intern()](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#intern--)

Each class does the following:

* Read a large file containing a list of the same 39 words over and over
* For each word, compare them to the same list of 39 words as String literals 1000 times.
* Keep a frequency count of the words

The class `DontUseIntern` uses `.equals()` to compare the read-in words to the literals.  `UseIntern` first gets the "intern" representation of the word, stores it in a local `Map<String,String>`, and uses `==` to compare to the literals.

This example is meaningless, there are obviously better ways to get word frequencies, but it demonstrates the performance advantage of using == to compare string references while minimizing actual calls to `intern()`, which are expensive, as demonstrated by [this article](https://shipilev.net/jvm/anatomy-quarks/10-string-intern/).

A potential real-word use of this performance gain could be parsing large numbers of XML files where the parser is matching known XML node names as it runs through the files.  The number of unique tag names in the input is small assuming the input is somewhat controlled.

Sample output:

```
$ ./runTest.sh
Using .equals():

real	0m30.837s
user	0m31.209s
sys	0m0.183s

Using ==:

real	0m14.535s
user	0m14.715s
sys	0m0.141s
```
