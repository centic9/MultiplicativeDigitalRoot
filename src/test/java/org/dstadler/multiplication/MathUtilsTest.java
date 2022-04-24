package org.dstadler.multiplication;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.dstadler.multiplication.MathUtils.MAX_DIGITS;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void getMultiplicativeDigitalRoot() {
        checkRoot("1", "1");
        checkRoot("1", "11");
        checkRoot("2", "12");
        checkRoot("10", "25");
        checkRoot("25", "55");
        checkRoot("0", "0");
        checkRoot("0", "10");
        checkRoot("0", "5040");
        checkRoot("672", "3784");
        checkRoot("84", "672");
        checkRoot("32", "84");
        checkRoot("6", "32");
        checkRoot("120", "12345");
    }

    private void checkRoot(String expected, String input) {
        assertEquals(expected, MathUtils.getMultiplicativeDigitalRoot(input).toString());

        // also check for byte-arrays
        byte[] number = new byte[MAX_DIGITS];
        MathUtils.toByteArray(number, new BigInteger(input));
        assertEquals(expected, MathUtils.getMultiplicativeDigitalRoot(number).toString());
    }

    @Test
    public void getMultiplicativeDigitalRootEmpty() {
        assertEquals(BigInteger.ONE, MathUtils.getMultiplicativeDigitalRoot(new byte[0]));
    }

    @Test
    public void testPersistence() {
        checkPersistence(1, "1");
        checkPersistence(1, "2");

        // https://oeis.org/A003001
        // 0, 10, 25, 39, 77, 679, 6788, 68889, 2677889, 26888999, 3778888999, 277777788888899
        //checkPersistence(0, "0");
        checkPersistence(1, "10");
        checkPersistence(2, "25");
        checkPersistence(3, "39");
        checkPersistence(4, "77");
        checkPersistence(5, "679");
        checkPersistence(6, "6788");
        checkPersistence(7, "68889");
        checkPersistence(8, "2677889");
        checkPersistence(9, "26888999");
        checkPersistence(10, "3778888999");
        checkPersistence(11, "277777788888899");

        checkPersistence(11, "1277777788888899");
        checkPersistence(11, "12777777888888991");
        checkPersistence(11, "2777777888888991");
        checkPersistence(11, "27777778888889911");

        checkPersistence(11, "84772818479372227374");

        checkPersistence(1, "100");
        checkPersistence(1, "847728184793722273740");
        checkPersistence(2, "12345");
    }

    private void checkPersistence(int expected, String input) {
        assertEquals(expected, MathUtils.getPersistence(input));

        // also check for byte-arrays
        byte[] number = new byte[MAX_DIGITS];
        MathUtils.toByteArray(number, new BigInteger(input));
        assertEquals(expected, MathUtils.getPersistence(number));
    }

    @Test
    public void testPrimeFactorization() {
        checkFactorization("0");
        checkFactorization("1");
        checkFactorization("2", "2");
        checkFactorization("3", "3");
        checkFactorization("4", "2", "2");
        checkFactorization("10", "2", "5");
        checkFactorization("13", "13");
        checkFactorization("24", "2", "2", "2", "3");

        // https://oeis.org/A003001
        // 0, 10, 25, 39, 77, 679, 6788, 68889, 2677889, 26888999, 3778888999, 277777788888899
        checkFactorization("0");
        checkFactorization("10", "2", "5");
        checkFactorization("25", "5", "5");
        checkFactorization("39", "3", "13");
        checkFactorization("77", "7", "11");
        checkFactorization("679", "7", "97");
        checkFactorization("6788", "2", "2", "1697");
        checkFactorization("68889", "3", "22963");
        checkFactorization("2677889", "29", "107", "863");
        checkFactorization("26888999", "523", "51413");
        checkFactorization("3778888999", "3778888999");
        checkFactorization("277777788888899", "13", "59", "1699", "213161503");

        // takes a few seconds: checkFactorization("1277777788888899", "3", "425925929629633");
        // takes a few seconds: checkFactorization("2777777888888991", "3", "113", "3011", "2721366679");
        // takes a few seconds: checkFactorization("27777778888889911", "7", "7", "23", "619", "8447", "4713901");
        // takes a few seconds: checkFactorization("84772818479372227374", "2", "3", "31", "10891", "137191", "305035439");
    }

    private void checkFactorization(String input, String... expectedFactors) {
        List<BigInteger> factors = MathUtils.primeFactors(input);

        assertEquals("Did not get expected factors, expected " + Arrays.toString(expectedFactors) + " but had " + factors,
                expectedFactors.length, factors.size());

        for(int i = 0;i < factors.size();i++) {
            assertEquals("Did not get expected factor at " + i + " for " + Arrays.toString(expectedFactors) + ", had: " + factors,
                    expectedFactors[i], factors.get(i).toString());
        }
    }

    @Test
    public void testLog10() {
        assertEquals(0, MathUtils.log10(BigInteger.ZERO));
        assertEquals(0, MathUtils.log10(BigInteger.ONE));
        assertEquals(0, MathUtils.log10(BigInteger.TWO));
        assertEquals(0, MathUtils.log10(new BigInteger("8")));
        assertEquals(0, MathUtils.log10(new BigInteger("9")));
        assertEquals(1, MathUtils.log10(BigInteger.TEN));
        assertEquals(1, MathUtils.log10(new BigInteger("11")));
        assertEquals(2, MathUtils.log10(new BigInteger("100")));
        assertEquals(3, MathUtils.log10(new BigInteger("1234")));
        assertEquals(4, MathUtils.log10(new BigInteger("31234")));
        assertEquals(16, MathUtils.log10(new BigInteger("27777778888889911")));
    }

    @Test
    public void testToByteArrayAndToString() {
        checkToByteArrayAndToString(new byte[] {0, -1}, "0");
        checkToByteArrayAndToString(new byte[] {1, -1}, "1");
        checkToByteArrayAndToString(new byte[] {0, 1, -1}, "10");
        checkToByteArrayAndToString(new byte[] {1, 1, -1}, "11");
        checkToByteArrayAndToString(new byte[] {2, 1, -1}, "12");
        checkToByteArrayAndToString(new byte[] {0, 0, 1, -1}, "100");
        checkToByteArrayAndToString(new byte[] {2, 0, 1, -1}, "102");
        checkToByteArrayAndToString(new byte[] {3, 4, 7, 9, 2, 1, -1}, "129743");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testToByteArrayOverflow() {
        MathUtils.toByteArray(new byte[2], new BigInteger("123"));
    }

    private void checkToByteArrayAndToString(byte[] expected, String strNumber) {
        byte[] number = new byte[MAX_DIGITS];
        MathUtils.toByteArray(number, new BigInteger(strNumber));

        for(int i = 0;i < MAX_DIGITS;i++) {
            if(expected[i] == -1 && number[i] == -1) {
                break;
            }

            assertEquals("Failed at " + i,
                    expected[i], number[i]);
        }

        assertEquals(strNumber, MathUtils.toString(number));
    }
}
