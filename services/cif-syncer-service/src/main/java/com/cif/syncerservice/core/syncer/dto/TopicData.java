package com.cif.syncerservice.core.syncer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TopicData(Set<String> metaFields, PartyRequest payload) {
}
