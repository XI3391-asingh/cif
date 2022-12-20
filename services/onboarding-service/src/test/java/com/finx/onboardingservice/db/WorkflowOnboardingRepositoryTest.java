package com.finx.onboardingservice.db;

import com.finx.onboardingservice.core.onboarding.domain.WorkflowOnboarding;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class WorkflowOnboardingRepositoryTest extends JdbiTest{

    @Test
    void should_create_workflow_onboarding() {
        WorkflowOnboardingRepository workflowOnboardingRepository = jdbi.onDemand(WorkflowOnboardingRepository.class);
        WorkflowOnboarding workflowOnboarding = new WorkflowOnboarding();
        int wf_id = RandomUtils.nextInt();
        workflowOnboarding.setWfId(wf_id);
        workflowOnboarding.setMobileNumber("+915625452712");
        workflowOnboarding.setCreatedBy("Finx");
        workflowOnboarding.setReason("Test_reason");
        workflowOnboarding.setWfStageId(1);
        workflowOnboarding.setWfDefId(101);
        workflowOnboarding.setStageStatus("PENDING");
        workflowOnboarding.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));

        var wo = workflowOnboardingRepository.save(workflowOnboarding);

        WorkflowOnboarding savedWorkflowOnboarding = jdbi.withHandle(handle ->
                handle.createQuery("select * from wf_onboarding where wf_id = :wf_id")
                        .bind("wf_id", wo.getWfId())
                        .map((rs, ctx) -> {
                            WorkflowOnboarding workflowOnboardingDB = new WorkflowOnboarding();
                            workflowOnboardingDB.setWfId(rs.getInt("wf_id"));
                            workflowOnboardingDB.setMobileNumber(rs.getString("mobile_number"));
                            workflowOnboardingDB.setWfDefId(rs.getInt("wf_def_id"));
                            workflowOnboardingDB.setWfStageId(rs.getInt("wf_stage_id"));
                            workflowOnboardingDB.setStageStatus(rs.getString("stage_status"));
                            workflowOnboardingDB.setReason(rs.getString("reason"));
                            workflowOnboardingDB.setCreatedBy(rs.getString("created_by"));
                            workflowOnboardingDB.setCreatedOn(rs.getTimestamp("created_on"));

                            return workflowOnboardingDB;
                        })
                        .first());

        assertThat(savedWorkflowOnboarding.getMobileNumber()).isEqualTo("+915625452712");
        assertThat(savedWorkflowOnboarding.getCreatedBy()).isEqualTo("Finx");
        assertThat(savedWorkflowOnboarding.getReason()).isEqualTo("Test_reason");
        assertThat(savedWorkflowOnboarding.getWfStageId()).isEqualTo(1);
        assertThat(savedWorkflowOnboarding.getWfDefId()).isEqualTo(101);

    }
}
