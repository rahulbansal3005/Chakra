package com.increff.chakra.config;

import com.nextscm.commons.spring.server.AbstractWebInitializer;

public class WebInitializer extends AbstractWebInitializer {

	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { AppConfig.class };
	}

}