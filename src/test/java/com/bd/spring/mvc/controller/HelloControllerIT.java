package com.bd.spring.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({ "classpath:applicationContext.xml", "file:spring-mvc/WEB-INF/mvc-dispatcher-servlet.xml" })
 @ContextConfiguration({ "classpath:applicationContext.xml" })
public class HelloControllerIT {

	@Autowired
	// private ApplicationContext applicationContext;
	private WebApplicationContext applicationContext;

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private HandlerAdapter handlerAdapter;

	@Before
	public void setUp() throws Exception {
		this.request = new MockHttpServletRequest();
		this.response = new MockHttpServletResponse();

		this.handlerAdapter = applicationContext.getBean(HandlerAdapter.class);
	}

	ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		final HandlerMapping handlerMapping = applicationContext.getBean(HandlerMapping.class);
		final HandlerExecutionChain handler = handlerMapping.getHandler(request);
		Assert.assertNotNull("No handler found for request, check you request mapping", handler);

		final Object controller = handler.getHandler();
		// if you want to override any injected attributes do it here

		final HandlerInterceptor[] interceptors = handlerMapping.getHandler(request).getInterceptors();
		for (HandlerInterceptor interceptor : interceptors) {
			final boolean carryOn = interceptor.preHandle(request, response, controller);
			if (!carryOn) {
				return null;
			}
		}

		final ModelAndView mav = handlerAdapter.handle(request, response, controller);
		return mav;
	}

	@Test
	public void testGetMessage() throws Exception {
		request.setMethod("GET");
		request.setRequestURI("/welcome");

		final ModelAndView mav = handle(request, response);
		// make assertions on the ModelAndView here
	}

	@Test
	public void testPostMessage() throws Exception {
		request.setMethod("POST");
		request.setRequestURI("/welcome");
		// set some request parameters for binding

		final ModelAndView mav = handle(request, response);
		// make assertions on the ModelAndView here plus any side effects
	}

}