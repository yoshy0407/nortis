package org.nortis.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.infrastructure.message.MessageCodes;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * APIキー認証のエラーレスポンスを処理する{@link AuthenticationFailureHandler}です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Slf4j
@AllArgsConstructor
public class NortisAuthenticaitonFailureHandler implements AuthenticationFailureHandler {

	private final MessageSource messageSource;
	
	private final ObjectMapper objectMapper;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		if (exception instanceof UsernameNotFoundException) {
			authenticationError(response, (UsernameNotFoundException) exception);
		} else if (exception instanceof CredentialsExpiredException) {
			expireError(response, (CredentialsExpiredException) exception);
		} else {
			log.error("想定外の認証エラー", exception);
			otherError(response);
		}
	}
	
	private void authenticationError(HttpServletResponse response, UsernameNotFoundException ex) throws IOException {
		ObjectNode json = createResponse(ex.getMessage());
		response.getWriter().write(json.toString());
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}

	private void expireError(HttpServletResponse response, CredentialsExpiredException ex) throws IOException {
		ObjectNode json = createResponse(
				MessageCodes.nortis60002().resolveMessage(messageSource, LocaleContextHolder.getLocale()));
		response.getWriter().write(json.toString());
		response.setStatus(HttpStatus.UNAUTHORIZED.value());		
	}

	private void otherError(HttpServletResponse response) throws IOException {
		ObjectNode json = createResponse(
				MessageCodes.nortis60001().resolveMessage(messageSource, LocaleContextHolder.getLocale()));
		response.getWriter().write(json.toString());
		response.setStatus(HttpStatus.NOT_FOUND.value());		
	}

	private ObjectNode createResponse(String message) {
		ObjectNode object = this.objectMapper.createObjectNode();
		object.put("error", message);
		return object;
	}
}
