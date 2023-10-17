package org.nortis.consumer.parameter;

/**
 * 抽象的な{@link ParameterDefinition}です
 * 
 * @author yoshiokahiroshi
 * @param <T> 対象とする型
 * @version 1.0.0
 */
public abstract class AbstractParameterDefinition<T> implements ParameterDefinition<T> {

    private final String parameterName;
    private final String displayName;
    private final boolean require;

    /**
     * コンシューマー
     * 
     * @param parameterName パラメータ名
     * @param displayName   画面表示名
     * @param require       必須パラメータかどうか
     */
    protected AbstractParameterDefinition(String parameterName, String displayName, boolean require) {
        this.parameterName = parameterName;
        this.displayName = displayName;
        this.require = require;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParameterName() {
        return this.parameterName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public boolean isRequire() {
        return this.require;
    }

}