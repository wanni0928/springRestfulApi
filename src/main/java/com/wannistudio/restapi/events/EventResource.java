package com.wannistudio.restapi.events;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventResource extends EntityModel<Event> {
//    @JsonUnwrapped
//    private Event event;
//
//    public EventResource(Event event) {
//        this.event = event;
//    }
//
//    public Event getEvent() {
//        return event;
//    }


    public EventResource(Event content, Link... links) {

//        super(content, links);
        super();
        content = EntityModel.of(content, links).getContent();
        add(linkTo(EventController.class).slash(content).withSelfRel());

    }
}