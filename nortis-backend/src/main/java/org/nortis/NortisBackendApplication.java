package org.nortis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * アプリケーションのメインクラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@SpringBootApplication
public class NortisBackendApplication {

	/**
	 * アプリケーションのエントリメソッドです
	 * @param args コマンドラインの引数
	 */
	public static void main(String[] args) {
		SpringApplication.run(NortisBackendApplication.class, args);
	}

}
