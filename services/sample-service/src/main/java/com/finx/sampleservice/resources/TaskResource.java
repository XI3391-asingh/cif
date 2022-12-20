package com.finx.sampleservice.resources;

import com.codahale.metrics.annotation.ResponseMetered;
import com.finx.sampleservice.api.CreateTaskCmd;
import com.finx.sampleservice.api.TodosApi;
import com.finx.sampleservice.core.task.services.TaskService;
import com.finx.sampleservice.resources.exceptions.FailedToCreateTask;
import com.finx.sampleservice.api.CreateTaskResponse;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class TaskResource implements TodosApi {

    private TaskService taskService;

    public TaskResource(TaskService taskService) {
        this.taskService = taskService;
    }

    @POST
    @ResponseMetered(name = "createTask")
    public Response create(@NotNull @Valid CreateTaskCmd cmd) {
        try {
            log.info("Command received to create a new Task [cmd={}]", cmd);
            var created = taskService.create(cmd);
            log.info("Task created [cmd={},id={}]", cmd, created.getId());
            var createdEntity = CreateTaskResponse.builder()
                    .id(created.getId().toString())
                    .name(created.getName())
                    .build();
            return Response.status(Response.Status.CREATED)
                    .entity(createdEntity)
                    .build();
        } catch (Exception e) {
            log.error("Failed to create Task for [cmd={}]", cmd, e);
            throw new FailedToCreateTask(
                    "Failed to create task",
                    e);
        }
    }
}
