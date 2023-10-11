package org.nortis.port.rest.payload;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * コンシューマを作成するリクエスト
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class ConsumerUpdateRequest {

    @NotBlank
    private String consumerName;

    @NotBlank
    private String consumerType;

    @NotBlank
    private String textType;

    private List<ConsumerUpdateParameter> parameters;

}
