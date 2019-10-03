[![Build Status](https://travis-ci.org/centic9/MultiplicativeDigitalRoot.svg)](https://travis-ci.org/centic9/MultiplicativeDigitalRoot) 
[![Gradle Status](https://gradleupdate.appspot.com/centic9/MultiplicativeDigitalRoot/status.svg?branch=master)](https://gradleupdate.appspot.com/centic9/MultiplicativeDigitalRoot/status)

This project plays with the mathematical topics of "Multiplicative digital roots" and 
"Multiplicative Persistence", see https://en.wikipedia.org/wiki/Multiplicative_digital_root and 
https://en.wikipedia.org/wiki/Persistence_of_a_number for more information.

It currently tries to compute the smallest number for each persistence using Java and BigInteger, 
exploring limits of handling large numbers with BigIntegers.

## Theory

There is a conjecture that there is no multiplicative persistence of a number higher than 11

See https://oeis.org/A007954 and https://oeis.org/A003001 for related mathematical series

More information on this topic:
* https://de.wikipedia.org/wiki/Querprodukt (German)
* https://www.spektrum.de/kolumne/behaglich-beharrliche-berechnungen/1643996 (German)
* https://arxiv.org/abs/1307.1188
* http://mathworld.wolfram.com/MultiplicativePersistence.html
* http://neilsloane.com/doc/persistence.html
* http://web.archive.org/web/20050214141815/http://www.wschnei.de/digit-related-numbers/persistence.html
* https://www.youtube.com/watch?v=Wim9WJeDTHQ
* http://www41.homepage.villanova.edu/robert.styer/MultiplicativePersistence/PersistenceStephPerezJournalArtAug2013.pdf
* http://markdiamond.com.au/download/joous-3-1-1.pdf
* https://www.tandfonline.com/doi/abs/10.1080/10586458.2014.910849

## Code

This project currently implements two ways of computing the multiplicative persistence of 
numbers.

The first version at `MultiplicativeDigitalRoot` uses Strings and BigIntegers to handle large numbers, but this is
obviously rather inefficient and limits the number of checks that can be performed.

The second version at `MultiplicativeDigitalRootByteArray` represents the digits of the number in a byte-array which
allows to perform some of the operations much quicker. It also optimizes incrementing the number a lot to check
much less numbers and skip large sections of numbers that are not relevant anyway.

## Change it

### Grab it

    git clone git://github.com/centic9/MultiplicativeDigitalRoot

### Build it and run tests

	cd MultiplicativeDigitalRoot
	./gradlew check jacocoTestReport

#### Licensing
* MultiplicativeDigitalRoot is licensed under the [BSD 2-Clause License].

[BSD 2-Clause License]: http://www.opensource.org/licenses/bsd-license.php
