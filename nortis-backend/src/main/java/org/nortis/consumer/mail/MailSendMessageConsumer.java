package org.nortis.consumer.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import org.nortis.consumer.ConsumerFailureException;
import org.nortis.consumer.ConsumerParameters;
import org.nortis.consumer.MessageConsumer;
import org.nortis.consumer.model.ConsumerType;
import org.nortis.consumer.model.Message;
import org.nortis.consumer.model.MessageTextType;
import org.nortis.consumer.parameter.ParameterDefinition;
import org.nortis.consumer.parameter.StandardParameterDefinition;
import org.nortis.consumer.parameter.converter.StringConverter;
import org.nortis.consumer.parameter.converter.StringListConverter;
import org.nortis.consumer.parameter.validator.StringValidatiorBuilder;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * メールを送信するドメインサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Component
public class MailSendMessageConsumer implements MessageConsumer {

    /**
     * 表示名
     */
    public static final String DISPLAY_NAME = "メール";

    /**
     * コード値
     */
    public static final String TYPE_CODE = "MAIL";

    private final StandardParameterDefinition<String> fromMailAddress;

    private final StandardParameterDefinition<List<String>> toMailAddresses;

    private final List<ParameterDefinition<?>> parameterList = new ArrayList<>();

    /** メール送信コンポーネント */
    private final JavaMailSender javaMailSender;

    /**
     * インスタンスを生成します
     * 
     * @param javaMailSender {@link JavaMailSender}
     */
    public MailSendMessageConsumer(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        this.fromMailAddress = new StandardParameterDefinition<String>("fromMailAddress", "送信メールアドレス", true,
                new StringConverter(), new StringValidatiorBuilder().notBlank("送信メールアドレスが未設定です").build());
        this.toMailAddresses = new StandardParameterDefinition<>("toMailAddresses", "送信先メールアドレス", true,
                new StringListConverter(), new StringValidatiorBuilder().notBlank("送信先メールアドレスが未設定です").build());
        this.parameterList.add(fromMailAddress);
        this.parameterList.add(toMailAddresses);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConsumerType consumerType() {
        return new ConsumerType(TYPE_CODE, DISPLAY_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParameterDefinition<?>> consumerParameters() {
        return this.parameterList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void consume(Message message, ConsumerParameters parameter) throws ConsumerFailureException {

        List<String> sendMailAddressList = parameter.getParameter(this.toMailAddresses);

        List<MimeMessage> messages = new ArrayList<>();
        for (String sendMailAddress : sendMailAddressList) {
            messages.add(createMessage(message, parameter, sendMailAddress));
        }

        try {
            this.javaMailSender.send(messages.toArray(new MimeMessage[messages.size()]));
        } catch (MailException ex) {
            throw new ConsumerFailureException("メールの送信に失敗しました", ex);
        }
    }

    private MimeMessage createMessage(Message message, ConsumerParameters parameter, String sendMailAddress)
            throws ConsumerFailureException {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(parameter.getParameter(this.fromMailAddress));
            helper.setTo(sendMailAddress);
            helper.setSubject(message.getSubject());
            helper.setText(message.getMessageBody(), message.getTextType().equals(MessageTextType.HTML));
        } catch (MessagingException ex) {
            throw new ConsumerFailureException("メールのメッセージ構築に失敗しました", ex);
        }
        return mimeMessage;
    }

}
