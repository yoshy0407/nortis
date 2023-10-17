package org.nortis.domain.endpoint.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.nortis.TestBase;
import org.nortis.infrastructure.exception.DomainException;

class EndpointIdentifierTest extends TestBase {

    @Test
    void testHashCode_True() throws DomainException {
        var endpointIdentifier1 = EndpointIdentifier.create("TEST");
        var endpointIdentifier2 = EndpointIdentifier.create("TEST");
        assertThat(endpointIdentifier1.hashCode()).isEqualTo(endpointIdentifier2.hashCode());
    }

    @Test
    void testHashCode_False() throws DomainException {
        var endpointIdentifier1 = EndpointIdentifier.create("TEST");
        var endpointIdentifier2 = EndpointIdentifier.create("TEST1");
        assertThat(endpointIdentifier1.hashCode()).isNotEqualTo(endpointIdentifier2.hashCode());
    }

    @Test
    void testToString() throws DomainException {
        var endpointIdentifier = EndpointIdentifier.create("TEST");
        assertThat(endpointIdentifier.toString()).isEqualTo("TEST");
    }

    @Test
    void testCreate_Null() {
        assertThrows(DomainException.class, () -> {
            EndpointIdentifier.create(null);
        });
    }

    @Test
    void testCreate_Empty() {
        assertThrows(DomainException.class, () -> {
            EndpointIdentifier.create("");
        });
    }

    @Test
    void testCreate_Length() {
        assertThrows(DomainException.class, () -> {
            EndpointIdentifier.create("123456789012345678901");
        });
    }

    @Test
    void testEqualsObject_False() throws DomainException {
        var endpointIdentifier1 = EndpointIdentifier.create("TEST");
        var endpointIdentifier2 = EndpointIdentifier.create("TEST1");
        assertThat(endpointIdentifier1.equals(endpointIdentifier2)).isFalse();
    }

    @Test
    void testEqualsObject_True() throws DomainException {
        var endpointIdentifier1 = EndpointIdentifier.create("TEST");
        var endpointIdentifier2 = EndpointIdentifier.create("TEST");
        assertThat(endpointIdentifier1.equals(endpointIdentifier2)).isTrue();
    }

}
