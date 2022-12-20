package com.finx.customerservice.core.customer.services;

import com.finx.customerservice.api.*;
import com.finx.customerservice.core.customer.domain.Party;
import com.finx.customerservice.core.customer.domain.PartyDetailsView;
import com.finx.customerservice.core.customer.util.*;
import com.finx.customerservice.db.PartyRepository;
import com.finx.customerservice.db.PartyTransactionRepository;
import com.finx.customerservice.resources.exceptions.DatabaseException;
import io.dropwizard.jersey.errors.ErrorMessage;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;


public class PartyService {
    private Logger log = getLogger(PartyService.class);

    public static final String ERROR = "ERROR";
    public static final String FAILED_TO_GET_PARTY_DETAILS = "Failed To Get Party Details";

    private final PartyRepository partyRepository;
    private final PartyTransactionRepository partyTransactionRepository;

    public PartyService(PartyRepository partyRepository, PartyTransactionRepository partyTransactionRepository) {
        this.partyRepository = partyRepository;
        this.partyTransactionRepository = partyTransactionRepository;
    }

    public PartySearchResponse findPartyDetailsByMobileNumber(PartySearchCmd partySearchRequest) {
        try {
            List<Party> parties = partyRepository.fetchPartyDetailsByMobileNumber(partySearchRequest.getMobileNumber());
            List<PartyDetail> partyDetails = parties.stream().filter(party -> party != null).map(party -> getPartyDetail(party)).toList();
            log.info("Found {} record(s) for mobileNumber {}", parties.size(), partySearchRequest.getMobileNumber());
            return PartySearchResponse.builder().data(partyDetails).build();
        } catch (Exception e) {
            log.error("Error occurred while fetching party detail for [mobileNumber{}]", partySearchRequest.getMobileNumber());
            throw new DatabaseException("Error occurred while fetching party detail", e);
        }
    }

    private PartyDetail getPartyDetail(Party party) {
        return PartyDetail.builder().partyId(party.getPartyId()).primaryMobileNumber(party.getPrimaryMobileNumber()).primaryEmail(party.getPrimaryEmail()).firstName(party.getFirstName()).middleName(party.getMiddleName()).lastName(party.getLastName()).dateOfBirth(party.getDateOfBirth().toString()).build();
    }

    public PartyResponseCmd save(PartyRequestCmd partyRequestCmd) {
        try {
            OccupationDetailCmd occupationDetailCmd = Optional.ofNullable(partyRequestCmd.getOccupationDetail())
                    .orElse(new OccupationDetailCmd());
            PartyFlagCmd partyFlagCmd = Optional.ofNullable(partyRequestCmd.getPartyFlag())
                    .orElse(new PartyFlagCmd());
            var partyId = partyTransactionRepository.createAllPartyRelatedRecords(partyRequestCmd, occupationDetailCmd, partyFlagCmd);
            PartyResponseCmd partyResponseCmd = fetchPartyDetails(partyId);
            return partyResponseCmd;
        } catch (Exception e) {
            log.error("Failed to create Party for [firstName={}] and [mobileNumber={}]", partyRequestCmd.getParty().getFirstName(), partyRequestCmd.getParty().getPrimaryMobileNumber());
            throw new DatabaseException("Failed To Create Party", e);
        }
    }

    public ErrorMessage validatePartyRecord(PartyCmd partyCmd) {
        ErrorMessage response = null;
        if (StringUtils.isNotBlank(partyCmd.getGender()) && !Stream.of(GenderType.values()).map(GenderType::name).collect(Collectors.toSet()).contains(partyCmd.getGender().toUpperCase())) {
            response = new ErrorMessage(HttpStatus.BAD_REQUEST_400, ERROR, "Invalid value of gender ");
        } else if (StringUtils.isNotBlank(partyCmd.getCreatedByChannel()) && !Stream.of(ChannelType.values()).map(ChannelType::name).collect(Collectors.toSet()).contains(partyCmd.getCreatedByChannel().toUpperCase())) {
            response = new ErrorMessage(HttpStatus.BAD_REQUEST_400, ERROR, "Invalid value of Created By Channel ");
        } else if (StringUtils.isNotBlank(partyCmd.getMaritalStatus()) && !Stream.of(MaritalStatus.values()).map(MaritalStatus::name).collect(Collectors.toSet()).contains(partyCmd.getMaritalStatus().toUpperCase())) {
            response = new ErrorMessage(HttpStatus.BAD_REQUEST_400, ERROR, "Invalid value of Marital Status ");
        } else if (StringUtils.isNotBlank(partyCmd.getPartyType()) && !Stream.of(TypeId.values()).map(TypeId::name).collect(Collectors.toSet()).contains(partyCmd.getPartyType().toUpperCase())) {
            response = new ErrorMessage(HttpStatus.BAD_REQUEST_400, ERROR, "Invalid value of Party Type ");
        } else if (StringUtils.isNotBlank(partyCmd.getStatus()) && !Stream.of(StatusType.values()).map(StatusType::name).collect(Collectors.toSet()).contains(partyCmd.getStatus().toUpperCase())) {
            response = new ErrorMessage(HttpStatus.BAD_REQUEST_400, ERROR, "Invalid value of Status ");
        }
        List<Party> parties = partyRepository.fetchPartyDetailsByMobileNumber(partyCmd.getPrimaryMobileNumber());
        if (!parties.isEmpty()) {
            response = new ErrorMessage(HttpStatus.BAD_REQUEST_400, ERROR, "Party already exists with entered mobile number");
        }
        return response;
    }

    public PartyResponseCmd fetchPartyDetails(Long partyId) {
        List<PartyDetailsView> partyDetailList;
        PartyResponseCmd partyResponseCmd;
        try {
            partyDetailList = partyRepository.findPartyDetail(partyId);
        } catch (Exception e) {
            log.error("Database Exception Occurred for partyId={}", partyId);
            throw new DatabaseException(FAILED_TO_GET_PARTY_DETAILS, e);
        }
        partyResponseCmd = new PartyResponseCmd();
        partyResponseCmd.setParty(PartyMapper.MAPPER.mapPartyInformation(!partyDetailList.isEmpty() ? partyDetailList.get(0) : null));
        partyResponseCmd.setOccupationDetail(PartyMapper.MAPPER.mapPartyOccupationDetails(!partyDetailList.isEmpty() ? partyDetailList.get(0) : null));
        partyResponseCmd.setPartyFlag(PartyMapper.MAPPER.mapPartyFlag(!partyDetailList.isEmpty() ? partyDetailList.get(0) : null));
        partyResponseCmd.setAddress(PartyMapper.MAPPER.mapAddressList(!partyDetailList.isEmpty() && partyDetailList.get(0).getPartyAddressId() != null ? partyDetailList : new ArrayList<>()));
        return partyResponseCmd;
    }
}
