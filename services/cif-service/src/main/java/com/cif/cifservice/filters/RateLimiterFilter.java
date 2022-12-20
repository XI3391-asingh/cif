package com.cif.cifservice.filters;

import com.cif.cifservice.core.party.domain.Config;
import com.cif.cifservice.core.party.domain.RateLimiterKey;
import com.cif.cifservice.db.elasticsearch.ElasticsearchAdminRepository;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConfigurationBuilder;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.hazelcast.HazelcastProxyManager;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.cif.cifservice.core.party.helper.ResponseHelper.buildErrorResponse;

public class RateLimiterFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    private final ElasticsearchAdminRepository adminRepository;

    private static final String TIMEUNIT = "TIMEUNIT";

    private static final String RESTRICTION = "RESTRICTION";

    @Context
    private HttpServletRequest request;

    private final ProxyManager<RateLimiterKey> proxyManager;


    public RateLimiterFilter(HazelcastInstance hazelcastInstance, ElasticsearchAdminRepository adminRepository) {
        this.adminRepository = adminRepository;
        IMap<RateLimiterKey, byte[]> bucketMap = hazelcastInstance.getMap("test");
        proxyManager = new HazelcastProxyManager<>(bucketMap);
    }


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String className = resourceInfo.getResourceClass().getName();
        if (StringUtils.isNotBlank(className) && className.equalsIgnoreCase("com.cif.cifservice.resources.PartyResource")) {
            if (adminRepository.fetchAdminConfigRecordBasedOnType("RATELIMITER").getConfigData().stream().filter(configData -> "RATELIMITERFLAG".equalsIgnoreCase(configData.getKey())).findFirst().get().getValues().get(0).equalsIgnoreCase("TRUE")) {
                Method method = resourceInfo.getResourceMethod();
                String id = "ip::" + request.getRemoteAddr();
                RateLimiterKey rateLimiterKey = new RateLimiterKey(id, method.getName());
                Bucket bucket = proxyManager.builder().build(rateLimiterKey, () -> bucketConfiguration());
                boolean consumeStatus = bucket.tryConsume(1);
                if (!consumeStatus) {
                    requestContext.abortWith(Response.status(HttpStatus.TOO_MANY_REQUESTS_429).entity(buildErrorResponse("Too Many Request", "Please retry after sometime !")).build());

                }
            }
        }
    }


    public BucketConfiguration bucketConfiguration() {
        ConfigurationBuilder configBuilder = new ConfigurationBuilder();
        configBuilder.addLimit(buildBandwidth());
        return configBuilder.build();

    }

    private Bandwidth buildBandwidth() {
        Config config = adminRepository.fetchAdminConfigRecordBasedOnType("RATELIMITER");
        TimeUnit timeUnit = TimeUnit.valueOf(config.getConfigData().stream().filter(configData -> TIMEUNIT.equalsIgnoreCase(configData.getKey())).findFirst().get().getValues().get(0));
        long restriction = Long.parseLong(config.getConfigData().stream().filter(configData -> RESTRICTION.equalsIgnoreCase(configData.getKey())).findFirst().get().getValues().get(0));

        if (TimeUnit.SECONDS.equals(timeUnit)) {
            return Bandwidth.simple(restriction, Duration.ofSeconds(1));
        } else if (TimeUnit.MINUTES.equals(timeUnit)) {
            return Bandwidth.simple(restriction, Duration.ofMinutes(1));
        } else if (TimeUnit.HOURS.equals(timeUnit)) {
            return Bandwidth.simple(restriction, Duration.ofHours(1));
        } else {
            return Bandwidth.simple(5000, Duration.ofSeconds(1));
        }
    }

}