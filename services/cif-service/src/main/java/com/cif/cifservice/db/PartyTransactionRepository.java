package com.cif.cifservice.db;

import com.cif.cifservice.api.*;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


public interface PartyTransactionRepository extends PartyRepository {


    String MOBILE = "Mobile";
    String EMAIL = "Email";

    @Transaction
    default Long createAllPartySection(PartyRequestCmd partyRequestCmd) {
        Long partyID;
        var createOccupationDetailCmd = Optional.ofNullable(partyRequestCmd.getOccupationDetail()).orElse(new CreateOccupationDetailCmd());
        if (isEmpty(partyRequestCmd.getParty().getCreatedBy())) {
            partyRequestCmd.getParty().setCreatedBy("SYSTEM");
        }
        if (isEmpty(partyRequestCmd.getParty().getPartyIdentifier())) {
            partyID = saveParty(partyRequestCmd.getParty(), createOccupationDetailCmd);
        } else {
            partyID = savePartyWithPartyIdentifier(partyRequestCmd.getParty(), createOccupationDetailCmd);
        }
        if (partyRequestCmd.getAddress() != null) {
            saveAddress(partyRequestCmd.getAddress(), partyID);
        }
        if (partyRequestCmd.getContactDetails() != null) {
            saveContactDetails(partyRequestCmd.getContactDetails(), partyID);
        }
        if (partyRequestCmd.getAssets() != null) {
            saveAsset(partyRequestCmd.getAssets(), partyID);
        }
        if (partyRequestCmd.getRisks() != null) {
            saveRisks(partyRequestCmd.getRisks(), partyID);
        }
        if (partyRequestCmd.getMemos() != null) {
            saveMemo(partyRequestCmd.getMemos(), partyID);
        }
        if (partyRequestCmd.getFatcaDetails() != null) {
            saveFatcaDetails(partyRequestCmd.getFatcaDetails(), partyID);
        }
        if (partyRequestCmd.getDocuments() != null) {
            saveDocument(partyRequestCmd.getDocuments(), partyID);
        }
        if (partyRequestCmd.getRelations() != null) {
            saveRelation(partyRequestCmd.getRelations(), partyID);
        }
        if (partyRequestCmd.getGuardians() != null) {
            saveGuardian(partyRequestCmd.getGuardians(), partyID);
        }
        return partyID;
    }

    @Transaction
    default boolean updateAllPartySection(UpdatePartyRequestCmd updatePartyRequestCmd) {
        var updateOccupationDetailCmd = Optional.ofNullable(updatePartyRequestCmd.getOccupationDetail()).orElse(new UpdateOccupationDetailCmd());
        var updatePartyFlagCmd = Optional.ofNullable(updatePartyRequestCmd.getPartyFlag()).orElse(new UpdatePartyFlagCmd());
        var updatedBy = updatePartyRequestCmd.getParty().getUpdatedBy() != null ? updatePartyRequestCmd.getParty().getUpdatedBy() : "SYSTEM";
        var partyId = findIdByPartyIdentifier(updatePartyRequestCmd.getParty().getPartyIdentifier());
        boolean partyUpdateStatus = updateParty(partyId, updatePartyRequestCmd.getParty(), updateOccupationDetailCmd, LocalDateTime.now(), updatedBy, updatePartyFlagCmd);

        if (updatePartyRequestCmd.getAddress() != null) {
            updateAddress(updatePartyRequestCmd.getAddress(), partyId);
        }
        if (updatePartyRequestCmd.getContactDetails() != null) {
            updateContactDetails(updatePartyRequestCmd.getContactDetails(), partyId);
        }
        if (updatePartyRequestCmd.getAssets() != null) {
            updateAsset(updatePartyRequestCmd.getAssets(), partyId);
        }
        if (updatePartyRequestCmd.getRisks() != null) {
            updateRisks(updatePartyRequestCmd.getRisks(), partyId);
        }
        if (updatePartyRequestCmd.getMemos() != null) {
            updateMemo(updatePartyRequestCmd.getMemos(), partyId);
        }

        if (updatePartyRequestCmd.getFatcaDetails() != null) {
            updateFatcaDetails(updatePartyRequestCmd.getFatcaDetails(), partyId);
        }

        if (updatePartyRequestCmd.getDocuments() != null) {
            updateDocument(updatePartyRequestCmd.getDocuments(), partyId);
        }

        if (updatePartyRequestCmd.getDocuments() != null) {
            updateDocument(updatePartyRequestCmd.getDocuments(), partyId);
        }
        if (updatePartyRequestCmd.getRelations() != null) {
            updateRelation(updatePartyRequestCmd.getRelations(), partyId);
        }
        if (updatePartyRequestCmd.getGuardians() != null) {
            updateGuardian(updatePartyRequestCmd.getGuardians(), partyId);
        }
        return partyUpdateStatus;
    }

    @Transaction
    default boolean saveContact(Set<CreateContactDetailsCmd> contactDetailsCmdSet, Long partyId) {
        boolean[] saveStatus = saveContactDetails(contactDetailsCmdSet, partyId);
        if (saveStatus.length > 0 && saveStatus[0]) {
            for (CreateContactDetailsCmd contactDetailsCmd : contactDetailsCmdSet) {
                updatePartyPrimaryMobileNumberAndPrimaryEmail(contactDetailsCmd.getIsPrimary(), partyId, contactDetailsCmd.getContactTypeCode(), contactDetailsCmd.getContactValue());
            }
        }
        return saveStatus[0];
    }

    @Transaction
    default boolean updateContactDetails(long partyId, UpdateContactDetailsCmd updateContactDetailsCmd) {
        var updateContactDetailsCmdSet = Collections.singleton(updateContactDetailsCmd);
        boolean[] updateStatus = updateContactDetails(updateContactDetailsCmdSet, partyId);
        if (updateStatus.length > 0 && updateStatus[0]) {
            updatePartyPrimaryMobileNumberAndPrimaryEmail(updateContactDetailsCmd.getIsPrimary(), partyId, updateContactDetailsCmd.getContactTypeCode(), updateContactDetailsCmd.getContactValue());
        }
        return updateStatus[0];

    }

    default boolean updatePartyPrimaryMobileNumberAndPrimaryEmail(boolean isPrimary, Long partyId, String contactTypeCode, String contactValue) {
        if (isPrimary) {
            UpdatePartyCmd updatePartyCmd = UpdatePartyCmd.builder().build();
            var value = findTypeValueByCode(contactTypeCode);
            if (isNotBlank(value) && MOBILE.equals(value)) {
                updatePartyCmd.setPrimaryMobileNumber(contactValue);
                return updateParty(partyId, updatePartyCmd, UpdateOccupationDetailCmd.builder().build(), LocalDateTime.now(), "User", UpdatePartyFlagCmd.builder().build());
            }
            if (isNotBlank(value) && EMAIL.equals(value)) {
                updatePartyCmd.setPrimaryEmail(contactValue);
                return updateParty(partyId, updatePartyCmd, UpdateOccupationDetailCmd.builder().build(), LocalDateTime.now(), "User", UpdatePartyFlagCmd.builder().build());
            }
        }
        return true;
    }
}
