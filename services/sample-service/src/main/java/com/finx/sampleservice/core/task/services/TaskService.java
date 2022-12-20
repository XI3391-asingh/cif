package com.finx.sampleservice.core.task.services;

import com.finx.sampleservice.api.CreateTaskCmd;
import com.finx.sampleservice.core.task.domain.Task;
import com.finx.sampleservice.db.TaskRepository;

public class TaskService {

    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task create(CreateTaskCmd cmd) {
        var task = new Task(cmd.getName());
        return this.taskRepository.save(task);
    }
}
