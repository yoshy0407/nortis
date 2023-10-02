package org.nortis.application.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.nortis.ServiceTestBase;
import org.nortis.application.endpoint.model.EndpointDeleteCommand;
import org.nortis.application.endpoint.model.EndpointDeleteMessageTemplateCommand;
import org.nortis.application.endpoint.model.EndpointMessageTemplateCommand;
import org.nortis.application.endpoint.model.EndpointNameUpdateCommand;
import org.nortis.application.endpoint.model.EndpointRegisterCommand;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.value.BodyTemplate;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.EndpointIdentifier;
import org.nortis.domain.endpoint.value.SubjectTemplate;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.service.AuthorityCheckDomainService;
import org.nortis.domain.service.NumberingDomainService;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.test.user.TestUsers;

class EndpointApplicationServiceTest extends ServiceTestBase {

    @Mock
    TenantRepository tenantRepository;

    @Mock
    EndpointRepository endpointRepository;

    @Mock
    NumberingDomainService numberingDomainService;

    @Mock
    AuthorityCheckDomainService authorityCheckDomainService;

    EndpointApplicationService endpointApplicationService;

    @BeforeEach
    void setup() {
        this.endpointApplicationService = new EndpointApplicationService(tenantRepository, endpointRepository,
                numberingDomainService, authorityCheckDomainService);
    }

    @Test
    void testGetTextTypes() {
        List<TextType> textTypes = this.endpointApplicationService.getTextTypes(ApplicationTranslator.noConvert());
        assertThat(textTypes).hasSize(TextType.values().length);
    }

