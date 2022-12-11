package io.github.mjcro.objects.exceptions;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Optional;

public class ExceptionsTest {
    @Test
    public void testFind() {
        NullPointerException a = new NullPointerException();
        IllegalArgumentException b = new IllegalArgumentException(a);
        RuntimeException c = new RuntimeException(b);

        Optional<NullPointerException> npe = Exceptions.find(c, NullPointerException.class);
        Assert.assertTrue(npe.isPresent());
        Assert.assertSame(npe.get(), a);

        Optional<RuntimeException> rt = Exceptions.find(c, RuntimeException.class);
        Assert.assertTrue(rt.isPresent());
        Assert.assertSame(rt.get(), c);

        Optional<IllegalArgumentException> ia = Exceptions.find(c, IllegalArgumentException.class);
        Assert.assertTrue(ia.isPresent());
        Assert.assertSame(ia.get(), b);
    }

    @Test
    public void testFindNull() {
        Assert.assertFalse(Exceptions.find(null, RuntimeException.class).isPresent());
    }
}