package io.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class ReactorTest {
    @Test
    public void testGet() {
        Reactor reactor = new Reactor();

        Assert.assertFalse(reactor.get(String.class).isPresent());

        reactor.register("Hello, world");
        Assert.assertTrue(reactor.get(String.class).isPresent());
        Assert.assertEquals(reactor.get(String.class).get(), "Hello, world");
    }

    @Test(
            dependsOnMethods = "testGet",
            expectedExceptions = NoSuchElementException.class,
            expectedExceptionsMessageRegExp = "No object or factory for .*"
    )
    public void testMustGet() {
        new Reactor().mustGet(String.class);
    }

    @Test(dependsOnMethods = "testMustGet")
    public void testGeneral() {
        Reactor reactor = new Reactor();
        reactor.register("As char sequence", CharSequence.class);
        reactor.register("Hello, world");
        reactor.register(Integer.class, $reactor -> $reactor.mustGet(String.class).length());
        reactor.register(Instant.class, (Supplier<Instant>) Instant::now);
        reactor.register(Duration.class, $reactor -> Duration.between($reactor.mustGet(Instant.class), Instant.now()));

        Assert.assertEquals(reactor.mustGet(CharSequence.class), "As char sequence");
        Assert.assertEquals(reactor.mustGet(String.class), "Hello, world");
        Assert.assertEquals(reactor.mustGet(Integer.class), 12);
        Assert.assertTrue(reactor.mustGet(Duration.class).toNanos() >= 0);
    }
}