package com.gcoder.securityflux.base;

import lombok.Getter;

@Getter
public enum SecurityWebFiltersOrder {
    FIRST(Integer.MIN_VALUE),
    HTTP_HEADERS_WRITER,
    HTTPS_REDIRECT,
    PERMIT_ALL,
    CORS,
    CSRF,
    REACTOR_CONTEXT,
    HTTP_BASIC,
    FORM_LOGIN, AUTHENTICATION,
    ANONYMOUS_AUTHENTICATION,
    OAUTH2_AUTHORIZATION_CODE,
    LOGIN_PAGE_GENERATING,
    LOGOUT_PAGE_GENERATING,
    SECURITY_CONTEXT_SERVER_WEB_EXCHANGE,
    SERVER_REQUEST_CACHE,
    LOGOUT,
    EXCEPTION_TRANSLATION,
    AUTHORIZATION,
    LAST(Integer.MAX_VALUE);
    private static final int INTERVAL = 100;
    private final int order;
    SecurityWebFiltersOrder() {
        this.order = ordinal() * INTERVAL;
    }
    SecurityWebFiltersOrder(int order) {
        this.order = order;
    }
}
