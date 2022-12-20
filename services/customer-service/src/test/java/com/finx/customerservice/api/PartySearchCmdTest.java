package com.finx.customerservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static io.dropwizard.jackson.Jackson.newObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PartySearchCmdTest {
    private static final ObjectMapper MAPPER = newObjectMapper();

    @Test
    void partySearchCmdSchemaTest() throws Exception {
        final PartySearchCmd partySearchCmd = PartySearchCmd.builder().mobileNumber("1111111111").build();

        final PartySearchCmd expected =
                MAPPER.readValue(getClass().getResource("/fixtures/PartySearchCmd.json"), PartySearchCmd.class);

        assertThat(partySearchCmd.toString()).isEqualTo(expected.toString());
        assertEquals(true, expected.equals(partySearchCmd));
    }

}