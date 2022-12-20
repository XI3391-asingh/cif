package com.cif.cifservice.core.party.services;

import com.cif.cifservice.core.party.domain.Config;
import com.cif.cifservice.core.party.util.BulkImportConfig;
import com.cif.cifservice.core.party.util.ConfigType;
import com.cif.cifservice.db.PartyRepository;
import com.cif.cifservice.core.party.util.CryptoUtil;
import com.cif.cifservice.db.elasticsearch.ElasticsearchAdminRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class AdminServiceTests {

    private final ElasticsearchAdminRepository adminRepository = mock(ElasticsearchAdminRepository.class);
    private final CryptoUtil cryptoUtil = mock(CryptoUtil.class);
    private final PartyRepository partyRepository = mock(PartyRepository.class);

    private final BulkImportConfig bulkImportConfig = mock(BulkImportConfig.class);
    private AdminService adminService = new AdminService(adminRepository, cryptoUtil, partyRepository, bulkImportConfig);
    private final ObjectMapper MAPPER = new ObjectMapper();
    private Config config;

    @BeforeEach
    void steptup() throws IOException {
        config = MAPPER.readValue(getClass().getResource("/fixtures/CreateAdmin.json"), Config.class);
    }

    @Mock
    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void should_return_success_when_config_record_is_saved() {
        when(adminRepository.addConfigRecord(config)).thenReturn(201);
        when(cryptoUtil.getConfigurationFlag()).thenReturn(new AtomicBoolean(false));
        when(cryptoUtil.getConfigurationSearchFlag()).thenReturn(new AtomicBoolean(false));
        Response actualResponse = adminService.save(config);

        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(adminRepository, times(1)).addConfigRecord(config);
    }

    @Test
    void should_throw_error_when_config_is_not_save() {
        when(adminRepository.addConfigRecord(config)).thenReturn(400);
        when(cryptoUtil.getConfigurationFlag()).thenReturn(new AtomicBoolean(false));
        when(cryptoUtil.getConfigurationSearchFlag()).thenReturn(new AtomicBoolean(false));
        Response actualResponse = adminService.save(config);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(adminRepository, times(1)).addConfigRecord(config);
    }

    @Test
    void should_return_success_when_config_record_is_updated() {
        when(adminRepository.addConfigRecord(config)).thenReturn(200);
        when(cryptoUtil.getConfigurationFlag()).thenReturn(new AtomicBoolean(false));
        when(cryptoUtil.getConfigurationSearchFlag()).thenReturn(new AtomicBoolean(false));
        Response actualResponse = adminService.save(config);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(adminRepository, times(1)).addConfigRecord(config);
    }

    @Test
    void should_return_success_when_fetch_config_record() {
        when(adminRepository.fetchAdminConfigRecordBasedOnType("UNIVERSALSEARCHFIELDS")).thenReturn(config);
        Response actualResponse = adminService.fetchConfigByRecord("UNIVERSALSEARCHFIELDS");

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(adminRepository, times(1)).fetchAdminConfigRecordBasedOnType("UNIVERSALSEARCHFIELDS");
    }

    @Test
    void should_return_error_when_fetch_config_record_is_failed() {
        when(adminRepository.fetchAdminConfigRecordBasedOnType("UNIVERSALSEARCHF")).thenReturn(null);
        Response actualResponse = adminService.fetchConfigByRecord("UNIVERSALSEARCHF");

        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(adminRepository, times(1)).fetchAdminConfigRecordBasedOnType("UNIVERSALSEARCHF");
    }
}
