package com.finx.customerservice.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finx.customerservice.api.PartyRequestCmd;
import com.finx.customerservice.core.customer.domain.PartyDetailsView;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PartyRepositoryTest extends JdbiTest {


    private final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void should_validate_party_detail_by_mobile_number() {
        /*
         * This test case will get enhance when will add create api
         * */
        PartyRepository partyRepositoryTest = jdbi.onDemand(PartyRepository.class);
        String mobileNumber = "1111111111";
        var w = partyRepositoryTest.fetchPartyDetailsByMobileNumber(mobileNumber);

        assertThat(w.size()).isEqualTo(0);
        assertThat(w.isEmpty()).isTrue();
    }


    @Test
    void should_return_response_when_is_party_saved() throws IOException {
        PartyRepository partyRepositoryTest = jdbi.onDemand(PartyRepository.class);
        PartyRequestCmd partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        Long partyId = Long.valueOf(partyRequestCmd.getParty().getPartyId());

        partyRepositoryTest.saveParty(partyRequestCmd.getParty(),partyRequestCmd.getOccupationDetail(),partyRequestCmd.getPartyFlag());
        List<PartyDetailsView> partyDetailsViewList = partyRepositoryTest.findPartyDetail(partyId);

        assertThat(partyDetailsViewList.size()).isEqualTo(1);
        assertThat(partyDetailsViewList.get(0).getPartyId()).isEqualTo(partyId);
    }
    @Test
    void should_return_empty_response_when_no_record_found_during_party_saved() throws IOException {
        PartyRepository partyRepositoryTest = jdbi.onDemand(PartyRepository.class);
        PartyRequestCmd partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        Long partyId = Long.valueOf(partyRequestCmd.getParty().getPartyId());

        partyRepositoryTest.saveParty(partyRequestCmd.getParty(),partyRequestCmd.getOccupationDetail(),partyRequestCmd.getPartyFlag());
        List<PartyDetailsView> partyDetailsViewList = partyRepositoryTest.findPartyDetail(2l);

        assertThat(partyDetailsViewList.size()).isEqualTo(0);
        assertThat(partyDetailsViewList.isEmpty()).isTrue();

    }

    @Test
    void should_return_response_when_party_address_not_saved() throws IOException {
        PartyRepository partyRepositoryTest = jdbi.onDemand(PartyRepository.class);
        PartyRequestCmd partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        Long partyId = Long.valueOf(partyRequestCmd.getParty().getPartyId());
        String addressLine1 = partyRequestCmd.getAddress().stream().findFirst().get().getAddressLine1();
        partyRepositoryTest.saveAddress(partyRequestCmd.getAddress(),partyId);
        List<PartyDetailsView> partyDetailsViewList = partyRepositoryTest.findPartyDetail(5l);
        boolean result = partyDetailsViewList.contains(addressLine1);
        assertThat(result).isFalse();
    }

}
