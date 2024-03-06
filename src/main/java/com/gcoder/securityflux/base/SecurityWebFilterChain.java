package com.gcoder.securityflux.base;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.*;

@AllArgsConstructor
public class SecurityWebFilterChain implements WebFilter {
    private final Set<String> pathAll;
    private final Set<String> pathAuthentication;
    private final Map<String, Set<String>> pathByRole;
    private final List<GCWebFilter> gcWebFilters;
    private final UserAuthentication userAuthentications;

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        return Mono.just(exchange).flatMap(this::checkAuth).flatMap(this::filterAllPermit).flatMap(chain::filter);
    }

    //st1
    private Mono<ServerWebExchange> checkAuth(ServerWebExchange exchange) {
        return Mono.just(exchange).flatMap(this::getInfoCheck).flatMap(this::authFilter);
    }

    private Mono<ServerWebExchange> filterAllPermit(ServerWebExchange exchange) {
        if (hasFilterPermitAll(exchange)) {
            List<Mono<Void>> filterMonos = new ArrayList<>();
            for (GCWebFilter gcWebFilter : gcWebFilters) {
                filterMonos.add(gcWebFilter.filter(exchange));
            }
            return Mono.when(filterMonos).thenReturn(exchange);
        }
        return Mono.just(exchange);
    }


    //2nd

    private Mono<Map<String, Object>> getInfoCheck(ServerWebExchange exchange) {
        String uri = exchange.getRequest().getURI().getPath();
        boolean hasAuth = pathAuthentication.stream().anyMatch(path -> checkURI(uri, path));
        Set<String> roles = new HashSet<>();
        for (Map.Entry<String, Set<String>> rolePath : pathByRole.entrySet()) {
            boolean hasRole = rolePath.getValue().stream().anyMatch(path -> checkURI(uri, path));
            if (hasRole) {
                roles.add(rolePath.getKey());
                hasAuth = true;
            }
        }
        return Mono.just(Map.of("exchange", exchange, "hasAuth", hasAuth, "roles", roles));
    }

    private Mono<ServerWebExchange> authFilter(Map<String, Object> info) {
        boolean hasAuth = (boolean) info.get("hasAuth");
        ServerWebExchange exchange = (ServerWebExchange) info.get("exchange");
        Set<String> roles = (Set<String>) info.get("roles");
        if (hasAuth) {
            return userAuthentications.getUserDetail(exchange).flatMap(userDetails -> {
                exchange.getAttributes().put("userDetails", userDetails);
                if (checkRole(userDetails, roles)) {
                    return Mono.just(exchange);
                } else {
                    return Mono.error(new FilterChainException("No has in roles " + roles));
                }
            });
        }
        return Mono.just(exchange);
    }

    /**
     * Kiem tra user co role ton tai trong yeu cau khong, neu khong co thi nem {@link  FilterChainException}
     */
    private boolean checkRole(UserDetails userDetails, Set<String> roles) {
        if (roles.isEmpty()) return true;
        for (String roleOfUser : userDetails.getRole()) {
            for (String roleRequired : roles) {
                if (roleOfUser.equals(roleRequired)) return true;
            }
        }
        return false;
    }

    private boolean hasFilterPermitAll(ServerWebExchange exchange) {
        String uri = exchange.getRequest().getURI().getPath();
        for (String path : pathAll) {
            if (checkURI(uri, path)) return true;
        }
        return false;
    }


    //3rd
    private static boolean checkURI(String uri, String path) {
        if (path.endsWith("/**")) {
            return uri.startsWith(path.substring(0, path.lastIndexOf("/**") + 1));
        } else {
            return uri.equals(path);
        }
    }


    //record
    public static class FilterChainException extends Exception {
        public FilterChainException(String message) {
            super(message);
        }
    }

    private record HasAuth(Set<String> hasRole, boolean hasAuth) {
    }

    // main
    public static void main(String[] args) {
        String uri = "/hello/abcd";
        String path = "/hello/**";
        System.out.println(checkURI(uri, path));
    }
}
