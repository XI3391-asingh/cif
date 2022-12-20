package com.finx.customerservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static io.dropwizard.jackson.Jackson.newObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PartyDetailTest {
    private static final ObjectMapper MAPPER = newObjectMapper();

    @Test
    void partyDetailSchemaTest() throws Exception {
        final PartyDetail partyDetail = PartyDetail.builder().partyId(70L).firstName("Test").lastName("Test").dateOfBirth("30/09/1994").primaryMobileNumber("1111111111").primaryEmail("test@test.com").build();

        final PartyDetail expected =
                MAPPER.readValue(getClass().getResource("/fixtures/PartyDetail.json"), PartyDetail.class);

        assertThat(partyDetail.toString()).isEqualTo(expected.toString());
        assertEquals(true, expected.equals(partyDetail));
    }
}