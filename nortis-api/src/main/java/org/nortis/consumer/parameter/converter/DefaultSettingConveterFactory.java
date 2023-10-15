package org.nortis.consumer.parameter.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.nortis.consumer.parameter.Converter;

/**
 * デフォルトの設定がされた{@link Converter}を返すファクトリークラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class DefaultSettingConveterFactory {

    private static boolean INITIALIZED = false;

    private static DefaultSettingConveterFactory INSTANCE = null;

    private DateTimeFormatter localDateFormatter;

    private DateTimeFormatter localDateTimeFormatter;

    private DateTimeFormatter localTimeFormatter;

    private DateTimeFormatter zonedDateTimeFormatter;

    /**
     * インスタンスを生成します
     * 
     * @param localDateFormatter     {@link LocalDate}用のフォーマット
     * @param localDateTimeFormatter {@link LocalDateTime}用のフォーマット
     * @param localTimeFormatter     {@link LocalTime}用のフォーマット
     * @param zonedDateTimeFormatter {@link ZonedDateTime}用のフォーマット
     */
    public DefaultSettingConveterFactory(DateTimeFormatter localDateFormatter, DateTimeFormatter localTimeFormatter,
            DateTimeFormatter localDateTimeFormatter, DateTimeFormatter zonedDateTimeFormatter) {
        this.localDateFormatter = localDateFormatter;
        this.localDateTimeFormatter = localDateTimeFormatter;
        this.localTimeFormatter = localTimeFormatter;
        this.zonedDateTimeFormatter = zonedDateTimeFormatter;
        setInstance(this);
    }

    /**
     * デフォルトの設定がされた{@link LocalDateConverter}を返します
     * 
     * @return {@link LocalDateConverter}
     */
    public LocalDateConverter defaultLocalDateConverter() {
        return new LocalDateConverter(localDateFormatter);
    }

    /**
     * デフォルトの設定がされた{@link LocalDateTimeConverter}を返します
     * 
     * @return {@link LocalDateTimeConverter}
     */
    public LocalDateTimeConverter defaultLocalDateTimeConverter() {
        return new LocalDateTimeConverter(localDateTimeFormatter);
    }

    /**
     * デフォルトの設定がされた{@link LocalTimeConverter}を返します
     * 
     * @return {@link LocalTimeConverter}
     */
    public LocalTimeConverter defaultLocalTimeConverter() {
        return new LocalTimeConverter(localTimeFormatter);
    }

    /**
     * デフォルトの設定がされた{@link ZonedDateTimeConverter}を返します
     * 
     * @return {@link ZonedDateTimeConverter}
     */
    public ZonedDateTimeConverter defaultZonedDateTimeConverter() {
        return new ZonedDateTimeConverter(zonedDateTimeFormatter);
    }

    private static void setInstance(DefaultSettingConveterFactory instance) {
        if (!DefaultSettingConveterFactory.INITIALIZED) {
            INSTANCE = instance;
        }
    }

    /**
     * インスタンスを返します
     * 
     * @return このクラスのインスタンス
     */
    public static DefaultSettingConveterFactory getInstance() {
        if (!INITIALIZED) {
            throw new IllegalStateException("This instance not initialized. check application lifecycle");
        }
        return INSTANCE;
    }
}
