package org.nortis.application.consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.nortis.ServiceTestBase;
import org.nortis.application.consumer.model.ConsumerDeleteCommand;
import org.nortis.application.consumer.model.ConsumerRegisterCommand;
import org.nortis.application.consumer.model.ConsumerSubscribeCommand;
import org.nortis.application.consumer.model.ConsumerUpdateCommand;
import org.nortis.domain.consumer.Consumer;
import org.nortis.domain.consumer.ConsumerRepository;
import org.nortis.domain.consumer.value.ConsumerId;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.EndpointIdentifier;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.service.AuthorityCheckDomainService;
import org.nortis.domain.service.ConsumerDomainService;
import org.nortis.domain.service.NumberingDomainService;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.test.user.TestUsers;

class ConsumerApplicationServiceTest extends ServiceTestBase {

    @Mock
    TenantRepository tenantRepository;

    @Mock
    ConsumerRepository consumerRepository;

    @Mock
    EndpointRepository endpointRepository;

    @Mock
    AuthorityCheckDomainService authorityCheckDomainService;

    @Mock
    ConsumerDomainService consumerDomainService;

    @Mock
    NumberingDomainService numberingDomainService;

    ConsumerApplicationService service;

    @BeforeEach
    void setup() {
        this.service = new ConsumerApplicationService(tenantRepository, consumerRepository, endpointRepository,
                authorityCheckDomainService, consumerDomainService, numberingDomainService);
    }

