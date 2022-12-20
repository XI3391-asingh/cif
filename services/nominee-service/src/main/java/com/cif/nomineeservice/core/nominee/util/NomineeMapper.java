package com.cif.nomineeservice.core.nominee.util;

import com.cif.nomineeservice.api.UpdateNomineeRequest;
import com.cif.nomineeservice.core.nominee.domain.Nominee;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NomineeMapper {

    NomineeMapper MAPPER = Mappers.getMapper(NomineeMapper.class);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Nominee updateWithNullAsNoChangeNominee(UpdateNomineeRequest latestNomineeRecord, @MappingTarget Nominee existingNomineeRecord);
}
