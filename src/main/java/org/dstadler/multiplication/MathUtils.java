package org.dstadler.multiplication;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utilities for computing multiplicative persistence and multiplicative digital root
 *
 * See https://en.wikipedia.org/wiki/Multiplicative_digital_root and
 * https://en.wikipedia.org/wiki/Persistence_of_a_number
 */
public class MathUtils {
    public static final int MAX_DIGITS = 100;

    // Pre-compute digits from 0-9 to not having to construct them always
    private static final BigInteger[] DIGITS = new BigInteger[10];
    static {
        for(int i = 0;i < 10;i++) {
            DIGITS[i] = new BigInteger("" + i);
        }
    }

    /**
     * Compute the multiplicative persistence of a number, see
     * also https://en.wikipedia.org/wiki/Persistence_of_a_number and
     *
     * E.g. 10 =&gt; 1, 25 =&gt; 2, 39 =&gt; 3, ...
     *
     * See also https://oeis.org/A007954 and https://oeis.org/A003001 for related
     * mathematical series.
     *
     * @param input The number as string
     * @return How many times the multiplicative digital root can be computed on this number.
     */
    public static int getPersistence(String input) {
        if(input.contains("0")) {
            return 1;
        }

        int persistence = 1;
        while(true) {
            BigInteger product = getMultiplicativeDigitalRoot(input);

            //System.out.println(persistence + ": Result of " + input + ": " + product);

            // stop when we have reached a single-digit number
            if(product.compareTo(BigInteger.TEN) < 0) {
                return persistence;
            }

            persistence++;
            input = product.toString();
        }
    }

    /**
     * Compute the multiplicative persistence of a number, see
     * also https://en.wikipedia.org/wiki/Persistence_of_a_number and
     *
     * E.g. 10 =&gt; 1, 25 =&gt; 2, 39 =&gt; 3, ...
     *
     * See also https://oeis.org/A007954 and https://oeis.org/A003001 for related
     * mathematical series.
     *
     * @param inputOrig The number as a byte array, lower digits at the beginning, unused entries are "-1"
     * @return How many times the multiplicative digital root can be computed on this number.
     */
    public static int getPersistence(byte[] inputOrig) {
        byte[] input = Arrays.copyOf(inputOrig, inputOrig.length);
        if(containsZero(input)) {
            return 1;
        }

        int persistence = 1;
        while(true) {
            BigInteger product = getMultiplicativeDigitalRoot(input);

            //System.out.println(persistence + ": Result of " + input + ": " + product);

            // stop when we have reached a single-digit number
            if(product.compareTo(BigInteger.TEN) < 0) {
                return persistence;
            }

            persistence++;

            toByteArray(input, product);
        }
    }

    /**
     * Convert the given BigInteger into a byte-array
     *
     * @param number The array for storing the digits of the number in reverse order.
     *               The byte-array needs to be large enough to hold all bytes
     * @param bigNumber The number to convert to the byte-array
     * @throws ArrayIndexOutOfBoundsException if the byte-array is not large enough to
     *          hold all digits of the bigNumber
     */
    public static void toByteArray(byte[] number, BigInteger bigNumber) {
        int i = 0;
        int log10 = log10(bigNumber);

        for(; i <= log10; i++) {
            BigInteger[] divideAndRemainder = bigNumber.divideAndRemainder(BigInteger.TEN);
            bigNumber = divideAndRemainder[0];
            number[i] = (byte)(divideAndRemainder[1].toString().charAt(0)-0x30);
        }
        number[i] = -1;
    }

    /**
     * Conver the given digits from the byte-array into a string-representation of the number
     *
     * @param number The byte-array which holds the digits of the number in reverse order.
     * @return A string-representation of the number.
     */
    public static String toString(byte[] number) {
        StringBuilder builder = new StringBuilder();
        for (byte b : number) {
            if(b == -1) {
                break;
            }
            builder.append((char)(b + 0x30));
        }
        return builder.reverse().toString();
    }

