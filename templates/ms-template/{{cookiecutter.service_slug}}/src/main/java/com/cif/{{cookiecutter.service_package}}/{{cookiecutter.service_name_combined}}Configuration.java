package com.cif.

{{cookiecutter.service_package}};

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class {{cookiecutter.service_name_combined}}Configuration extends Configuration{

@Valid
@NotNull
private DataSourceFactory database=new DataSourceFactory();

@JsonProperty("database")
public DataSourceFactory getDataSourceFactory(){
        return database;
        }

@JsonProperty("database")
public void setDatasourceFactory(DataSourceFactory database){
        this.database=database;
        }
        }
