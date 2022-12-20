package com.finx.onboardingservice.db;

import com.finx.onboardingservice.core.onboarding.domain.WorkflowMaster;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface WorkflowMasterRepository {

    @SqlQuery("SELECT * FROM wf_master where wf_def_id = :wfDefId")
    @RegisterBeanMapper(WorkflowMaster.class)
    WorkflowMaster getWorkflow(@Bind("wfDefId") int wfDefId);
}
