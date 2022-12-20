package com.finx.onboardingservice.db;

import com.finx.onboardingservice.core.onboarding.domain.WorkflowStageMaster;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface WorkflowStageMasterRepository {

    @SqlQuery("SELECT * FROM wf_stage_master where wf_stage_id = :wfStageId")
    @RegisterBeanMapper(WorkflowStageMaster.class)
    WorkflowStageMaster getWorkflowStage(@Bind("wfStageId") int wfStageId);
}
