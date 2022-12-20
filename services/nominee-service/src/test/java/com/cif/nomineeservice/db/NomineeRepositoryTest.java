package com.cif.nomineeservice.db;

import com.cif.nomineeservice.api.CreateNomineeRequest;
import com.cif.nomineeservice.api.NomineeMappingRequest;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NomineeRepositoryTest extends JdbiTest {

    private NomineeRepository  nomineeRepository;

    @BeforeAll
    void setup(){
        nomineeRepository = jdbi.onDemand(NomineeRepository.class);
    }

    @Test
    @Order(1)
    void should_return_success_response_when_nominee_is_persist() {
        CreateNomineeRequest nomineeRequest = CreateNomineeRequest.builder().partyId(1).firstName("Ramesh").lastName("Kumar").nationalId("01").relation(CreateNomineeRequest.RelationEnum.BROTHER).salutation(CreateNomineeRequest.SalutationEnum.MR_).build();
        var id = nomineeRepository.save(nomineeRequest);
        assertThat(id).isPositive();
    }

    @Test
    @Order(2)
    void should_return_true_when_nominee_mapping_is_persist() {
        NomineeMappingRequest nomineeMappingRequest = NomineeMappingRequest.builder().nomineeId(1).partyId(1).accountNumber("70149340835549500").build();
        var result = nomineeRepository.saveNomineeMapping(nomineeMappingRequest);
        assertTrue(result);
    }

    @Test
    @Order(3)
    void should_return_true_when_nominee_mapping_is_deleted()  {
        boolean result = nomineeRepository.deleteNomineeMapping(1l);
        assertTrue(result);
    }

    @Test
    @Order(4)
    void should_return_false_when_nominee_mapping_is_not_deleted() {
        boolean result = nomineeRepository.deleteNomineeMapping(0l);
        assertFalse(result);
    }

}
