package com.inventory.productservice.exceptions;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApiErrorTest {

    @Test
    void record_accessors_work() {
        LocalDateTime now = LocalDateTime.now();
        ApiError err = new ApiError(400, "msg", "/path", now);
        assertEquals(400, err.status());
        assertEquals("msg", err.message());
        assertEquals("/path", err.path());
        assertEquals(now, err.timestamp());
        assertNotNull(err.toString());
    }
}

