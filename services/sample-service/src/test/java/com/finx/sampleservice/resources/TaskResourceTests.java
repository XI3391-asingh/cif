package com.finx.sampleservice.resources;

import ch.qos.logback.classic.Level;
import com.codahale.metrics.MetricRegistry;
import com.finx.sampleservice.api.CreateTaskResponse;
import com.finx.sampleservice.api.CreateTaskCmd;
import com.finx.sampleservice.core.task.domain.Task;
import com.finx.sampleservice.core.task.services.TaskService;
import com.finx.sampleservice.resources.exceptionmappers.FailedToCreateTaskExceptionMapper;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(DropwizardExtensionsSupport.class)
class TaskResourceTests {

    private final TaskService mockTaskService = mock(TaskService.class);
    private final ResourceExtension resourceTestClient =
            ResourceExtension.builder()
                    .addResource(new TaskResource(mockTaskService))
                    .addProvider(() ->
                            new FailedToCreateTaskExceptionMapper(new MetricRegistry()))
                    .build();

    static {
        BootstrapLogging.bootstrap(Level.INFO);
    }

    @Test
    void should_create_task_successfully() {
        when(mockTaskService.create(CreateTaskCmd.builder().name("name").build()))
                .thenReturn(new Task("name"));
        Entity<CreateTaskCmd> entity = Entity.json(CreateTaskCmd.builder().name("name").build());
        final Response response = resourceTestClient.target("/tasks")
                .request()
                .post(entity);
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        var r = response.readEntity(CreateTaskResponse.class);
        assertThat(r.getId()).isNotNull();
        assertThat(r.getName()).isEqualTo("name");
    }

    @Test
    void should_return_failure_response_when_fail_to_create_task() {
        when(mockTaskService.create(CreateTaskCmd.builder().name("name").build()))
                .thenThrow(new RuntimeException());

        Entity<CreateTaskCmd> entity = Entity.json(CreateTaskCmd.builder().name("name").build());
        final Response response = resourceTestClient.target("/tasks")
                .request()
                .post(entity);
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(response.readEntity(ErrorMessage.class).getMessage())
                .isEqualTo("Failed to create task");
    }
}