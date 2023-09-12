package com.devtoon.jtoon.security.interceptor;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

import com.devtoon.jtoon.global.common.MemberThreadLocal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MemberInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(
		@NotNull HttpServletRequest request,
		@NotNull HttpServletResponse response,
		@NotNull Object handler,
		Exception ex) throws Exception {
		if (MemberThreadLocal.getMember() != null) {
			MemberThreadLocal.clear();
		}
	}
}
