package com.cif.

{{cookiecutter.service_package}};

import com.cif.{{cookiecutter.service_package}}.core.{{cookiecutter.domain_package}}.services.{{cookiecutter.entity}}Service;
import com.cif.{{cookiecutter.service_package}}.db.{{cookiecutter.entity}}Repository;
import com.cif.{{cookiecutter.service_package}}.resources.{{cookiecutter.entity}}Resource;
import com.cif.{{cookiecutter.service_package}}.resources.GitInfoResource;
import com.cif.{{cookiecutter.service_package}}.resources.exceptionmappers.FailedToCreate{{cookiecutter.entity}}ExceptionMapper;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;

public class {{cookiecutter.service_name_combined}}Application extends Application<{{cookiecutter.service_name_combined}}Configuration>{

public static void main(final String[]args)throws Exception{
        new{{cookiecutter.service_name_combined}}Application().run(args);
        }

@Override
public String getName(){
        return"{{ cookiecutter.service_slug }}";
        }

@Override
public void initialize(final Bootstrap<{{cookiecutter.service_name_combined}}Configuration>bootstrap){
        bootstrap.setConfigurationSourceProvider(
        new SubstitutingSourceProvider(
        bootstrap.getConfigurationSourceProvider(),
        new EnvironmentVariableSubstitutor(false)
        )
        );
        bootstrap.addBundle(new MigrationsBundle<>(){
@Override
public PooledDataSourceFactory getDataSourceFactory({{cookiecutter.service_name_combined}}Configuration configuration){
        return configuration.getDataSourceFactory();
        }
        });
        }

@Override
public void run(final {{cookiecutter.service_name_combined}}Configuration configuration,
final Environment environment){
final JdbiFactory factory=new JdbiFactory();
        Jdbi jdbi=factory.build(environment,configuration.getDataSourceFactory(),"postgresql");
        environment.jersey().register(new FailedToCreate{{cookiecutter.entity}}ExceptionMapper(environment.metrics()));
        {{cookiecutter.entity}}Service{{cookiecutter.entity.lower()}}Service=new{{cookiecutter.entity}}Service(jdbi.onDemand({{cookiecutter.entity}}Repository.class));
        {{cookiecutter.entity}}Resource{{cookiecutter.entity.lower()}}Resource=new{{cookiecutter.entity}}Resource({{cookiecutter.entity.lower()}}Service);
        environment.jersey().register({{cookiecutter.entity.lower()}}Resource);
        environment.jersey().register(new GitInfoResource());
        }

        }
