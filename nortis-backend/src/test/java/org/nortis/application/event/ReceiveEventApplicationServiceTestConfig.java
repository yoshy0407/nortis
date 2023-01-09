package org.nortis.application.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.apache.velocity.app.VelocityEngine;
import org.nortis.domain.consumer.MessageConsumer;
import org.nortis.domain.consumer.MessageFactory;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.EndpointRepositoryImpl;
import org.nortis.domain.event.ReceiveEventCheckDomainService;
import org.nortis.domain.event.ReceiveEventConsumeDomainService;
import org.nortis.domain.event.ReceiveEventRepository;
import org.nortis.domain.event.ReceiveEventRepositoryImpl;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.TenantRepositoryImpl;
import org.nortis.infrastructure.ApplicationContextAccessor;
import org.nortis.infrastructure.template.VelocityTemplateRender;
import org.seasar.doma.jdbc.Config;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;


@TestConfiguration
class ReceiveEventApplicationServiceTestConfig implements ApplicationContextAware {

	@Bean
	ReceiveEventRepository receiveEventRepository(Config condig) {
		return new ReceiveEventRepositoryImpl(condig);
	}
	
	@Bean
	TenantRepository tenantRepository(Config config) {
		return new TenantRepositoryImpl(config);
	}
	
	@Bean
	EndpointRepository endpointRepository(Config config) {
		return new EndpointRepositoryImpl(config);
	}
	
	@Bean
	ReceiveEventCheckDomainService receiveEventCheckDomainService(
			TenantRepository tenantRepository,
			EndpointRepository endpointRepository) {
		return new ReceiveEventCheckDomainService(
				tenantRepository, 
				endpointRepository);
	}
	
	@Bean
	MessageFactory messageFactory(ObjectMapper objectMapper) {
		VelocityEngine vm = new VelocityEngine();
		vm.init();
		VelocityTemplateRender render = new VelocityTemplateRender(vm);
		return new MessageFactory(render, objectMapper);
	}
	
	@Bean
	ReceiveEventConsumeDomainService receiveEventConsumeDomainService(
			EndpointRepository endpointRepository,
			List<MessageConsumer> messageConsumerList,
			MessageFactory messageFactory) {
		return new ReceiveEventConsumeDomainService(
				endpointRepository, 
				messageConsumerList, 
				messageFactory);
	}
	
	@Bean
	ReceiveEventApplicationService receiveEventApplicationService(
			ReceiveEventRepository receiveEventRepository,
			ReceiveEventCheckDomainService receiveEventCheckDomainService,
			ReceiveEventConsumeDomainService receiveEventConsumeDomainService) {
		return new ReceiveEventApplicationService(
				receiveEventRepository, 
				receiveEventCheckDomainService, 
				receiveEventConsumeDomainService);
	}

	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextAccessor.set(applicationContext);
	}
}
