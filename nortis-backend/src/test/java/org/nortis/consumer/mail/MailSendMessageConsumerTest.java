package org.nortis.consumer.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nortis.consumer.ConsumerFailureException;
import org.nortis.consumer.ConsumerParameters;
import org.nortis.consumer.model.ConsumerType;
import org.nortis.consumer.model.Message;
import org.nortis.consumer.model.MessageTextType;
import org.nortis.consumer.parameter.ParameterDefinition;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@ExtendWith(MockitoExtension.class)
class MailSendMessageConsumerTest {

    @Mock
    JavaMailSender javaMailSender;

    JavaMailSenderImpl delegate;

    MailSendMessageConsumer mailSendMessageConsumer;

    @BeforeEach
    void setup() {
        this.delegate = new JavaMailSenderImpl();
        this.mailSendMessageConsumer = new MailSendMessageConsumer(this.javaMailSender);
    }

    @Test
    void testConsumerName() {
        assertThat(this.mailSendMessageConsumer.consumerName()).isEqualTo("MailConsumer");
    }

    @Test
    void testConsumerType() {
        assertThat(this.mailSendMessageConsumer.consumerType())
                .isEqualTo(new ConsumerType(MailSendMessageConsumer.TYPE_CODE, MailSendMessageConsumer.DISPLAY_NAME));
    }

    @Test
    void testConsumerParameters() {
        List<ParameterDefinition<?>> parameterList = this.mailSendMessageConsumer.consumerParameters();

        assertThat(parameterList).hasSize(2);
        assertThat(parameterList.get(0).getParameterName()).isEqualTo("fromMailAddress");
        assertThat(parameterList.get(0).getDisplayName()).isEqualTo("送信メールアドレス");
        assertThat(parameterList.get(1).getParameterName()).isEqualTo("toMailAddresses");
        assertThat(parameterList.get(1).getDisplayName()).isEqualTo("送信先メールアドレス");
    }

    @Test
    void testConsume() throws ConsumerFailureException, MessagingException, IOException {

        MimeMessage message1 = this.delegate.createMimeMessage();
        MimeMessage message2 = this.delegate.createMimeMessage();
        when(this.javaMailSender.createMimeMessage()).thenReturn(message1, message2);

        Message message = new Message(MessageTextType.HTML, "subject", "body");

        Map<String, String> map = new HashMap<>();
        map.put("fromMailAddress", "noreply@nortis.com");
        map.put("toMailAddresses", "sample1@nortis.com,sample2@nortis.com");
        ConsumerParameters parameters = new ConsumerParameters(map);

        this.mailSendMessageConsumer.consume(message, parameters);

        ArgumentCaptor<MimeMessage[]> captor = ArgumentCaptor.forClass(MimeMessage[].class);
        verify(this.javaMailSender).send(captor.capture());

        MimeMessage[] mails = captor.getValue();

        assertThat(mails).hasSize(2);
        MimeMessage mail1 = mails[0];
        assertThat(mail1.getFrom()[0].toString()).isEqualTo("noreply@nortis.com");
        assertThat(mail1.getRecipients(RecipientType.TO)[0].toString()).isEqualTo("sample1@nortis.com");
        assertThat(mail1.getSubject()).isEqualTo("subject");
        assertThat(mail1.getContent()).isNotNull();

        MimeMessage mail2 = mails[1];
        assertThat(mail2.getFrom()[0].toString()).isEqualTo("noreply@nortis.com");
        assertThat(mail2.getRecipients(RecipientType.TO)[0].toString()).isEqualTo("sample2@nortis.com");
        assertThat(mail2.getSubject()).isEqualTo("subject");
        assertThat(mail2.getContent()).isNotNull();
    }

    @Test
    void testConsume_FromMailNotFound() throws ConsumerFailureException, MessagingException, IOException {

        Message message = new Message(MessageTextType.HTML, "subject", "body");

        Map<String, String> map = new HashMap<>();
        map.put("toMailAddresses", "sample1@nortis.com,sample2@nortis.com");
        ConsumerParameters parameters = new ConsumerParameters(map);

        assertThatThrownBy(() -> {
            this.mailSendMessageConsumer.consume(message, parameters);
        }).isInstanceOf(ConsumerFailureException.class).hasMessage("送信元メールアドレスが未設定です");

    }

    @Test
    void testConsume_toMailNotFound() throws ConsumerFailureException, MessagingException, IOException {

        Message message = new Message(MessageTextType.HTML, "subject", "body");

        Map<String, String> map = new HashMap<>();
        map.put("fromMailAddress", "noreply@nortis.com");
        ConsumerParameters parameters = new ConsumerParameters(map);

        assertThatThrownBy(() -> {
            this.mailSendMessageConsumer.consume(message, parameters);
        }).isInstanceOf(ConsumerFailureException.class).hasMessage("送信先メールアドレスが未設定です");

    }

}