    /**
     * Compute the multiplicative digital root of the given number.
     *
     * See also https://en.wikipedia.org/wiki/Multiplicative_digital_root
     *
     * E.g. 10 =&gt; 0, 25 =&gt; 10, 39 =&gt; 27, 27 =&gt; 14, 14 =&gt; 4, ...
     *
     * See also https://oeis.org/A007954 and https://oeis.org/A003001 for related
     * mathematical series.
     *
     * @param input The number as string
     * @return The multiplicative root of the given number.
     */
    public static BigInteger getMultiplicativeDigitalRoot(String input) {
        // shortcut this calculation
        if(input.contains("0")) {
            return BigInteger.ZERO;
        }

        BigInteger product = BigInteger.ONE;
        for (char digit: input.toCharArray()) {
            // multiplying by "1" does not change the result
            if(digit != '1') {
                product = product.multiply(DIGITS[digit - 0x30]);
            }
        }

        return product;
    }

    /**
     * Compute the multiplicative digital root of the given number.
     *
     * See also https://en.wikipedia.org/wiki/Multiplicative_digital_root
     *
     * E.g. 10 =&gt; 0, 25 =&gt; 10, 39 =&gt; 27, 27 =&gt; 14, 14 =&gt; 4, ...
     *
     * See also https://oeis.org/A007954 and https://oeis.org/A003001 for related
     * mathematical series.
     *
     * @param input The number as a byte array, lower digits at the beginning, unused entries are "-1"
     * @return The multiplicative root of the given number.
     */
    public static BigInteger getMultiplicativeDigitalRoot(byte[] input) {
        // shortcut this calculation
        if(containsZero(input)) {
            return BigInteger.ZERO;
        }

        BigInteger product = BigInteger.ONE;
        for (byte digit: input) {
            if(digit == -1) {
                break;
            }

            // multiplying by "1" does not change the result
            if(digit != 1) {
                product = product.multiply(DIGITS[digit]);
            }
        }

        return product;
    }

    private static boolean containsZero(byte[] number) {
        for (byte b : number) {
            if(b == -1) {
                return false;
            }

            if(b == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Simple algorithm for computing prime factors for BigIntegers.
     * Mostly used in testing/evaluation of known smallest numbers for
     * multiplicative persistence.
     *
     * @param input The number to factor
     * @return A sorted list of all factors of the given number.
     */
    public static List<BigInteger> primeFactors(String input) {
        /*        int n = numbers;
        List<Integer> factors = new ArrayList<Integer>();
        for (int i = 2; i <= n / i; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }
        if (n > 1) {
            factors.add(n);
        }
        return factors;*/

        BigInteger n = new BigInteger(input);
        List<BigInteger> factors = new ArrayList<>();

        // start with prime factor "2"
        BigInteger i = BigInteger.TWO;
        //noinspection ConditionalBreakInInfiniteLoop
        while(true) {
            // if i is too large for an additional factor we are done here
            if(i.compareTo(n.divide(i)) > 0) {
                break;
            }

            while(true) {
                if(n.remainder(i).equals(BigInteger.ZERO)) {
                    factors.add(i);
                    n = n.divide(i);
                } else {
                    break;
                }
            }

            i = i.add(BigInteger.ONE);
        }

        if (n.compareTo(BigInteger.ONE) > 0) {
            factors.add(n);
        }

        return factors;
    }

    /**
     * Compute the base-10 logarithm of the given BigInteger.
     * <p><p>
     * log10(0) =&gt; 0<p>
     * log10(1) =&gt; 0<p>
     * ...<p>
     * log10(9) =&gt; 0<p>
     * log10(10) =&gt; 1<p>
     * ...<p>
     * log10(100) =&gt; 2<p>
     * ...<p>
     *
     * @param input the number used for computation
     * @return the logarithm of the given number in base 10
     */
    public static int log10(BigInteger input) {
        int digits = 0;
        int bits = input.bitLength();
        while (bits > 4) {
            // 4 > log[2](10) so we should not reduce it too far.
            int reduce = bits / 4;
            // Divide by 10^reduce
            input = input.divide(BigInteger.TEN.pow(reduce));
            // Removed that many decimal digits.
            digits += reduce;
            // Recalculate bitLength
            bits = input.bitLength();
        }
        // Now 4 bits or less - add 1 if necessary.
        if ( input.intValue() > 9 ) {
            digits += 1;
        }
        return digits;
    }
}
