package io.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DecoratorTest {
    @Test
    public void testGetDecoratedRoot() {
        String a = "Foo";
        Foo b = new Foo(a);
        Foo c = new Foo(b);

        Assert.assertSame(b.getDecorated(), a);
        Assert.assertSame(c.getDecorated(), b);
        Assert.assertSame(b.getDecoratedRoot(), a);
        Assert.assertSame(c.getDecoratedRoot(), a);
    }

    @Test
    public void testUndecorate() {
        String a = "Foo";
        Foo b = new Foo(a);
        Foo c = new Foo(b);

        Assert.assertSame(Decorator.<CharSequence>undecorate(a), a);
        Assert.assertSame(Decorator.<CharSequence>undecorate(b), a);
        Assert.assertSame(Decorator.<CharSequence>undecorate(c), a);
    }

    static class Foo implements CharSequence, Decorator<CharSequence> {
        private final CharSequence decorated;

        Foo(CharSequence decorated) {
            this.decorated = decorated;
        }

        @Override
        public CharSequence getDecorated() {
            return decorated;
        }

        @Override
        public int length() {
            return getDecorated().length();
        }

        @Override
        public char charAt(int index) {
            return getDecorated().charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return getDecorated().subSequence(start, end);
        }

        @Override
        public String toString() {
            return getDecorated().toString();
        }
    }
}