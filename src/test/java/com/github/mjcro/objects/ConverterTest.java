package com.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class ConverterTest {
    @DataProvider
    public Object[][] standardDataProvider() {
        return new Object[][]{
                // Booleans
                {"True", true},
                {"False", false},
                {true, true},
                {false, false},
                {Boolean.TRUE, true},
                {Boolean.FALSE, false},
                {"1", true},
                {"2", false},
                {"0", false},

                // Shorts
                {(short) 2, (short) 2},
                {2, (short) 2},
                {2L, (short) 2},
                {BigDecimal.valueOf(3), (short) 3},
                {BigInteger.valueOf(2), (short) 2},
                {"2", (short) 2},

                // Integers
                {(short) 234, 234},
                {645, 645},
                {-3423L, -3423},
                {BigDecimal.valueOf(134), 134},
                {BigInteger.valueOf(3027), 3027},
                {"-93837", -93837},

                // Longs
                {(short) 234, 234L},
                {8726344, 8726344L},
                {-9943534523L, -9943534523L},
                {BigDecimal.valueOf(-3726482736L), -3726482736L},
                {BigInteger.valueOf(2933287235L), 2933287235L},
                {"-9383732001", -9383732001L},

                // Big integers
                {(short) 234, BigInteger.valueOf(234)},
                {8726344, BigInteger.valueOf(8726344L)},
                {-9943534523L, BigInteger.valueOf(-9943534523L)},
                {BigInteger.valueOf(2933287235L), BigInteger.valueOf(2933287235L)},
                {"-9383732001", BigInteger.valueOf(-9383732001L)},

                // Floats
                {(short) 21, 21.f},
                {222113, 222113.f},
                {2131L, 2131.f},
                {-33234., -33234.f},
                {231.44122d, 231.44122f},
                {231.44122f, 231.44122f},
                {BigInteger.valueOf(187235L), 187235.f},
                {BigDecimal.valueOf(8772.744), 8772.744f},
                {"-732001", -732001.f},

                // Doubles
                {(short) 21, 21.d},
                {222113, 222113.d},
                {2131L, 2131.d},
                {-33234., -33234.d},
                {231.44122f, 231.44122d},
                {BigInteger.valueOf(187235L), 187235.d},
                {BigDecimal.valueOf(8772.744), 8772.744d},
                {"-732001", -732001d},

                // Constructed using constructor
                {3865353L, new Constructing(3865353L)},
                {"Hello, world", new Constructing("Hello, world")},

                // Enums
                {SimpleEnum.ONE_WAY, SimpleEnum.ONE_WAY},
                {"OR", SimpleEnum.OR},
                {"Another ", SimpleEnum.ANOTHER}
        };
    }

    @Test(dataProvider = "standardDataProvider")
    public void testStandard(Object given, Object expected) {
        Assert.assertEquals(General.CONVERTER.convert(given, expected.getClass()), expected);
    }

    @DataProvider
    public Object[][] exceptionalStandardDataProvider() {
        return new Object[][]{
                // Boolean
                {1, boolean.class},
                {1, Boolean.class},
                {"", boolean.class},

                // Shorts
                {1234567890, short.class},
                {1.f, short.class},
                {1.d, short.class},
                {true, short.class},
                {false, short.class},
                {BigDecimal.valueOf(3.3), short.class},
                {new BigInteger("123456789012345678901234567890123456789012345678901234567890", 10), int.class},
                {"", short.class},

                // Integers
                {12345678901L, int.class},
                {1.f, int.class},
                {1.d, int.class},
                {true, int.class},
                {false, int.class},
                {BigDecimal.valueOf(3.3), int.class},
                {new BigInteger("123456789012345678901234567890123456789012345678901234567890", 10), int.class},
                {"", int.class},

                // Longs
                {1.f, long.class},
                {1.d, long.class},
                {true, long.class},
                {false, long.class},
                {BigDecimal.valueOf(3.3), int.class},
                {new BigInteger("123456789012345678901234567890123456789012345678901234567890", 10), int.class},
                {"", long.class},

                // Floats
                {true, float.class},
                {false, float.class},
                {"", float.class},

                // Doubles
                {true, double.class},
                {false, double.class},
                {"", double.class},

                // Constructing
                {2, Constructing.class},
        };
    }

    @Test(dataProvider = "exceptionalStandardDataProvider", expectedExceptions = ConversionException.class)
    public void testStandardExceptions(Object given, Class<?> clazz) {
        General.CONVERTER.convert(given, clazz);
    }

    private static class Constructing {
        private final String value;

        public Constructing(final Long value) {
            this.value = value.toString();
        }

        public Constructing(final String value) {
            this.value = String.valueOf(value);
        }

        public Constructing(final /* non boxed*/ int value) {
            this.value = String.valueOf(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Constructing that = (Constructing) o;
            return value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return "Constructing{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    private enum SimpleEnum {
        ONE_WAY, OR, ANOTHER;
    }
}