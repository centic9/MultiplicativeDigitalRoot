This project plays with "Multiplicative digital roots", see https://en.wikipedia.org/wiki/Multiplicative_digital_root and 
https://en.wikipedia.org/wiki/Persistence_of_a_number for the underlying theory.

See also:
* https://de.wikipedia.org/wiki/Querprodukt (German)
* https://www.spektrum.de/kolumne/behaglich-beharrliche-berechnungen/1643996 (German)

## Theory

There is a conjecture that there is no multiplicative persistence of an number higher than 11!

See https://oeis.org/A007954 and https://oeis.org/A003001 for related mathematical series



## Change it

### Grab it

    git clone git://github.com/centic9/MultiplicativeDigitalRoot

### Build it and run tests

	cd commons-dost
	./gradlew check jacocoTestReport

#### Licensing
* MultiplicativeDigitalRoot is licensed under the [BSD 2-Clause License].
* A few pieces are imported from other sources, the source-files contain the necessary license pieces/references.

[BSD 2-Clause License]: http://www.opensource.org/licenses/bsd-license.php
