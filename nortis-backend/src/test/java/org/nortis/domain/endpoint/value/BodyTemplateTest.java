package org.nortis.domain.endpoint.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.nortis.TestBase;
import org.nortis.infrastructure.exception.DomainException;

class BodyTemplateTest extends TestBase {

    @Test
    void testCreate_Null() {
        assertThrows(DomainException.class, () -> {
            BodyTemplate.create(null);
        });
    }

    @Test
    void testCreate_Empty() {
        assertThrows(DomainException.class, () -> {
            BodyTemplate.create("");
        });
    }

    @Test
    void testHashCode_True() throws DomainException {
        var bodyTemplate1 = BodyTemplate.create("message");
        var bodyTemplate2 = BodyTemplate.create("message");
        assertThat(bodyTemplate1.hashCode()).isEqualTo(bodyTemplate2.hashCode());
    }

    @Test
    void testHashCode_False() throws DomainException {
        var bodyTemplate1 = BodyTemplate.create("message");
        var bodyTemplate2 = BodyTemplate.create("message1");
        assertThat(bodyTemplate1.hashCode()).isNotEqualTo(bodyTemplate2.hashCode());
    }

    @Test
    void testToString() throws DomainException {
        var bodyTemplate = BodyTemplate.create("message");
        assertThat(bodyTemplate.toString()).isEqualTo("message");
    }

    @Test
    void testEquals_True() throws DomainException {
        var bodyTemplate1 = BodyTemplate.create("message");
        var bodyTemplate2 = BodyTemplate.create("message");
        assertThat(bodyTemplate1.equals(bodyTemplate2)).isTrue();
    }

    @Test
    void testEquals_False() throws DomainException {
        var bodyTemplate1 = BodyTemplate.create("message");
        var bodyTemplate2 = BodyTemplate.create("message");
        assertThat(bodyTemplate1.equals(bodyTemplate2)).isTrue();
    }

}
