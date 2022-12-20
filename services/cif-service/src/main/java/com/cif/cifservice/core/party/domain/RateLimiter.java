package com.cif.cifservice.core.party.domain;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface RateLimiter {

}
