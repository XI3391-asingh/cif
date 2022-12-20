package com.cif.cifservice.core.party.event;

import com.cif.cifservice.api.PartyResponseCmd;

public record CreateEvent(PartyResponseCmd partyResponseCmd, long partyId) {
}
