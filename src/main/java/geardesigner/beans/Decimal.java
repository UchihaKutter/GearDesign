package geardesigner.beans;


/**
 * 可变小数位数显示的十进制类型
 *
 * @author SUPERSTATION
 */
public class Decimal extends Number implements Comparable<Decimal> {

    private static final long serialVersionUID = -2769073041747563049L;

    public static final int MAX_EXPONENT = 1023;

    /**
     * Minimum exponent a normalized {@code double} variable may
     * have.  It is equal to the value returned by
     * {@code Math.getExponent(Double.MIN_NORMAL)}.
     *
     * @since 1.6
     */
    public static final int MIN_EXPONENT = -1022;

    /**
     * The number of bits used to represent a {@code double} value.
     *
     * @since 1.5
     */
    public static final int SIZE = 64;

    /**
     * The number of bytes used to represent a {@code double} value.
     *
     * @since 1.8
     */
    public static final int BYTES = SIZE / Byte.SIZE;

    private final double value;

    public static String toString(Decimal d) {
        return d.toString();
    }

    public static Decimal valueOf(String s) throws NumberFormatException {
        return new Decimal(Double.parseDouble(s));
    }

    public static Decimal valueOf(double d) {
        return new Decimal(d);
    }

    public Decimal(double value) {
        this.value = value;
    }


    /**
     * Returns a string representation of this {@code Double} object.
     * The primitive {@code double} value represented by this
     * object is converted to a string exactly as if by the method
     * {@code toString} of one argument.
     *
     * @return a {@code String} representation of this object.
     * @see java.lang.Double#toString(double)
     */
    @Override
    public String toString() {
        return Double.toString(value);
    }

    /**
     * Returns the value of this {@code Double} as a {@code byte}
     * after a narrowing primitive conversion.
     *
     * @return the {@code double} value represented by this object
     * converted to type {@code byte}
     * @jls 5.1.3 Narrowing Primitive Conversions
     * @since 1.1
     */
    @Override
    public byte byteValue() {
        return (byte) value;
    }

    /**
     * Returns the value of this {@code Double} as a {@code short}
     * after a narrowing primitive conversion.
     *
     * @return the {@code double} value represented by this object
     * converted to type {@code short}
     * @jls 5.1.3 Narrowing Primitive Conversions
     * @since 1.1
     */
    @Override
    public short shortValue() {
        return (short) value;
    }

    /**
     * Returns the value of this {@code Double} as an {@code int}
     * after a narrowing primitive conversion.
     *
     * @return the {@code double} value represented by this object
     * converted to type {@code int}
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    @Override
    public int intValue() {
        return (int) value;
    }

    /**
     * Returns the value of this {@code Double} as a {@code long}
     * after a narrowing primitive conversion.
     *
     * @return the {@code double} value represented by this object
     * converted to type {@code long}
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    @Override
    public long longValue() {
        return (long) value;
    }

    /**
     * Returns the value of this {@code Double} as a {@code float}
     * after a narrowing primitive conversion.
     *
     * @return the {@code double} value represented by this object
     * converted to type {@code float}
     * @jls 5.1.3 Narrowing Primitive Conversions
     * @since 1.0
     */
    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(value);
        return (int) (bits ^ (bits >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Decimal)
                && (Double.doubleToLongBits(((Decimal) obj).value) ==
                Double.doubleToLongBits(value));
    }


    @Override
    public int compareTo(Decimal anotherDouble) {
        return compare(value, anotherDouble.value);
    }

    /**
     * Compares the two specified {@code double} values. The sign
     * of the integer value returned is the same as that of the
     * integer that would be returned by the call:
     * <pre>
     *    new Double(d1).compareTo(new Double(d2))
     * </pre>
     *
     * @param d1 the first {@code double} to compare
     * @param d2 the second {@code double} to compare
     * @return the value {@code 0} if {@code d1} is
     * numerically equal to {@code d2}; a value less than
     * {@code 0} if {@code d1} is numerically less than
     * {@code d2}; and a value greater than {@code 0}
     * if {@code d1} is numerically greater than
     * {@code d2}.
     * @since 1.4
     */
    public static int compare(double d1, double d2) {
        if (d1 < d2) {
            return -1;           // Neither val is NaN, thisVal is smaller
        }
        if (d1 > d2) {
            return 1;            // Neither val is NaN, thisVal is larger
        }

        // Cannot use doubleToRawLongBits because of possibility of NaNs.
        long thisBits = Double.doubleToLongBits(d1);
        long anotherBits = Double.doubleToLongBits(d2);

        return (thisBits == anotherBits ? 0 : // Values are equal
                (thisBits < anotherBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
                        1));                          // (0.0, -0.0) or (NaN, !NaN)
    }
}
