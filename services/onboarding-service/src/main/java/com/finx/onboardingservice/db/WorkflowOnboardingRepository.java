package com.finx.onboardingservice.db;

import com.finx.onboardingservice.core.onboarding.domain.WorkflowOnboarding;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface WorkflowOnboardingRepository {

    @SqlUpdate("INSERT INTO wf_onboarding (mobile_number, wf_def_id, wf_stage_id, "
            + " stage_status, reason, created_by, created_on) "
            + " VALUES (:mobileNumber, :wfDefId, :wfStageId, :stageStatus,"
            + " :reason, :createdBy, :createdOn)")
    @GetGeneratedKeys("wf_id")
    @RegisterBeanMapper(WorkflowOnboarding.class)
    WorkflowOnboarding save(@BindBean WorkflowOnboarding workflowOnboarding);

    @SqlQuery("SELECT * FROM wf_onboarding where wf_id = :wfId")
    @RegisterBeanMapper(WorkflowOnboarding.class)
    WorkflowOnboarding get(@Bind("wfId") int wfId);

    @SqlQuery("SELECT count(*) FROM wf_onboarding where mobile_number = :mobileNumber")
    int countByPhoneNumber(@Bind("mobileNumber") String mobileNumber);
}
