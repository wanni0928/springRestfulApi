package com.wannistudio.restapi.events;

import junitparams.JUnitParamsRunner;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
class EventTest {
    @Test
    public void builder() {
        Event event = Event.builder()
                .name("wanni rest api")
                .description("rest api description with spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void testFree() {
        // Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        // When
        event.update();
        // Then
        assertThat(event.isFree()).isTrue();

        // Given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();
        // When
        event.update();
        // Then
        assertThat(event.isFree()).isFalse();

        // Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();
        // When
        event.update();
        // Then
        assertThat(event.isFree()).isFalse();
    }

    @Test
    public void testOffline() {
        // Given
        Event event = Event.builder()
                .location("너네집")
                .build();
        // When
        event.update();
        // Then
        assertThat(event.isOffline()).isTrue();

        // Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        // When
        event.update();
        // Then
        assertThat(event.isOffline()).isFalse();
    }

    @Test
    public void javaBean() {

    }
}