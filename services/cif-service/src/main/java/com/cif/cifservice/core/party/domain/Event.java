package com.cif.cifservice.core.party.domain;

public record Event(
        String eventSource,
        String eventId,
        String eventCreated,
        String eventType) {
}
