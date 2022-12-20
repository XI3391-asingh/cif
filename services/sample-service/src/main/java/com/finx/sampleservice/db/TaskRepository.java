package com.finx.sampleservice.db;

import com.finx.sampleservice.core.task.domain.Task;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface TaskRepository {

    @SqlUpdate("INSERT INTO tasks (id, name) VALUES (:id, :name)")
    @GetGeneratedKeys
    @RegisterBeanMapper(Task.class)
    Task save(@BindBean Task workflow);
}
