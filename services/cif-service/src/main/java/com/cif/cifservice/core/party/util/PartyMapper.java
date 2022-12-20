package com.cif.cifservice.core.party.util;


import com.cif.cifservice.api.*;
import com.cif.cifservice.core.party.domain.PartyDetailsView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PartyMapper {

    PartyMapper MAPPER = Mappers.getMapper(PartyMapper.class);

    @Mapping(target = "cityZipCode", source = "zipCode")
    @Mapping(target = "country", source = "countryAddress")
    ViewAddressCmd mapAddressDetail(PartyDetailsView partyDetailsView);

    Set<ViewAddressCmd> mapAddressDetails(List<PartyDetailsView> partyDetailsView);

    @Mapping(target = "mothersMaidenName", source = "motherMaidenName")
    @Mapping(target = "primaryMobileNumber", source = "primaryMobileNumber")
    @Mapping(target = "referralCode", source = "refferalCode")
    @Mapping(target = "countryOfBirthCode", source = "countryBirthCode")
    @Mapping(target = "countryOfResidenceCode", source = "countryResidenceCode")
    @Mapping(target = "dateOfBirth", dateFormat = "dd-MM-yyyy", source = "dateOfBirth")
    @Mapping(target = "segmentCode", source = "segmentTypeCode")
    @Mapping(target = "amlRiskEvalDate", dateFormat = "dd-MM-yyyy", source = "amlRiskEvalDate")
    @Mapping(target = "createdAt", dateFormat = "dd-MM-yyyy'T'HH:mm:ss")
    ViewPartyCmd mapPartyInformation(PartyDetailsView partyDetailsView);

    @Mapping(target = "lastVerifiedDate", dateFormat = "dd-MM-yyyy", source = "lastVerifiedDate")
    ViewContactDetailsCmd mapPartyContactDetail(PartyDetailsView partyDetailsView);

    Set<ViewContactDetailsCmd> mapPartyContactDetails(List<PartyDetailsView> partyDetailsView);

    @Mapping(target = "companyTypeCode", source = "companyCode")
    @Mapping(target = "dateOfInCorporation", dateFormat = "dd-MM-yyyy", source = "dateOfIncorp")
    ViewOccupationDetailCmd mapPartyOccupationDetails(PartyDetailsView partyDetailsView);

    Set<ViewAssetsCmd> mapPartyAssetDetails(List<PartyDetailsView> partyDetailsView);

    @Mapping(target = "isSolvency", source = "isInsolvency")
    @Mapping(target = "isWillFullDefaulter", source = "isWillfulDefaulter")
    @Mapping(target = "willFullDefaulterDate", source = "willfulDefaulterDate")
    @Mapping(target = "isLoanOverDue", source = "isLoanOverdue")
    @Mapping(target = "isSuitFiled", source = "isSuitFailed")
    @Mapping(target = "isPoliticallyExposed", source = "isPep")
    @Mapping(target = "isUnderWatchList", source = "isUnderWatchlist")
    ViewPartyFlagCmd mapPartyFlag(PartyDetailsView partyDetailsView);

    @Mapping(target = "evaluationDate", dateFormat = "dd-MM-YYYY", source = "evaluationDate")
    @Mapping(target = "validUntil", dateFormat = "dd-MM-YYYY", source = "validUntil")
    ViewRisksCmd mapPartyRiskDetail(PartyDetailsView partyDetailsView);

    Set<ViewRisksCmd> mapPartyRiskDetails(List<PartyDetailsView> partyDetailsView);

    Set<ViewRelationsCmd> mapPartyRelationDetails(List<PartyDetailsView> partyDetailsView);

    @Mapping(target = "validTo", dateFormat = "dd-MM-YYYY", source = "validTo")
    @Mapping(target = "validFrom", dateFormat = "dd-MM-YYYY", source = "validFrom")
    ViewMemosCmd mapPartyMemoDetail(PartyDetailsView partyDetailsView);

    Set<ViewMemosCmd> mapPartyMemoDetails(List<PartyDetailsView> partyDetailsView);

    Set<ViewFatcaDetailsCmd> mapPartyFatcaDetails(List<PartyDetailsView> partyDetailsView);

    @Mapping(target = "issuingDate", dateFormat = "dd-MM-YYYY", source = "issuingDate")
    @Mapping(target = "expiryDate", dateFormat = "dd-MM-YYYY", source = "expiryDate")
    ViewDocumentsCmd mapPartyDocumentDetail(PartyDetailsView partyDetailsView);

    Set<ViewDocumentsCmd> mapPartyDocumentDetails(List<PartyDetailsView> partyDetailsView);

    @Mapping(target = "dateOfBirth", dateFormat = "dd-MM-YYYY", source = "dateOfBirth")
    @Mapping(target = "onBoardingDate", dateFormat = "dd-MM-YYYY", source = "onBoardingDate")
    List<PartyRecordCmd> mapPartyRecordCmdList(List<PartyRecordCmd> partyRecordCmdList);


    Set<ViewXrefsCmd> mapPartyXrefDetails(List<PartyDetailsView> partyDetailsView);

    Set<ViewGuardiansCmd> mapPartyGuardianDetails(List<PartyDetailsView> partyDetailsView);
}
