package com.masterdata.masterdataservice.db;

import com.masterdata.masterdataservice.api.*;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MasterDataRepositoryTests extends JdbiTest{
    private MasterDataRepository masterDataRepository;

    @BeforeAll
    void setup(){
        masterDataRepository = jdbi.onDemand(MasterDataRepository.class);
    }

    @Test
    @Order(1)
    void should_return_empty_when_address_master_data_is_not_fetched(){
        Set<ViewMasterDataCmd> result=masterDataRepository.findAllAddressMasterData();

        assertThat(result).isEmpty();
    }

    @Test
    @Order(2)
    void should_return_empty_response_when_look_up_master_data_is_not_fetched(){
        Set<ViewMasterDataCmd> resultCmd=masterDataRepository.findAllLookUpMasterData();

        assertThat(resultCmd).isEmpty();
    }

    @Test
    @Order(3)
    void should_return_empty_response_when_country_master_data_is_not_fetched(){
        Set<ViewCountryMasterDataCmd> resultSet=masterDataRepository.findAllCountryMasterData();

        assertThat(resultSet).isEmpty();
    }

    @Test
    @Order(4)
    void should_return_success_response_when_look_up_master_data_is_saved(){
        CreateMasterCmd createMasterCmd= CreateMasterCmd.builder().type("ADDRESS_TYPE").code("01").dtType(CreateMasterCmd.DtTypeEnum.ADDRESS_MASTER).build();
        List<CreateMasterCmd> masterList= Collections.singletonList(createMasterCmd);
        boolean[] result=masterDataRepository.saveLookUpMasterData(masterList);

        assertTrue(result[0]);
    }

    @Test
    @Order(5)
    void should_return_success_response_when_address_master_data_is_saved(){
        CreateMasterCmd createMasterCmd= CreateMasterCmd.builder().type("ADDRESS_TYPE").code("01").dtType(CreateMasterCmd.DtTypeEnum.ADDRESS_MASTER).build();
        List<CreateMasterCmd> masterList= Collections.singletonList(createMasterCmd);
        boolean[] result=masterDataRepository.saveAddressMasterData(masterList);

        assertTrue(result[0]);
    }

    @Test
    @Order(6)
    void should_return_success_response_when_look_up_master_data_is_fetched(){
        CreateMasterCmd createMasterCmd= CreateMasterCmd.builder().type("ADDRESS_TYPE").code("01").dtType(CreateMasterCmd.DtTypeEnum.ADDRESS_MASTER).build();
        List<CreateMasterCmd> masterList= Collections.singletonList(createMasterCmd);
        masterDataRepository.saveLookUpMasterData(masterList);
        Set<ViewMasterDataCmd> result=masterDataRepository.findAllLookUpMasterData();

        assertThat(result.size()).isGreaterThan(0);
    }

    @Test
    @Order(7)
    void should_return_success_response_when_address_master_data_is_fetched(){
        CreateMasterCmd createMasterCmd= CreateMasterCmd.builder().type("ADDRESS_TYPE").code("01").dtType(CreateMasterCmd.DtTypeEnum.ADDRESS_MASTER).build();
        List<CreateMasterCmd> masterList= Collections.singletonList(createMasterCmd);
        masterDataRepository.saveAddressMasterData(masterList);
        Set<ViewMasterDataCmd> result=masterDataRepository.findAllAddressMasterData();

        assertThat(result.size()).isGreaterThan(0);
    }

    @Test
    @Order(8)
    void should_return_success_response_when_look_up_master_data_is_updated(){
        CreateMasterCmd createMasterCmd= CreateMasterCmd.builder().type("ADDRESS_TYPE").code("01").dtType(CreateMasterCmd.DtTypeEnum.ADDRESS_MASTER).build();
        List<CreateMasterCmd> masterList= Collections.singletonList(createMasterCmd);
        masterDataRepository.saveLookUpMasterData(masterList);
        UpdateMasterDataRequestCmd updateLookUpMasterCmd= UpdateMasterDataRequestCmd.builder().code("02").build();
        Long masterId=1l;
        boolean result=masterDataRepository.updateLookUpMasterData(masterId,updateLookUpMasterCmd);

        assertTrue(result);
    }

    @Test
    @Order(9)
    void should_return_failure_response_when_look_up_master_data_is_not_updated(){
        CreateMasterCmd createMasterCmd= CreateMasterCmd.builder().type("ADDRESS_TYPE").code("01").dtType(CreateMasterCmd.DtTypeEnum.ADDRESS_MASTER).build();
        List<CreateMasterCmd> masterList= Collections.singletonList(createMasterCmd);
        masterDataRepository.saveLookUpMasterData(masterList);
        UpdateMasterDataRequestCmd updateLookUpMasterCmd= UpdateMasterDataRequestCmd.builder().code("02").build();
        Long masterId=0l;
        boolean result=masterDataRepository.updateLookUpMasterData(masterId,updateLookUpMasterCmd);

        assertFalse(result);
    }

    @Test
    @Order(10)
    void should_return_success_response_when_address_master_data_is_updated(){
        CreateMasterCmd createMasterCmd= CreateMasterCmd.builder().type("ADDRESS_TYPE").code("01").dtType(CreateMasterCmd.DtTypeEnum.ADDRESS_MASTER).build();
        List<CreateMasterCmd> masterList= Collections.singletonList(createMasterCmd);
        masterDataRepository.saveAddressMasterData(masterList);
        UpdateMasterDataRequestCmd updateAddressMasterCmd= UpdateMasterDataRequestCmd.builder().code("02").build();
        Long masterId=1l;
        boolean result=masterDataRepository.updateAddressMasterData(masterId,updateAddressMasterCmd);

        assertTrue(result);
    }

    @Test
    @Order(11)
    void should_return_failure_response_when_address_master_data_is_not_updated(){
        CreateMasterCmd createMasterCmd= CreateMasterCmd.builder().type("ADDRESS_TYPE").code("01").dtType(CreateMasterCmd.DtTypeEnum.ADDRESS_MASTER).build();
        List<CreateMasterCmd> masterList= Collections.singletonList(createMasterCmd);
        masterDataRepository.saveAddressMasterData(masterList);
        UpdateMasterDataRequestCmd updateAddressMasterCmd= UpdateMasterDataRequestCmd.builder().code("02").build();
        Long masterId=0l;
        boolean result=masterDataRepository.updateAddressMasterData(masterId,updateAddressMasterCmd);

        assertFalse(result);
    }

    @Test
    @Order(12)
    void should_return_success_response_on_address_master_delete(){
        CreateMasterCmd createMasterCmd= CreateMasterCmd.builder().type("ADDRESS_TYPE").code("01").dtType(CreateMasterCmd.DtTypeEnum.ADDRESS_MASTER).build();
        List<CreateMasterCmd> masterList= Collections.singletonList(createMasterCmd);
        masterDataRepository.saveAddressMasterData(masterList);
        boolean result=masterDataRepository.deleteAddressMasterData(1L);
        assertTrue(result);
    }

    @Test
    @Order(13)
    void should_return_failure_response_when_address_master_is_not_deleted(){
        CreateMasterCmd createMasterCmd= CreateMasterCmd.builder().type("ADDRESS_TYPE").code("01").dtType(CreateMasterCmd.DtTypeEnum.ADDRESS_MASTER).build();
        List<CreateMasterCmd> masterList= Collections.singletonList(createMasterCmd);
        masterDataRepository.saveAddressMasterData(masterList);
        boolean result=masterDataRepository.deleteAddressMasterData(0L);
        assertFalse(result);
    }

    @Test
    @Order(14)
    void should_return_success_response_on_lookup_master_delete(){
        CreateMasterCmd createMasterCmd= CreateMasterCmd.builder().type("ADDRESS_TYPE").code("01").dtType(CreateMasterCmd.DtTypeEnum.LOOKUP_MASTER).build();
        List<CreateMasterCmd> masterList= Collections.singletonList(createMasterCmd);
        masterDataRepository.saveLookUpMasterData(masterList);
        boolean result=masterDataRepository.deleteLookUpMasterData(1L);
        assertTrue(result);
    }

    @Test
    @Order(15)
    void should_return_success_response_when_lookup_master_is_not_deleted(){
        CreateMasterCmd createMasterCmd= CreateMasterCmd.builder().type("ADDRESS_TYPE").code("01").dtType(CreateMasterCmd.DtTypeEnum.LOOKUP_MASTER).build();
        List<CreateMasterCmd> masterList= Collections.singletonList(createMasterCmd);
        masterDataRepository.saveLookUpMasterData(masterList);
        boolean result=masterDataRepository.deleteLookUpMasterData(0L);
        assertFalse(result);
    }

    @Test
    @Order(16)
    void should_return_success_response_when_country_master_data_is_saved(){
        CreateCountryMasterCmd countryMasterCmd= CreateCountryMasterCmd.builder().code("01").createdBy("User1").build();
        List<CreateCountryMasterCmd> masterList= Collections.singletonList(countryMasterCmd);
        boolean[] result=masterDataRepository.saveCountryMasterData(masterList);

        assertTrue(result[0]);
    }

    @Test
    @Order(17)
    void should_return_success_response_when_country_master_data_is_fetched(){
        CreateCountryMasterCmd countryMasterCmd= CreateCountryMasterCmd.builder().code("01").createdBy("User1").build();
        List<CreateCountryMasterCmd> masterList= Collections.singletonList(countryMasterCmd);
        masterDataRepository.saveCountryMasterData(masterList);
        Set<ViewCountryMasterDataCmd> result=masterDataRepository.findAllCountryMasterData();

        assertThat(result.size()).isGreaterThan(0);
    }

    @Test
    @Order(18)
    void should_return_success_response_when_country_master_data_is_updated(){
        CreateCountryMasterCmd countryMasterCmd= CreateCountryMasterCmd.builder().code("01").createdBy("User1").build();
        List<CreateCountryMasterCmd> masterList= Collections.singletonList(countryMasterCmd);
        masterDataRepository.saveCountryMasterData(masterList);
        UpdateCountryMasterCmd updateCountryMasterCmd= UpdateCountryMasterCmd.builder().code("01").createdBy("User2").build();
        Long masterId=1l;
        boolean result=masterDataRepository.updateCountryMasterData(masterId,updateCountryMasterCmd);

        assertTrue(result);
    }

    @Test
    @Order(19)
    void should_return_failure_response_when_country_master_data_is_not_updated(){
        CreateCountryMasterCmd countryMasterCmd= CreateCountryMasterCmd.builder().code("01").createdBy("User1").build();
        List<CreateCountryMasterCmd> masterList= Collections.singletonList(countryMasterCmd);
        masterDataRepository.saveCountryMasterData(masterList);
        UpdateCountryMasterCmd updateCountryMasterCmd= UpdateCountryMasterCmd.builder().code("02").createdBy("User2").build();
        Long masterId=0l;
        boolean result=masterDataRepository.updateCountryMasterData(masterId,updateCountryMasterCmd);

        assertFalse(result);
    }

    @Test
    @Order(20)
    void should_return_success_response_on_country_master_delete(){
        CreateCountryMasterCmd countryMasterCmd= CreateCountryMasterCmd.builder().code("01").createdBy("User1").build();
        List<CreateCountryMasterCmd> masterList= Collections.singletonList(countryMasterCmd);
        masterDataRepository.saveCountryMasterData(masterList);
        boolean result=masterDataRepository.deleteCountryMasterData(1L);
        assertTrue(result);
    }

    @Test
    @Order(21)
    void should_return_failure_response_when_country_master_is_not_deleted(){
        CreateCountryMasterCmd countryMasterCmd= CreateCountryMasterCmd.builder().code("01").createdBy("User1").build();
        List<CreateCountryMasterCmd> masterList= Collections.singletonList(countryMasterCmd);
        masterDataRepository.saveCountryMasterData(masterList);
        boolean result=masterDataRepository.deleteLookUpMasterData(0L);
        assertFalse(result);
    }
}
