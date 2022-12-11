package io.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponseTest {
    @Test
    public void testErrorResponseImpl() {
        List<Object> context = new ArrayList<>();
        context.add("Bar");
        context.add(76L);

        List<String> contextString = new ArrayList<>();
        contextString.add("Bar");
        contextString.add("76");

        ErrorResponse response = new ErrorResponseImpl(522, "Some message", context);
        Assert.assertEquals(response.getCode(), 522);
        Assert.assertEquals(response.getMessage(), "Some message");
        Assert.assertEquals(response.getContext(), context);
        Assert.assertTrue(response.has("Bar"));
        Assert.assertTrue(response.has(76L));
        Assert.assertFalse(response.has(76));
        Assert.assertEquals(response.getContextString(), contextString);
    }

    @Test(dependsOnMethods = "testErrorResponseImpl")
    public void testOfCodeMessage() {
        Assert.assertEquals(
                ErrorResponse.of(4, "Foo"),
                new ErrorResponseImpl(4, "Foo", null)
        );
    }

    @Test(dependsOnMethods = "testErrorResponseImpl")
    public void testOfCodeMessageContext() {
        List<Object> context = new ArrayList<>();
        context.add("Bar");
        context.add(76L);

        Assert.assertEquals(
                ErrorResponse.of(4, "Foo", context),
                new ErrorResponseImpl(4, "Foo", context)
        );
    }
}