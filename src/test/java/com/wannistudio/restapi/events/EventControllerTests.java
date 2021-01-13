package com.wannistudio.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wannistudio.restapi.common.RestDocsConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class EventControllerTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    EventRepository eventRepository;

    @Test
    @DisplayName("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("spring")
                .description("rest api")
                .beginEnrollmentDateTime(LocalDateTime.of(2021, 1, 5, 16, 53))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 1, 6, 16, 53))
                .beginEventDateTime(LocalDateTime.of(2021, 1, 5, 16, 53))
                .endEventDateTime(LocalDateTime.of(2021, 1, 7, 16, 53))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("느그집")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-events").exists())
                .andDo(document("create-event", links(
                        linkWithRel("self").description("link to self"),
                        linkWithRel("query-events").description("link to query an exisist event"),
                        linkWithRel("update-events").description("link to update an exisist event")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of enrollment new event"),
                                fieldWithPath("beginEventDateTime").description("date time of new event"),
                                fieldWithPath("endEventDateTime").description("date time of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close enrollment time"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of new event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of enrollment new event"),
                                fieldWithPath("beginEventDateTime").description("date time of new event"),
                                fieldWithPath("endEventDateTime").description("date time of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close enrollment time"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of new event"),
                                fieldWithPath("free").description("it tells is this event is free or not"),
                                fieldWithPath("offline").description("it tells is this event is offline meeting or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("self link to event"),
                                fieldWithPath("_links.query-events.href").description("query event link"),
                                fieldWithPath("_links.update-events.href").description("update event link")
                        )
                    )
                )
        ;
    }

    @Test
    public void createEvent_badRequest() throws Exception {
        Event event = Event.builder()
                .id(10)
                .name("spring")
                .description("rest api")
                .beginEnrollmentDateTime(LocalDateTime.of(2021, 1, 5, 16, 53))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 1, 6, 16, 53))
                .beginEventDateTime(LocalDateTime.of(2021, 1, 6, 16, 53))
                .endEventDateTime(LocalDateTime.of(2021, 1, 7, 16, 53))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("느그집")
                .build();
//        event.setId(10);
//        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))
        ).andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto))
        )
                .andExpect(status().isBadRequest())
                ;
    }

    @Test
    public void createEvent_badRequest_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("spring")
                .description("rest api")
                .beginEnrollmentDateTime(LocalDateTime.of(2021, 1, 7, 16, 53))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 1, 6, 16, 53))
                .beginEventDateTime(LocalDateTime.of(2021, 1, 9, 16, 53))
                .endEventDateTime(LocalDateTime.of(2021, 1, 7, 16, 53))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("느그집")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].rejectedValue").exists())


        ;
    }
}
