package com.finx.onboardingservice.db;

import com.finx.onboardingservice.core.onboarding.domain.WorkflowMaster;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WorkflowMasterRepositoryTest extends JdbiTest{

    @Test
    void should_return_record_from_workflow_master() {
        WorkflowMasterRepository workflowMasterRepository = jdbi.onDemand(WorkflowMasterRepository.class);
        WorkflowMaster workflowMaster = workflowMasterRepository.getWorkflow(101);
        assertThat(workflowMaster.getWfName()).isEqualTo("Onboarding");
        assertThat(workflowMaster.getWfDesc()).isEqualTo("To onboard a customer");
        assertThat(workflowMaster.getCreatedBy()).isEqualTo("Finx");
    }
}
