package com.finx.customerservice.db;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.finx.customerservice.api.OccupationDetailCmd;
import com.finx.customerservice.api.PartyFlagCmd;
import com.finx.customerservice.api.PartyRequestCmd;
import com.finx.customerservice.core.customer.domain.PartyDetailsView;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class PartyTransactionRepositoryTest extends JdbiTest {
    private final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void should_return_response_when_party_is_created() throws IOException {
        PartyTransactionRepository partyTransactionRepositoryTest = jdbi.onDemand(PartyTransactionRepository.class);
        PartyRequestCmd partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        Long partyId = Long.valueOf(partyRequestCmd.getParty().getPartyId());
        PartyFlagCmd partyFlagCmd = partyRequestCmd.getPartyFlag();
        Long result = partyTransactionRepositoryTest.createAllPartyRelatedRecords(partyRequestCmd,partyRequestCmd.getOccupationDetail(),partyFlagCmd);
        List<PartyDetailsView> partyDetailsViewList = partyTransactionRepositoryTest.findPartyDetail(partyId);
        assertThat(partyDetailsViewList.get(0).getPartyId()).isEqualTo(result);
    }

}