    @Test
    void testRegisterEndpoint() throws DomainException {

        var testUser = TestUsers.memberUser();
        TenantId tenantId = TenantId.create("1000000001");

        when(this.numberingDomainService.createNewEndpointId()).thenReturn(EndpointId.create("2000000001"));

        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "テスト");
        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));

        //@formatter:off
        EndpointRegisterCommand command = EndpointRegisterCommand.builder()
                .tenantId(tenantId.toString())
                .endpointIdentifier("TEST")
                .endpointName("エンドポイント")
                .textType("00001")
                .subjectTemplate("${name}!!")
                .bodyTemplate("Hello! ${name}")
                .build();
        //@formatter:on

        Endpoint endpoint = endpointApplicationService.registerEndpoint(command, testUser.getUserDetails(),
                ApplicationTranslator.noConvert());

        verify(this.endpointRepository).save(eq(endpoint));
    }

    @Test
    void testUpdateEndpointName() throws DomainException {

        var testUser = TestUsers.memberUser();
        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId = EndpointId.create("2000000001");

        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "テスト");
        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));

        Endpoint testDataEndpoint = Endpoint.create(tenantId, endpointId, EndpointIdentifier.create("TEST"), "エンドポイント");
        when(this.endpointRepository.get(eq(tenantId), eq(endpointId))).thenReturn(Optional.of(testDataEndpoint));

        //@formatter:off
        EndpointNameUpdateCommand command = EndpointNameUpdateCommand.builder()
                .tenantId(tenantId.toString())
                .endpointId(endpointId.toString())
                .endpointName("テスト")
                .build();
        //@formatter:on

        Endpoint endpoint = endpointApplicationService.updateEndpointName(command, testUser.getUserDetails(),
                ApplicationTranslator.noConvert());

        verify(this.endpointRepository).update(eq(endpoint));
        assertThat(endpoint.getEndpointName()).isEqualTo("テスト");
    }

    @Test
    void testUpdateEndpointName_NotFoundEndpoint() throws DomainException {

        var testUser = TestUsers.memberUser();
        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId = EndpointId.create("2000000001");

        when(this.endpointRepository.get(eq(tenantId), eq(endpointId))).thenReturn(Optional.empty());

        //@formatter:off
        EndpointNameUpdateCommand command = EndpointNameUpdateCommand.builder()
                .tenantId(tenantId.toString())
                .endpointId(endpointId.toString())
                .endpointName("テスト")
                .build();
        //@formatter:on

        assertThrows(DomainException.class, () -> {
            endpointApplicationService.updateEndpointName(command, testUser.getUserDetails(),
                    ApplicationTranslator.noConvert());
        });
    }

    @Test
    void testAddMessageTemplate() throws DomainException {

        var testUser = TestUsers.memberUser();
        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId = EndpointId.create("2000000001");

        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "テスト");
        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));

        Endpoint testDataEndpoint = Endpoint.create(tenantId, endpointId, EndpointIdentifier.create("TEST"), "エンドポイント");
        when(this.endpointRepository.get(eq(tenantId), eq(endpointId))).thenReturn(Optional.of(testDataEndpoint));

        //@formatter:off
        EndpointMessageTemplateCommand command = EndpointMessageTemplateCommand.builder()
                .tenantId(tenantId.toString())
                .endpointId(endpointId.toString())
                .textType("00001")
                .subjectTemplate("subject")
                .bodyTemplate("body")
                .build();
        //@formatter:on

        Endpoint endpoint = endpointApplicationService.addMessageTemplate(command, testUser.getUserDetails(),
                ApplicationTranslator.noConvert());

        verify(this.endpointRepository).update(eq(endpoint));
        assertThat(endpoint.getMessageTemplateList()).hasSize(1);
    }

    @Test
    void testUpdateMessageTemplate() throws DomainException {

        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId = EndpointId.create("2000000001");

        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "テスト");
        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));

        Endpoint testDataEndpoint = Endpoint.create(tenantId, endpointId, EndpointIdentifier.create("TEST"), "エンドポイント");
        testDataEndpoint.createTemplate(TextType.TEXT, SubjectTemplate.create("test"), BodyTemplate.create("test"));
        when(this.endpointRepository.get(eq(tenantId), eq(endpointId))).thenReturn(Optional.of(testDataEndpoint));

        //@formatter:off
        EndpointMessageTemplateCommand command = EndpointMessageTemplateCommand.builder()
                .tenantId(tenantId.toString())
                .endpointId(endpointId.toString())
                .textType("00001")
                .subjectTemplate("subject")
                .bodyTemplate("body")
                .build();
        //@formatter:on
        var testUser = TestUsers.memberUser();

        Endpoint endpoint = this.endpointApplicationService.updateMessageTemplate(command, testUser.getUserDetails(),
                ApplicationTranslator.noConvert());

        verify(this.endpointRepository).update(eq(endpoint));
        assertThat(endpoint.getMessageTemplateList()).hasSize(1);
    }

    @Test
    void testDeleteMessageTemplate() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId = EndpointId.create("2000000001");

        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "テスト");
        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));

        Endpoint testDataEndpoint = Endpoint.create(tenantId, endpointId, EndpointIdentifier.create("TEST"), "エンドポイント");
        testDataEndpoint.createTemplate(TextType.TEXT, SubjectTemplate.create("test"), BodyTemplate.create("test"));
        when(this.endpointRepository.get(eq(tenantId), eq(endpointId))).thenReturn(Optional.of(testDataEndpoint));

        //@formatter:off
        EndpointDeleteMessageTemplateCommand command = EndpointDeleteMessageTemplateCommand.builder()
                .tenantId(tenantId.toString())
                .endpointId(endpointId.toString())
                .textType("00001")
                .build();
        //@formatter:on
        var testUser = TestUsers.memberUser();

        this.endpointApplicationService.deleteMessageTemplate(command, testUser.getUserDetails());

        verify(this.endpointRepository).update(eq(testDataEndpoint));
        assertThat(testDataEndpoint.getMessageTemplateList()).hasSize(1);
    }

    @Test
    void testDelete() throws DomainException {
        var testUser = TestUsers.memberUser();
        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId = EndpointId.create("2000000001");

        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "テスト");
        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));

        Endpoint testDataEndpoint = Endpoint.create(tenantId, endpointId, EndpointIdentifier.create("TEST"), "エンドポイント");
        when(this.endpointRepository.get(eq(tenantId), eq(endpointId))).thenReturn(Optional.of(testDataEndpoint));

        //@formatter:off
        EndpointDeleteCommand command = EndpointDeleteCommand.builder()
                .tenantId(tenantId.toString())
                .endpointId(endpointId.toString())
                .build();
        //@formatter:on

        endpointApplicationService.delete(command, testUser.getUserDetails());

        verify(this.endpointRepository).remove(eq(testDataEndpoint));
    }

    @Test
    void testDelete_NotFoundEndpoint() throws DomainException {
        var testUser = TestUsers.memberUser();
        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId = EndpointId.create("2000000001");

        when(this.endpointRepository.get(eq(tenantId), eq(endpointId))).thenReturn(Optional.empty());

        //@formatter:off
        EndpointDeleteCommand command = EndpointDeleteCommand.builder()
                .tenantId(tenantId.toString())
                .endpointId(endpointId.toString())
                .build();
        //@formatter:on

        assertThrows(DomainException.class, () -> {
            endpointApplicationService.delete(command, testUser.getUserDetails());
        });
    }

    @Test
    void testDeleteFromTenantId() throws DomainException {
        var testUser = TestUsers.memberUser();
        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId1 = EndpointId.create("2000000001");
        EndpointId endpointId2 = EndpointId.create("2000000001");

        Endpoint testData1 = Endpoint.create(tenantId, endpointId1, EndpointIdentifier.create("TEST1"), "エンドポイント1");
        Endpoint testData2 = Endpoint.create(tenantId, endpointId2, EndpointIdentifier.create("TEST2"), "エンドポイント2");
        List<Endpoint> endpointList = Lists.list(testData1, testData2);
        when(this.endpointRepository.getFromTenantId(eq(tenantId))).thenReturn(endpointList);

        endpointApplicationService.deleteFromTenantId(tenantId.toString());

        verify(this.endpointRepository).removeAll(eq(endpointList));
    }

}
