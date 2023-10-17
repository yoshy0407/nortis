package org.nortis.infrastructure.template;

import java.util.Map;

/**
 * テンプレートをレンダリングするインターフェースです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface TemplateRender {

    /**
     * テンプレートをレンダリングします
     * 
     * @param endpointId   エンドポイントID
     * @param templateText テンプレートテキスト
     * @param parameter    テンプレートの引数
     * @return テンプレートのテキスト
     */
    String render(String endpointId, String templateText, Map<String, Object> parameter);

}
