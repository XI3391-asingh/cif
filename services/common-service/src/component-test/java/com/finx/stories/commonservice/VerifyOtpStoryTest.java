package com.finx.stories.commonservice;

import java.util.List;

public class VerifyOtpStoryTest extends AbstractStoryTest {
    @Override
    protected List<?> getStepClasses() {
        return List.of(new VerifyOtpSteps(getClient(), getLocalPort()));
    }

    @Override
    public List<String> storyPaths() {
        return List.of("stories/verify-otp.story");
    }
}
