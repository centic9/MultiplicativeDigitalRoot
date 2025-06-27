package org.dstadler.multiplication;

import static org.dstadler.multiplication.MathUtils.MAX_DIGITS;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * A class which models an arbitrary long integer via
 * a byte array
 */
public class ByteArrayInteger {
	private final byte[] number = new byte[MAX_DIGITS];
	private int digits = 0;

	public ByteArrayInteger() {
		Arrays.fill(number, (byte) -1);
	}

	protected ByteArrayInteger(byte[] arr) {
		this();

		for (int i = 0; i < arr.length; i++) {
			number[i] = arr[i];
			if (arr[i] != -1) {
				digits++;
			}
		}
	}

	protected ByteArrayInteger(BigInteger big) {
		this();

		MathUtils.toByteArray(number, big);

		for (int i = 0; i < MAX_DIGITS; i++) {
			if (number[i] == -1) {
				break;
			}

			digits++;
		}
	}

	/**
	 * Increment to a following number which should be checked further.
	 *
	 * Note: This will not increment in single steps, but often
	 * skip large parts of numbers which are not interesting anyway, e.g.
	 * all numbers containing "0" or "1".
	 *
	 * The byte-array should be initialized fully with "-1" values.
	 *
	 * This method expects to usually only receive an empty byte-array (all -1s)
	 * or numbers which were the result of previous invocations of this method!
	 */
	public void increment() {
		for(int i = 0;i < MAX_DIGITS;i++) {
			byte nr = number[i];

			// reached the end and thus should set the current digit to 1 now
			if (nr == -1) {
				// skip 0 and 1 as both are not seen as candidates anyway
				number[i] = 2;
				digits = i+1;
				break;
			}

			// when a digit is 9, set it to 2 and continue incrementing the next digits
			if (nr == 9) {
				// all digits except the first two need to be 7, 8 or 9
				if (digits > 2 && i < (digits - 2)) {
					number[i] = 7;
				} else {
					// skip 0 and 1 as both are not seen as candidates anyway
					number[i] = 2;
				}
			} else {
				// if the following digit is higher, we can immediately increment to it
				// as otherwise we do not have digits in ascending order anymore
				if (number[i + 1] > (nr + 1)) {
					number[i] = number[i + 1];
				} else {
					// otherwise simply increment
					number[i] = (byte) (nr + 1);
				}

				break;
			}
		}
	}

	public byte[] getArray() {
		return number;
	}

	public BigInteger getAsBigInteger() {
		return new BigInteger(MathUtils.toString(number));
	}

	public int getLength() {
		int count = 0;
		for (; count < MAX_DIGITS; count++) {
			if (number[count] == -1) {
				break;
			}
		}

		return count;
	}

	public String toString() {
		return MathUtils.toString(number);
	}

	protected int getDigits() {
		return digits;
	}
}
