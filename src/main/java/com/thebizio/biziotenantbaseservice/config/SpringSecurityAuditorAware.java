package com.thebizio.biziotenantbaseservice.config;

import com.thebizio.biziotenantbaseservice.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Autowired
	private UtilService utilService;

	private static final String ANONYMOUS_USER = "service";

    @Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}

		if (authentication.getPrincipal().equals("anonymousUser")) {
			return Optional.of(ANONYMOUS_USER);
		}

		return Optional.of(utilService.getAuthUserName());
	}
}
