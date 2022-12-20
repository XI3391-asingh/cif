package com.finx.customerservice.core.customer.util;


import com.finx.customerservice.api.OccupationDetailCmd;
import com.finx.customerservice.api.PartyAddressCmd;
import com.finx.customerservice.api.PartyCmd;
import com.finx.customerservice.api.PartyFlagCmd;
import com.finx.customerservice.core.customer.domain.PartyDetailsView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PartyMapper {

    PartyMapper MAPPER = Mappers.getMapper(PartyMapper.class);

    @Mapping(target = "dateOfBirth", dateFormat = "dd-MM-yyyy", source = "dateOfBirth")
    @Mapping(target = "amlRiskEvalDate", dateFormat = "dd-MM-yyyy", source = "amlRiskEvalDate")
    PartyCmd mapPartyInformation(PartyDetailsView partyDetailsView);

    OccupationDetailCmd mapPartyOccupationDetails(PartyDetailsView partyDetailsView);
    PartyFlagCmd mapPartyFlag(PartyDetailsView partyDetailsView);

    PartyAddressCmd mapAddress(PartyDetailsView partyDetailsView);
    Set<PartyAddressCmd> mapAddressList(List<PartyDetailsView> partyDetailsView);






}
