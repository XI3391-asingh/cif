package com.finx.stories.commonservice;

import java.util.List;

public class BlockingPeriodExpiredStepsTest extends AbstractStoryTest {
    @Override
    protected List<?> getStepClasses() {
        return List.of(new BlockingPeriodExpiredSteps(getClient(), getLocalPort()));
    }

    @Override
    public List<String> storyPaths() {
        return List.of("stories/blocking-period-expired.story");
    }
}
