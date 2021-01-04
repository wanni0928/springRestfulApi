package com.wannistudio.restapi.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


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
    public void javaBean() {

    }
}