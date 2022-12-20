package com.finx.stories.commonservice;

import java.util.List;

public class SendOtpStoryTest extends AbstractStoryTest {
    @Override
    protected List<?> getStepClasses() {
        return List.of(new SendOtpSteps(getClient(), getLocalPort()));
    }

    @Override
    public List<String> storyPaths() {
        return List.of("stories/send-otp.story");
    }
}
