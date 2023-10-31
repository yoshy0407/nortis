package org.nortis.consumer.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class MessageTest {

    Message message = new Message(MessageTextType.HTML, "subject", "body");

    @Test
    void testGetTextType() {
        assertThat(message.getTextType()).isEqualTo(MessageTextType.HTML);
    }

    @Test
    void testGetSubject() {
        assertThat(message.getSubject()).isEqualTo("subject");
    }

    @Test
    void testGetMessageBody() {
        assertThat(message.getMessageBody()).isEqualTo("body");
    }

}
