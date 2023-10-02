package org.nortis.infrastructure.doma.repository;

import org.nortis.domain.endpoint.EndpointEntityListener;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

@SuppressWarnings("javadoc")
@TestConfiguration
public class DomaEndpointRepositoryTestConfig {

    @Bean
    EndpointEntityListener endpointEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        var listner = new EndpointEntityListener();
        listner.setApplicationEventPublisher(applicationEventPublisher);
        return listner;
    }
}
