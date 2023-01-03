package org.nortis.infrastructure.mvc;

import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestController {

	public static final String DOMAIN_ID = "domainId";

	public static final String UNEXPECTED_ID = "unexpectedId";

	@GetMapping("/test1")
	public void domain() {
		throw new DomainException(DOMAIN_ID);
	}

	@GetMapping("/test2")
	public void unexpected() {
		throw new UnexpectedException(UNEXPECTED_ID);
	}
	
}
