package com.finx.stories.onboardingservice;

import java.util.List;
import org.jbehave.core.junit.JUnit4StoryRunner;
import org.junit.runner.RunWith;

@RunWith(JUnit4StoryRunner.class)
public class VerifyMobileNumberStoryTest extends AbstractStoryTest {

    @Override
    protected List<?> getStepClasses() {
        return List.of(new VerifyMobileNumberSteps(getClient(), getLocalPort()));
    }

    @Override
    public List<String> storyPaths() {
        return List.of("stories/onboarding_workflow_verify_mobile_no.story");
    }

}