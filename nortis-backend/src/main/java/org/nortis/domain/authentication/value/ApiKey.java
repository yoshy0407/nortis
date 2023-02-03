package org.nortis.domain.authentication.value;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

/**
 * APIキー
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@EqualsAndHashCode
@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "create")
public class ApiKey {

	private final String value;
	
	private ApiKey(String value) {
		Validations.hasText(value, "APIキー");
		Validations.maxTextLength(value, 36, "APIキー");
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	
	/**
	 * APIキーを構築します
	 * @param value 文字列
	 * @return APIキー
	 */
	public static ApiKey create(String value) {
		return new ApiKey(value);
	}
	
	/**
	 * APIキーを新規生成します
	 * @return APIキー
	 */
	public static ApiKey newKey() {
		return new ApiKey(UUID.randomUUID().toString());
	}
	
}
