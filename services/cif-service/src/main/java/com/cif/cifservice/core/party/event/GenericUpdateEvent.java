package com.cif.cifservice.core.party.event;

import com.cif.cifservice.api.PartyResponseCmd;
import com.cif.cifservice.api.UpdatePartyRequestCmd;

public record GenericUpdateEvent(PartyResponseCmd updatedDbPartyRecord, long partyId,
                                 UpdatePartyRequestCmd updatePartyRequestCmd) {
}
