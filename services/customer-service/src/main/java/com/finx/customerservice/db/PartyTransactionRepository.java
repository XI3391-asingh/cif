package com.finx.customerservice.db;


import com.finx.customerservice.api.OccupationDetailCmd;
import com.finx.customerservice.api.PartyFlagCmd;
import com.finx.customerservice.api.PartyRequestCmd;


public interface PartyTransactionRepository extends PartyRepository {

    default Long createAllPartyRelatedRecords(PartyRequestCmd partyRequestCmd, OccupationDetailCmd occupationDetailCmd, PartyFlagCmd partyFlagCmd) {
        Long partyID = saveParty(partyRequestCmd.getParty(), occupationDetailCmd, partyFlagCmd);
        if (partyRequestCmd.getAddress() != null) {
            saveAddress(partyRequestCmd.getAddress(), partyID);
        }
        return partyID;
    }


}
