package com.finx.onboardingservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class OnboardingServiceConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @NotNull
    private String urlSearchMobile;

    @NotNull
    private String urlCheckBlockingPeriod;

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDatasourceFactory(DataSourceFactory database) {
        this.database = database;
    }

    @JsonProperty("urlSearchMobile")
    public String getUrlSearchMobile() {
        return urlSearchMobile;
    }

    @JsonProperty("urlSearchMobile")
    public void setUrlSearchMobile(String urlSearchMobile) {
        this.urlSearchMobile = urlSearchMobile;
    }

    @JsonProperty("urlCheckBlockingPeriod")
    public String getUrlCheckBlockingPeriod() {
        return urlCheckBlockingPeriod;
    }

    @JsonProperty("urlCheckBlockingPeriod")
    public void setUrlCheckBlockingPeriod(String urlCheckBlockingPeriod) {
        this.urlCheckBlockingPeriod = urlCheckBlockingPeriod;
    }
}
