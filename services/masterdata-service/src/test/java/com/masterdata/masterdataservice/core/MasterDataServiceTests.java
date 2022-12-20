package com.masterdata.masterdataservice.core;

import com.masterdata.masterdataservice.api.*;
import com.masterdata.masterdataservice.core.masterdata.services.MasterDataService;
import com.masterdata.masterdataservice.db.MasterDataRepository;
import com.masterdata.masterdataservice.resources.exceptions.DatabasePersistException;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class MasterDataServiceTests {
    @Mock
    private AutoCloseable closeable;

    private final MasterDataRepository masterDataRepository = mock(MasterDataRepository.class);

    private final MasterDataService masterDataService = new MasterDataService(masterDataRepository);

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void should_return_success_when_master_data_is_saved() {
        CreateMasterCmd createMasterCmd = CreateMasterCmd.builder().code("01").dtType(CreateMasterCmd.DtTypeEnum.ADDRESS_MASTER).build();
        List<CreateMasterCmd> masterList = Collections.singletonList(createMasterCmd);
        ViewMasterDataCmd masterDataCmd = ViewMasterDataCmd.builder().type("ADDRESS_MASTER").code("01").build();
        Set<ViewMasterDataCmd> masterDataCmdSet = Collections.singleton(masterDataCmd);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.TRUE);

        when(masterDataRepository.saveAddressMasterData(masterList)).thenReturn(array);
        when(masterDataRepository.findAllAddressMasterData()).thenReturn(masterDataCmdSet);
        Response actualResponse = masterDataService.saveMasterData(masterList);

        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(masterDataRepository, times(1)).saveAddressMasterData(masterList);
        verify(masterDataRepository, times(1)).findAllAddressMasterData();
    }

    @Test
    void should_return_failure_when_exception_occur_during_master_data_save() {
        CreateMasterCmd createMasterCmd = CreateMasterCmd.builder().code("01").dtType(CreateMasterCmd.DtTypeEnum.LOOKUP_MASTER).build();
        List<CreateMasterCmd> masterList = Collections.singletonList(createMasterCmd);

        when(masterDataRepository.saveLookUpMasterData(masterList)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> masterDataService.saveMasterData(masterList), "Error occurred while creating master data. Please contact the system administrator ! ");

        assertEquals("Error occurred while creating master data. Please contact the system administrator ! ", databasePersistException.getMessage());
        verify(masterDataRepository, times(1)).saveLookUpMasterData(masterList);
    }

    @Test
    void should_return_success_when_master_data_is_updated() {
        UpdateMasterDataRequestCmd updateMasterCmd = UpdateMasterDataRequestCmd.builder().code("01").dtType(UpdateMasterDataRequestCmd.DtTypeEnum.ADDRESS_MASTER).build();
        Long masterId = 1l;

        when(masterDataRepository.updateAddressMasterData(masterId, updateMasterCmd)).thenReturn(true);
        Response actualResponse = masterDataService.updateMasterData(masterId, updateMasterCmd);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(masterDataRepository, times(1)).updateAddressMasterData(masterId, updateMasterCmd);
    }

    @Test
    void should_return_success_when_master_data_is_not_updated() {
        UpdateMasterDataRequestCmd updateMasterCmd = UpdateMasterDataRequestCmd.builder().code("01").dtType(UpdateMasterDataRequestCmd.DtTypeEnum.ADDRESS_MASTER).build();
        Long masterId = 0l;

        when(masterDataRepository.updateAddressMasterData(masterId, updateMasterCmd)).thenReturn(false);
        Response actualResponse = masterDataService.updateMasterData(masterId, updateMasterCmd);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(masterDataRepository, times(1)).updateAddressMasterData(masterId, updateMasterCmd);
    }

    @Test
    void should_return_failure_when_exception_occur_during_master_data_update() {
        UpdateMasterDataRequestCmd updateMasterCmd = UpdateMasterDataRequestCmd.builder().code("01").dtType(UpdateMasterDataRequestCmd.DtTypeEnum.ADDRESS_MASTER).build();
        Long masterId = 1l;

        when(masterDataRepository.updateAddressMasterData(masterId, updateMasterCmd)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> masterDataService.updateMasterData(masterId, updateMasterCmd), "Error occurred while update record. Please contact the system administrator ! ");

        assertEquals("Error occurred while update record. Please contact the system administrator ! ", databasePersistException.getMessage());
        verify(masterDataRepository, times(1)).updateAddressMasterData(masterId, updateMasterCmd);
    }

    @Test
    void should_return_success_when_fetch_master_data() {
        ViewMasterDataCmd masterDataCmd = ViewMasterDataCmd.builder().type("ADDRESS_TYPE").code("01").build();
        Set<ViewMasterDataCmd> masterDataCmdSet = Collections.singleton(masterDataCmd);
        String masterDataType = "ADDRESS_MASTER";

        when(masterDataRepository.findAllAddressMasterData()).thenReturn(masterDataCmdSet);
        Response actualResponse = masterDataService.fetchAllMasterData(masterDataType);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(masterDataRepository, times(1)).findAllAddressMasterData();
    }

    @Test
    void should_return_failure_when_no_master_data_is_fetched() {
        String masterDataType = "ADDRESS_MASTER";

        when(masterDataRepository.findAllAddressMasterData()).thenReturn(new HashSet<>());
        Response actualResponse = masterDataService.fetchAllMasterData(masterDataType);

        assertEquals(HttpStatus.NO_CONTENT_204, actualResponse.getStatus());
        verify(masterDataRepository, times(1)).findAllAddressMasterData();
    }

    @Test
    void should_return_failure_when_exception_occur_during_master_data_fetch() {
        String masterDataType = "ADDRESS_MASTER";

        when(masterDataRepository.findAllAddressMasterData()).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> masterDataService.fetchAllMasterData(masterDataType), "Error occurred while fetch master data.Please contact the system administrator!");

        assertEquals("Error occurred while fetch master data.Please contact the system administrator!", databasePersistException.getMessage());
        verify(masterDataRepository, times(1)).findAllAddressMasterData();
    }

    @Test
    void should_return_success_when_master_data_is_deleted() {
        String masterDataType = "ADDRESS_MASTER";
        Long masterDataId = 1l;

        when(masterDataRepository.deleteAddressMasterData(masterDataId)).thenReturn(true);
        Response actualResponse = masterDataService.deleteMasterData(masterDataId, masterDataType);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(masterDataRepository, times(1)).deleteAddressMasterData(masterDataId);
    }

    @Test
    void should_return_failure_when_master_data_is_not_deleted() {
        String masterDataType = "LOOKUP_MASTER";
        Long masterDataId = 0l;

        when(masterDataRepository.deleteLookUpMasterData(masterDataId)).thenReturn(false);
        Response actualResponse = masterDataService.deleteMasterData(masterDataId, masterDataType);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(masterDataRepository, times(1)).deleteLookUpMasterData(masterDataId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_delete_master_data() {
        String masterDataType = "ADDRESS_MASTER";
        Long masterDataId = 0l;

        when(masterDataRepository.deleteAddressMasterData(masterDataId)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> masterDataService.deleteMasterData(masterDataId, masterDataType), "Error occurred while deleting master data record. Please contact the system administrator!");

        assertEquals("Error occurred while deleting master data record. Please contact the system administrator!", databasePersistException.getMessage());
        verify(masterDataRepository, times(1)).deleteAddressMasterData(masterDataId);
    }


    void should_return_success_when_country_master_data_is_saved() {
        CreateCountryMasterCmd createCountryMasterCmd = CreateCountryMasterCmd.builder().code("02").createdBy("User1").build();
        List<CreateCountryMasterCmd> countryMasterList = Collections.singletonList(createCountryMasterCmd);
        ViewCountryMasterDataCmd viewCountryMasterDataCmd = ViewCountryMasterDataCmd.builder().createdBy("User1").code("01").build();
        Set<ViewCountryMasterDataCmd> countryMasterDataCmdSet = Collections.singleton(viewCountryMasterDataCmd);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.TRUE);

        when(masterDataRepository.saveCountryMasterData(countryMasterList)).thenReturn(array);
        when(masterDataRepository.findAllCountryMasterData()).thenReturn(countryMasterDataCmdSet);
        Response actualResponse = masterDataService.saveCountryMasterData(countryMasterList);

        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(masterDataRepository, times(1)).saveCountryMasterData(countryMasterList);
        verify(masterDataRepository, times(1)).findAllCountryMasterData();

    }

    void should_return_failure_when_exception_occur_during_country_master_data_save() {
        CreateCountryMasterCmd createCountryMasterCmd = CreateCountryMasterCmd.builder().code("01").createdBy("User1").build();
        List<CreateCountryMasterCmd> countryMasterList = Collections.singletonList(createCountryMasterCmd);

        when(masterDataRepository.saveCountryMasterData(countryMasterList)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> masterDataService.saveCountryMasterData(countryMasterList), "Error occurred while creating country master data. Please contact the system administrator ! ");

        assertEquals("Error occurred while creating country master data. Please contact the system administrator ! ", databasePersistException.getMessage());
        verify(masterDataRepository, times(1)).saveCountryMasterData(countryMasterList);
    }

    @Test
    void should_return_success_when_fetch_country_master_data() {
        ViewCountryMasterDataCmd viewCountryMasterDataCmd = ViewCountryMasterDataCmd.builder().createdBy("User1").code("01").build();
        Set<ViewCountryMasterDataCmd> countryMasterDataCmdSet = Collections.singleton(viewCountryMasterDataCmd);

        when(masterDataRepository.findAllCountryMasterData()).thenReturn(countryMasterDataCmdSet);
        Response actualResponse = masterDataService.fetchAllCountryMasterData();

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(masterDataRepository, times(1)).findAllCountryMasterData();
    }

    @Test
    void should_return_failure_when_no_country_master_data_is_fetched() {
        when(masterDataRepository.findAllCountryMasterData()).thenReturn(new HashSet<>());
        Response actualResponse = masterDataService.fetchAllCountryMasterData();

        assertEquals(HttpStatus.NO_CONTENT_204, actualResponse.getStatus());
        verify(masterDataRepository, times(1)).findAllCountryMasterData();
    }

    @Test
    void should_return_failure_when_exception_occur_during_country_master_data_fetch() {
        when(masterDataRepository.findAllCountryMasterData()).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> masterDataService.fetchAllCountryMasterData(), "Error occurred while fetch country master data.Please contact the system administrator!");

        assertEquals("Error occurred while fetch country master data.Please contact the system administrator!", databasePersistException.getMessage());
        verify(masterDataRepository, times(1)).findAllCountryMasterData();
    }

    @Test
    void should_return_success_when_country_master_data_is_updated() {
        UpdateCountryMasterCmd updateCountryMasterCmd = UpdateCountryMasterCmd.builder().code("01").createdBy("User1").build();
        Long masterId = 1l;

        when(masterDataRepository.updateCountryMasterData(masterId, updateCountryMasterCmd)).thenReturn(true);
        Response actualResponse = masterDataService.updateCountryMasterData(masterId, updateCountryMasterCmd);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(masterDataRepository, times(1)).updateCountryMasterData(masterId, updateCountryMasterCmd);
    }

    @Test
    void should_return_success_when_country_master_data_is_not_updated() {
        UpdateCountryMasterCmd updateCountryMasterCmd = UpdateCountryMasterCmd.builder().code("01").createdBy("User1").build();
        Long masterId = 0l;

        when(masterDataRepository.updateCountryMasterData(masterId, updateCountryMasterCmd)).thenReturn(false);
        Response actualResponse = masterDataService.updateCountryMasterData(masterId, updateCountryMasterCmd);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(masterDataRepository, times(1)).updateCountryMasterData(masterId, updateCountryMasterCmd);
    }

    @Test
    void should_return_failure_when_exception_occur_during_country_master_data_update() {
        UpdateCountryMasterCmd updateCountryMasterCmd = UpdateCountryMasterCmd.builder().code("01").createdBy("User1").build();
        Long masterId = 1l;

        when(masterDataRepository.updateCountryMasterData(masterId, updateCountryMasterCmd)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> masterDataService.updateCountryMasterData(masterId, updateCountryMasterCmd), "Error occurred while update record. Please contact the system administrator ! ");

        assertEquals("Error occurred while update record. Please contact the system administrator ! ", databasePersistException.getMessage());
        verify(masterDataRepository, times(1)).updateCountryMasterData(masterId, updateCountryMasterCmd);
    }

    @Test
    void should_return_success_when_country_master_data_is_deleted() {
        Long masterDataId = 1l;

        when(masterDataRepository.deleteCountryMasterData(masterDataId)).thenReturn(true);
        Response actualResponse = masterDataService.deleteCountryMasterData(masterDataId);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(masterDataRepository, times(1)).deleteCountryMasterData(masterDataId);
    }

    @Test
    void should_return_failure_when_country_master_data_is_not_deleted() {
        Long masterDataId = 0l;

        when(masterDataRepository.deleteCountryMasterData(masterDataId)).thenReturn(false);
        Response actualResponse = masterDataService.deleteCountryMasterData(masterDataId);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(masterDataRepository, times(1)).deleteCountryMasterData(masterDataId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_delete_country_master_data() {
        Long masterDataId = 0l;

        when(masterDataRepository.deleteCountryMasterData(masterDataId)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> masterDataService.deleteCountryMasterData(masterDataId), "Error occurred while deleting country master data record. Please contact the system administrator!");

        assertEquals("Error occurred while deleting country master data record. Please contact the system administrator!", databasePersistException.getMessage());
        verify(masterDataRepository, times(1)).deleteCountryMasterData(masterDataId);
    }
}
