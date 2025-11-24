package com.me.heuron.domains.test.setup;

import com.me.heuron.domains.test.config.TestProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 통합 테스트 Setup
 */
@Profile(TestProfile.TEST)
@RequiredArgsConstructor
@Component
public class IntegrationTestSetup {
	public void setup() {
	}
}
