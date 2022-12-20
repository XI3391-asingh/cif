package com.cif.

{{cookiecutter.service_package}}.resources;

import ch.qos.logback.classic.Level;
import com.codahale.metrics.MetricRegistry;
import com.cif.{{cookiecutter.service_package}}.api.Create{{cookiecutter.entity}}Response;
import com.cif.{{cookiecutter.service_package}}.api.Create{{cookiecutter.entity}}Cmd;
import com.cif.{{cookiecutter.service_package}}.core.{{cookiecutter.domain_package}}.domain.{{cookiecutter.entity}};
import com.cif.{{cookiecutter.service_package}}.core.{{cookiecutter.domain_package}}.services.{{cookiecutter.entity}}Service;
import com.cif.{{cookiecutter.service_package}}.resources.exceptionmappers.FailedToCreate{{cookiecutter.entity}}ExceptionMapper;
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
class {{cookiecutter.entity}}ResourceTests{

private final {{cookiecutter.entity}}Service mock{{cookiecutter.entity}}Service=mock({{cookiecutter.entity}}Service.class);
private final ResourceExtension resourceTestClient=
        ResourceExtension.builder()
        .addResource(new{{cookiecutter.entity}}Resource(mock{{cookiecutter.entity}}Service))
        .addProvider(()->
        new FailedToCreate{{cookiecutter.entity}}ExceptionMapper(new MetricRegistry()))
        .build();

static {
        BootstrapLogging.bootstrap(Level.INFO);
        }

@Test
    void should_create_{{cookiecutter.entity.lower()}}_successfully(){
            when(mock{{cookiecutter.entity}}Service.create(Create{{cookiecutter.entity}}Cmd.builder().name("name").build()))
            .thenReturn(new{{cookiecutter.entity}}("name"));
            Entity<Create{{cookiecutter.entity}}Cmd>entity=Entity.json(Create{{cookiecutter.entity}}Cmd.builder().name("name").build());
final Response response=resourceTestClient.target("/{{ cookiecutter.entity.lower() + 's' }}")
        .request()
        .post(entity);
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        var r=response.readEntity(Create{{cookiecutter.entity}}Response.class);
        assertThat(r.getId()).isNotNull();
        assertThat(r.getName()).isEqualTo("name");
        }

@Test
    void should_return_failure_response_when_fail_to_create_{{cookiecutter.entity.lower()}}(){
            when(mock{{cookiecutter.entity}}Service.create(Create{{cookiecutter.entity}}Cmd.builder().name("name").build()))
            .thenThrow(new RuntimeException());

            Entity<Create{{cookiecutter.entity}}Cmd>entity=Entity.json(Create{{cookiecutter.entity}}Cmd.builder().name("name").build());
final Response response=resourceTestClient.target("/{{ cookiecutter.entity.lower() + 's' }}")
        .request()
        .post(entity);
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(response.readEntity(ErrorMessage.class).getMessage())
        .isEqualTo("Failed to create {{cookiecutter.entity.lower()}}");
        }
        }