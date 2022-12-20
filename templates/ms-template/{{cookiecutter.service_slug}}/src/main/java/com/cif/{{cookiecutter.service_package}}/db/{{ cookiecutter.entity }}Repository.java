package com.cif.

{{cookiecutter.service_package}}.db;

import com.cif.{{cookiecutter.service_package}}.core.{{cookiecutter.domain_package}}.domain.{{cookiecutter.entity}};
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface {{cookiecutter.entity}}Repository{

@SqlUpdate("INSERT INTO {{ cookiecutter.entity.lower() + 's' }} (id, name) VALUES (:id, :name)")
@GetGeneratedKeys
@RegisterBeanMapper({{cookiecutter.entity}}.class)
    {{cookiecutter.entity}}save(@BindBean {{cookiecutter.entity}}workflow);
        }