    @Test
    void testRegister() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");
        ConsumerId consumerId = ConsumerId.create("3000000001");

        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "テストテナント");
        when(tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));
        when(this.numberingDomainService.createNewConsumerId()).thenReturn(consumerId);

        Map<String, String> parameter = new LinkedHashMap<>();
        parameter.put("sendMailAddress", "hoge@example.com");
        parameter.put("return-path", "hoge@example.com");

        //@formatter:off
        ConsumerRegisterCommand command = ConsumerRegisterCommand.builder()
                .tenantId(tenantId.toString())
                .consumerName("テストコンシューマ")
                .consumerType("MAIL")
                .textType(TextType.TEXT.getTextType())
                .parameter(parameter)
                .build();
        //@formatter:on

        Consumer consumer = this.service.register(command, TestUsers.memberUser().getUserDetails(),
                ApplicationTranslator.noConvert());

        verify(this.consumerRepository).save(eq(consumer));
    }

    @Test
    void testUpdate() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");

        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "テストテナント");
        when(tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));

        Map<String, String> parameter = new LinkedHashMap<>();
        parameter.put("sendMailAddress", "hoge@example.com");
        parameter.put("return-path", "hoge@example.com");
        ConsumerId consumerId = ConsumerId.create("3000000001");
        Consumer consumer = Consumer.create(tenantId, consumerId, "テストコンシューマ", "MAIL", TextType.HTML, parameter);
        when(consumerRepository.get(eq(tenantId), eq(consumerId))).thenReturn(Optional.of(consumer));

        //@formatter:off
        ConsumerUpdateCommand command = ConsumerUpdateCommand.builder()
                .tenantId(tenantId.toString())
                .consumerId(consumerId.toString())
                .consumerName("テストコンシューマ")
                .consumerType("MAIL")
                .textType(TextType.TEXT.getTextType())
                .parameter(parameter)
                .build();
        //@formatter:on

        Consumer updateConsumer = this.service.update(command, TestUsers.memberUser().getUserDetails(),
                ApplicationTranslator.noConvert());

        verify(this.consumerRepository).update(eq(updateConsumer));
    }

    @Test
    void testSubscribe() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");

        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "テストテナント");
        when(tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));

        EndpointId endpointId = EndpointId.create("2000000001");
        Endpoint endpoint = Endpoint.create(tenantId, endpointId, EndpointIdentifier.create("TEST"), "テストエンドポイント");
        when(this.endpointRepository.get(eq(tenantId), eq(endpointId))).thenReturn(Optional.of(endpoint));

        Map<String, String> parameter = new LinkedHashMap<>();
        parameter.put("sendMailAddress", "hoge@example.com");
        parameter.put("return-path", "hoge@example.com");
        ConsumerId consumerId = ConsumerId.create("3000000001");
        Consumer consumer = Consumer.create(tenantId, consumerId, "テストコンシューマ", "MAIL", TextType.HTML, parameter);
        when(consumerRepository.get(eq(tenantId), eq(consumerId))).thenReturn(Optional.of(consumer));

        //@formatter:off
        ConsumerSubscribeCommand command = ConsumerSubscribeCommand.builder()
                .tenantId(tenantId.toString())
                .consumerId(consumerId.toString())
                .endpointId(endpointId.toString())
                .build();
        //@formatter:on

        this.service.subscribe(command, TestUsers.memberUser().getUserDetails());

        verify(this.consumerRepository).update(any());
    }

    @Test
    void testUnsubscribe() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");

        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "テストテナント");
        when(tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));

        EndpointId endpointId = EndpointId.create("2000000001");
        Endpoint endpoint = Endpoint.create(tenantId, endpointId, EndpointIdentifier.create("TEST"), "テストエンドポイント");
        when(this.endpointRepository.get(eq(tenantId), eq(endpointId))).thenReturn(Optional.of(endpoint));

        Map<String, String> parameter = new LinkedHashMap<>();
        parameter.put("sendMailAddress", "hoge@example.com");
        parameter.put("return-path", "hoge@example.com");
        ConsumerId consumerId = ConsumerId.create("3000000001");
        Consumer consumer = Consumer.create(tenantId, consumerId, "テストコンシューマ", "MAIL", TextType.HTML, parameter);
        when(consumerRepository.get(eq(tenantId), eq(consumerId))).thenReturn(Optional.of(consumer));

        //@formatter:off
        ConsumerSubscribeCommand command = ConsumerSubscribeCommand.builder()
                .tenantId(tenantId.toString())
                .consumerId(consumerId.toString())
                .endpointId(endpointId.toString())
                .build();
        //@formatter:on

        this.service.unsubscribe(command, TestUsers.memberUser().getUserDetails());

        verify(this.consumerRepository).update(any());
    }

    @Test
    void testDelete() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");

        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "テストテナント");
        when(tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));

        Map<String, String> parameter = new LinkedHashMap<>();
        parameter.put("sendMailAddress", "hoge@example.com");
        parameter.put("return-path", "hoge@example.com");
        ConsumerId consumerId = ConsumerId.create("3000000001");
        Consumer consumer = Consumer.create(tenantId, consumerId, "テストコンシューマ", "MAIL", TextType.HTML, parameter);
        when(consumerRepository.get(eq(tenantId), eq(consumerId))).thenReturn(Optional.of(consumer));

        //@formatter:off
        ConsumerDeleteCommand command = ConsumerDeleteCommand.builder()
                .tenantId(tenantId.toString())
                .consumerId(consumerId.toString())
                .build();
        //@formatter:on

        this.service.delete(command, TestUsers.memberUser().getUserDetails());

        verify(this.consumerRepository).remove(any());
    }

    @Test
    void testUnsubscribeEndpoint() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId = EndpointId.create("2000000001");

        Map<String, String> parameter = new LinkedHashMap<>();
        parameter.put("sendMailAddress", "hoge@example.com");
        parameter.put("return-path", "hoge@example.com");
        ConsumerId consumerId1 = ConsumerId.create("3000000001");
        Consumer consumer1 = Consumer.create(tenantId, consumerId1, "テストコンシューマ", "MAIL", TextType.HTML, parameter);
        ConsumerId consumerId2 = ConsumerId.create("3000000001");
        Consumer consumer2 = Consumer.create(tenantId, consumerId2, "テストコンシューマ", "MAIL", TextType.HTML, parameter);
        when(consumerRepository.getFromEndpoint(eq(endpointId))).thenReturn(Lists.list(consumer1, consumer2));

        this.service.unsubscribeEndpoint(endpointId.toString());

        verify(this.consumerRepository).updateAll(anyList());
    }

}
