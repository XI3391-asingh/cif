package com.cif.

{{cookiecutter.service_package}}.resources;

import com.codahale.metrics.annotation.ResponseMetered;
import com.cif.{{cookiecutter.service_package}}.api.Create{{cookiecutter.entity}}Cmd;
import com.cif.{{cookiecutter.service_package}}.api.{{cookiecutter.entity+'s'}}Api;
import com.cif.{{cookiecutter.service_package}}.core.{{cookiecutter.domain_package}}.services.{{cookiecutter.entity}}Service;
import com.cif.{{cookiecutter.service_package}}.resources.exceptions.FailedToCreate{{cookiecutter.entity}};
import com.cif.{{cookiecutter.service_package}}.api.Create{{cookiecutter.entity}}Response;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/{{ cookiecutter.entity.lower() + 's' }}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class {{cookiecutter.entity}}Resource implements{{cookiecutter.entity+'s'}}Api{

private {{cookiecutter.entity}}Service{{cookiecutter.entity.lower()}}Service;

public {{cookiecutter.entity}}Resource({{cookiecutter.entity}}Service{{cookiecutter.entity.lower()}}Service){
        this.{{cookiecutter.entity.lower()}}Service={{cookiecutter.entity.lower()}}Service;
        }

@POST
@ResponseMetered(name = "create{{ cookiecutter.entity }}")
public Response create(@NotNull @Valid Create{{cookiecutter.entity}}Cmd cmd){
        try{
        log.info("Command received to create a new {{ cookiecutter.entity }} [cmd={}]",cmd);
        var created={{cookiecutter.entity.lower()}}Service.create(cmd);
        log.info("{{ cookiecutter.entity }} created [cmd={},id={}]",cmd,created.getId());
        var createdEntity=Create{{cookiecutter.entity}}Response.builder()
        .id(created.getId().toString())
        .name(created.getName())
        .build();
        return Response.status(Response.Status.CREATED)
        .entity(createdEntity)
        .build();
        }catch(Exception e){
        log.error("Failed to create {{ cookiecutter.entity }} for [cmd={}]",cmd,e);
        throw new FailedToCreate{{cookiecutter.entity}}(
        "Failed to create {{ cookiecutter.entity.lower() }}",
        e);
        }
        }
        }
