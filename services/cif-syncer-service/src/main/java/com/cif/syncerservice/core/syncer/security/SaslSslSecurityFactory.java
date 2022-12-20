package com.cif.syncerservice.core.syncer.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableMap;
import org.apache.kafka.common.config.SslConfigs;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@JsonTypeName("sasl_ssl")
public class SaslSslSecurityFactory extends SaslPlaintextSecurityFactory {

    @NotEmpty
    @JsonProperty
    private String sslProtocol = "TLSv1.2";

    public String getSslProtocol() {
        return sslProtocol;
    }

    public void setSslProtocol(final String sslProtocol) {
        this.sslProtocol = sslProtocol;
    }

    @Override
    public Map<String, Object> build() {
        final ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .putAll(super.build())
                .put(SslConfigs.SSL_PROTOCOL_CONFIG, sslProtocol);

        return builder.build();
    }
}
