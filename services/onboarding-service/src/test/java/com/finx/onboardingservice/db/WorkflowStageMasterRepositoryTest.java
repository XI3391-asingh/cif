package com.finx.onboardingservice.db;

import com.finx.onboardingservice.core.onboarding.domain.WorkflowStageMaster;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WorkflowStageMasterRepositoryTest extends JdbiTest {

    @Test
    void should_return_workflow_stage_details() {
        WorkflowStageMasterRepository workflowStageMasterRepository = jdbi.onDemand(WorkflowStageMasterRepository.class);
        WorkflowStageMaster verifyMobileStage = workflowStageMasterRepository.getWorkflowStage(1);
        assertThat(verifyMobileStage.getStageName()).isEqualTo("verify-mobile");
        assertThat(verifyMobileStage.getWfDefId()).isEqualTo(101);
        assertThat(verifyMobileStage.getStageDesc()).isEqualTo("Mobile verification stage");
        assertThat(verifyMobileStage.getOrderStage()).isEqualTo(1);

        WorkflowStageMaster kycStage = workflowStageMasterRepository.getWorkflowStage(2);
        assertThat(kycStage.getStageName()).isEqualTo("KYC");
        assertThat(kycStage.getWfDefId()).isEqualTo(101);
        assertThat(kycStage.getStageDesc()).isEqualTo("Know your customer stage");
        assertThat(kycStage.getOrderStage()).isEqualTo(2);

        WorkflowStageMaster liveStage = workflowStageMasterRepository.getWorkflowStage(3);
        assertThat(liveStage.getStageName()).isEqualTo("LIVE");
        assertThat(liveStage.getWfDefId()).isEqualTo(101);
        assertThat(liveStage.getStageDesc()).isEqualTo("Liveliness check stage");
        assertThat(liveStage.getOrderStage()).isEqualTo(3);
    }
}
