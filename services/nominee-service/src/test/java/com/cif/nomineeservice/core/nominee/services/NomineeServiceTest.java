package com.cif.nomineeservice.core.nominee.services;

import com.cif.nomineeservice.api.CreateNomineeRequest;
import com.cif.nomineeservice.api.NomineeMappingRequest;
import com.cif.nomineeservice.api.UpdateNomineeRequest;
import com.cif.nomineeservice.core.nominee.domain.Nominee;
import com.cif.nomineeservice.db.NomineeRepository;
import com.cif.nomineeservice.resources.exceptions.RecordNotFoundException;
import com.cif.nomineeservice.resources.exceptions.RecordPersistException;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NomineeServiceTest {

    private final NomineeRepository mockNomineeRepository = mock(NomineeRepository.class);

    private final NomineeService mockNomineeService = new NomineeService(mockNomineeRepository);

    @Test
    void should_return_success_response_when_nominees_persist_success() {
        var nominee = CreateNomineeRequest.builder().build();
        var generatedNomineeId = 1;
        var nomineeResponse = new Nominee();
        when(mockNomineeRepository.save(nominee)).thenReturn(generatedNomineeId);
        when(mockNomineeRepository.find(generatedNomineeId)).thenReturn(nomineeResponse);

        var result = mockNomineeService.savePartyNominees(nominee);

        assertEquals(HttpStatus.CREATED_201, result.getStatus());
        verify(mockNomineeRepository, times(1)).save(nominee);
        verify(mockNomineeRepository, times(1)).find(generatedNomineeId);
    }

    @Test
    void should_return_failure_response_when_nominees_persist_failed() {
        var nominee = CreateNomineeRequest.builder().build();
        when(mockNomineeRepository.save(nominee)).thenReturn(0);

        var result = mockNomineeService.savePartyNominees(nominee);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, result.getStatus());
        verify(mockNomineeRepository, times(1)).save(nominee);
    }

    @Test
    void should_return_exception_response_when_exception_occurred_while_nominees_persist() {
        var nominee = CreateNomineeRequest.builder().build();
        when(mockNomineeRepository.save(nominee)).thenThrow(new RuntimeException());

        RecordPersistException recordPersistException = assertThrows(RecordPersistException.class, () -> mockNomineeService.savePartyNominees(nominee), "Failed to create party nominee");

        assertEquals("Failed to create party nominee", recordPersistException.getMessage());
        verify(mockNomineeRepository, times(1)).save(nominee);
    }

    @Test
    void should_return_success_response_when_nominees_update_success() {
        var nominee = UpdateNomineeRequest.builder().nomineeId(1).build();
        var updatedNominee = new Nominee();
        updatedNominee.setNomineeId(1);

        var updateStatus = true;
        var nomineeResponse = new Nominee();
        when(mockNomineeRepository.updateNominee(Mockito.any())).thenReturn(updateStatus);
        when(mockNomineeRepository.find(nominee.getNomineeId())).thenReturn(nomineeResponse);

        var result = mockNomineeService.updateNominee(nominee);

        assertEquals(HttpStatus.OK_200, result.getStatus());
        verify(mockNomineeRepository, times(1)).updateNominee(Mockito.any());
        verify(mockNomineeRepository, times(1)).find(nominee.getNomineeId());
    }

    @Test
    void should_return_failure_response_when_nominees_update_failed() {
        var nominee = UpdateNomineeRequest.builder().nomineeId(1).build();
        var updatedNominee = new Nominee();
        updatedNominee.setNomineeId(1);
        var updateStatus = false;
        var nomineeResponse = new Nominee();
        when(mockNomineeRepository.find(nominee.getNomineeId())).thenReturn(nomineeResponse);
        when(mockNomineeRepository.updateNominee(Mockito.any())).thenReturn(updateStatus);

        var result = mockNomineeService.updateNominee(nominee);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, result.getStatus());
        verify(mockNomineeRepository, times(1)).updateNominee(Mockito.any());
    }

    @Test
    void should_return_exception_response_when_exception_occurred_while_nominees_update() {
        var nominee = UpdateNomineeRequest.builder().nomineeId(1).build();
        var updatedNominee = new Nominee();
        updatedNominee.setNomineeId(1);
        when(mockNomineeRepository.find(nominee.getNomineeId())).thenReturn(new Nominee());
        when(mockNomineeRepository.updateNominee(Mockito.any())).thenThrow(new RuntimeException());

        RecordPersistException recordPersistException = assertThrows(RecordPersistException.class, () -> mockNomineeService.updateNominee(nominee), "Failed to update party nominee");

        assertEquals("Failed to update party nominee", recordPersistException.getMessage());
        verify(mockNomineeRepository, times(1)).updateNominee(Mockito.any());
    }

    @Test
    void should_return_success_response_when_nominees_fetch_success() {
        var partyId = 1;
        var nomineeId = 1;
        var nomineeRecord = new Nominee();
        when(mockNomineeRepository.findByPartyId(partyId)).thenReturn(Collections.singleton(nomineeRecord));
        when(mockNomineeRepository.find(partyId)).thenReturn(nomineeRecord);

        var fetchByPartyIdResult = mockNomineeService.fetchNomineeDetailsByPartyIdOrNomineeId(partyId, 0);
        var fetchByNomineeIdResult = mockNomineeService.fetchNomineeDetailsByPartyIdOrNomineeId(0, nomineeId);

        assertEquals(HttpStatus.OK_200, fetchByPartyIdResult.getStatus());
        assertEquals(HttpStatus.OK_200, fetchByNomineeIdResult.getStatus());
        verify(mockNomineeRepository, times(1)).findByPartyId(partyId);
        verify(mockNomineeRepository, times(1)).find(nomineeId);
    }

    @Test
    void should_return_exception_response_when_exception_occurred_while_nominees_fetch() {
        var nomineeId = 1;
        when(mockNomineeRepository.find(nomineeId)).thenThrow(new RuntimeException());

        RecordNotFoundException recordPersistException = assertThrows(RecordNotFoundException.class, () -> mockNomineeService.fetchNomineeDetailsByPartyIdOrNomineeId(0, nomineeId), "Failed to fetch details for nominee");

        assertEquals("Failed to fetch details for nominee", recordPersistException.getMessage());
        verify(mockNomineeRepository, times(1)).find(nomineeId);
    }

    @Test
    void should_return_exception_response_when_exception_occurred_while_nominees_fetchAll() {
        var partyId = 1;
        when(mockNomineeRepository.findByPartyId(partyId)).thenThrow(new RuntimeException());

        RecordNotFoundException recordPersistException = assertThrows(RecordNotFoundException.class, () -> mockNomineeService.fetchNomineeDetailsByPartyIdOrNomineeId(partyId, 0), "Failed to fetch details for nominee");

        assertEquals("Failed to fetch details for nominee", recordPersistException.getMessage());
        verify(mockNomineeRepository, times(1)).findByPartyId(partyId);
    }

    @Test
    void should_return_success_response_when_nominees_delete_success() {
        var nomineeId = 1;
        var deleteStatus = true;
        when(mockNomineeRepository.delete(nomineeId)).thenReturn(deleteStatus);

        var result = mockNomineeService.deleteNominee(nomineeId);

        assertEquals(HttpStatus.OK_200, result.getStatus());
        verify(mockNomineeRepository, times(1)).delete(nomineeId);
    }

    @Test
    void should_return_failure_response_when_nominees_delete_failed() {
        var nomineeId = 1;
        var deleteStatus = false;
        when(mockNomineeRepository.delete(nomineeId)).thenReturn(deleteStatus);

        var result = mockNomineeService.deleteNominee(nomineeId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, result.getStatus());
        verify(mockNomineeRepository, times(1)).delete(nomineeId);
    }

    @Test
    void should_return_exception_response_when_exception_occurred_while_nominees_delete() {
        var nomineeId = 1;
        when(mockNomineeRepository.delete(nomineeId)).thenThrow(new RuntimeException());

        RecordPersistException recordPersistException = assertThrows(RecordPersistException.class, () -> mockNomineeService.deleteNominee(nomineeId), "Failed to delete party nominee");

        assertEquals("Failed to delete party nominee", recordPersistException.getMessage());
        verify(mockNomineeRepository, times(1)).delete(nomineeId);
    }

    @Test
    void should_return_success_when_nominee_mapping_persist_success() {
        NomineeMappingRequest nomineeMappingRequest = NomineeMappingRequest.builder().nomineeId(1).partyId(1).accountNumber("70149340835549500").build();
        Nominee nominee = new Nominee();
        nominee.setPartyId(1);

        when(mockNomineeRepository.find(nomineeMappingRequest.getNomineeId())).thenReturn(nominee);
        when(mockNomineeRepository.saveNomineeMapping(nomineeMappingRequest)).thenReturn(true);
        Response actualResponse = mockNomineeService.saveNomineeMapping(nomineeMappingRequest);

        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(mockNomineeRepository, times(1)).find(nomineeMappingRequest.getNomineeId());
        verify(mockNomineeRepository, times(1)).saveNomineeMapping(nomineeMappingRequest);
    }

    @Test
    void should_return_error_when_nominee_mapping_persist_failed() {
        NomineeMappingRequest nomineeMappingRequest = NomineeMappingRequest.builder().nomineeId(1).partyId(1).accountNumber("70149340835549500").build();

        when(mockNomineeRepository.find(nomineeMappingRequest.getNomineeId())).thenReturn(null);
        Response actualResponse = mockNomineeService.saveNomineeMapping(nomineeMappingRequest);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockNomineeRepository, times(1)).find(nomineeMappingRequest.getNomineeId());
    }
    @Test
    void should_failure_when_exception_occur_nominee_mapping_is_saved() {
        NomineeMappingRequest nomineeMappingRequest = NomineeMappingRequest.builder().nomineeId(1).partyId(1).accountNumber("70149340835549500").build();

        when(mockNomineeRepository.find(nomineeMappingRequest.getNomineeId())).thenThrow(new RuntimeException());
        RecordPersistException recordPersistException = assertThrows(RecordPersistException.class, () -> mockNomineeService.saveNomineeMapping(nomineeMappingRequest), "Failed to create nominee mapping");

        assertEquals("Failed to create nominee mapping", recordPersistException.getMessage());
        verify(mockNomineeRepository, times(1)).find(nomineeMappingRequest.getNomineeId());
    }

    @Test
    void should_return_success_when_delete_nominee_mapping() {
        when(mockNomineeRepository.deleteNomineeMapping(1l)).thenReturn(true);
        Response actualResponse = mockNomineeService.deleteNomineeMapping(1l);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockNomineeRepository, times(1)).deleteNomineeMapping(1l);
    }

    @Test
    void should_return_error_when_delete_nominee_mapping_failed() {
        when(mockNomineeRepository.deleteNomineeMapping(1l)).thenReturn(false);
        Response actualResponse = mockNomineeService.deleteNomineeMapping(1l);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockNomineeRepository, times(1)).deleteNomineeMapping(1l);
    }
    @Test
    void should_return_failure_when_exception_occur_during_delete_nominee_mapping() {
        when(mockNomineeRepository.deleteNomineeMapping(1l)).thenThrow(new RuntimeException());

        RecordPersistException recordPersistException = assertThrows(RecordPersistException.class, () -> mockNomineeService.deleteNomineeMapping(1l), "Failed to delete nominee mapping");

        assertEquals("Failed to delete nominee mapping", recordPersistException.getMessage());
        verify(mockNomineeRepository, times(1)).deleteNomineeMapping(1l);
    }
}