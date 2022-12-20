package com.cif.syncerservice.core.syncer.helper;

import com.cif.syncerservice.api.*;
import com.cif.syncerservice.core.syncer.domain.AdapterConfig;
import com.cif.syncerservice.core.syncer.domain.ChangeConfig;
import com.cif.syncerservice.core.syncer.domain.SystemDetail;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SyncerMapper {

    SyncerMapper MAPPER = Mappers.getMapper(SyncerMapper.class);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "changeConfigId", source = "configId")
    ChangeConfig updateWithNullAsNoChangeConfig(UpdateConfigRequest latestConfigData, @MappingTarget ChangeConfig existingConfigData);

    String map(ApiDetails value);

    @Mapping(target = "apiDetails", source = "apiObject")
    @Mapping(target = "configId", source = "changeConfigId")
    ConfigResponseCmd mapConfigResponse(ChangeConfig changeConfig);

    @Mapping(target = "apiDetails", source = "apiDetails")
    ChangeConfig mapChangeConfig(CreateConfigRequest createConfigRequest);

    AdapterConfig mapAdapterConfig(CreateAdapterRequest createAdapterRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AdapterConfig updateWithNullAsNoChangeAdapter(UpdateAdapterRequest latestAdapterRequest, @MappingTarget AdapterConfig existingAdapterConfig);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SystemDetail updateWithNullAsNoSystemDetail(SystemRequestCmd systemRequestCmd, @MappingTarget SystemDetail systemDetail);


}
