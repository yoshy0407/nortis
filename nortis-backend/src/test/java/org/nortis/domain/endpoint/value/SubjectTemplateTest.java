package org.nortis.domain.endpoint.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.nortis.TestBase;
import org.nortis.infrastructure.exception.DomainException;

class SubjectTemplateTest extends TestBase {

    @Test
    void testCreate_Null() {
        assertThrows(DomainException.class, () -> {
            SubjectTemplate.create(null);
        });
    }

    @Test
    void testCreate_Empty() {
        assertThrows(DomainException.class, () -> {
            SubjectTemplate.create("");
        });
    }

    @Test
    void testHashCode_True() throws DomainException {
        var subjectTemplate1 = SubjectTemplate.create("message");
        var subjectTemplate2 = SubjectTemplate.create("message");
        assertThat(subjectTemplate1.hashCode()).isEqualTo(subjectTemplate2.hashCode());
    }

    @Test
    void testHashCode_False() throws DomainException {
        var subjectTemplate1 = SubjectTemplate.create("message");
        var subjectTemplate2 = SubjectTemplate.create("message1");
        assertThat(subjectTemplate1.hashCode()).isNotEqualTo(subjectTemplate2.hashCode());
    }

    @Test
    void testToString() throws DomainException {
        var subjectTemplate = SubjectTemplate.create("message");
        assertThat(subjectTemplate.toString()).isEqualTo("message");
    }

    @Test
    void testEquals_True() throws DomainException {
        var subjectTemplate1 = SubjectTemplate.create("message");
        var subjectTemplate2 = SubjectTemplate.create("message");
        assertThat(subjectTemplate1.equals(subjectTemplate2)).isTrue();
    }

    @Test
    void testEquals_False() throws DomainException {
        var subjectTemplate1 = SubjectTemplate.create("message");
        var subjectTemplate2 = SubjectTemplate.create("message");
        assertThat(subjectTemplate1.equals(subjectTemplate2)).isTrue();
    }

}
