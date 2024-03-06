package com.gcoder.securityflux.base.impl;

import com.gcoder.securityflux.base.GCWebFilter;
import com.gcoder.securityflux.throwable.TooManyRequest;
import io.github.bucket4j.Bucket;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Log4j2
public class AntiDDOSFilter implements GCWebFilter {
    int capacity;
    int refillGreedy;
    private final Map<String, Bucket> cache;

    public AntiDDOSFilter(int capacity, int refillGreedy) {
        this.capacity = capacity;
        this.refillGreedy = refillGreedy;
        cache = new ConcurrentHashMap<>();
    }
    @Scheduled(fixedRate = 1, initialDelay = 1, timeUnit = TimeUnit.DAYS)
    public void dailyResetIpMap(){
        cache.clear();
    }

    /**
     * limit -> limit.capacity(3): Đây là một hàm lambda, và limit là tham số của nó. Phương thức capacity(3) được gọi trên đối tượng limit. Nó thiết lập dung lượng (capacity) của "Bucket" là 3, tức là "Bucket" có thể chứa tối đa 3 "tokens" (một token thường được hiểu là một quyền truy cập).
     * .refillGreedy(3, Duration.ofMinutes(1)): Phương thức refillGreedy được gọi để thiết lập tần suất làm mới (refill rate) của "Bucket". Trong trường hợp này, 3 là số lượng "tokens" được bổ sung mỗi lần làm mới và Duration.ofMinutes(1) là khoảng thời gian giữa các lần làm mới, ở đây là 1 phút.
     */
    private void updateCache(String IPAddress) {
        cache.putIfAbsent(IPAddress,
                Bucket.builder()
                        .addLimit(limit -> limit.capacity(capacity).refillGreedy(refillGreedy, Duration.ofMinutes(1)))
                        .build());
    }

    private boolean isRequestRateOk(String IPAddress) {
        return cache.get(IPAddress).tryConsume(1);
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            try {
                String ipAddress = exchange.getRequest().getRemoteAddress().toString();
                updateCache(ipAddress);
                if (!isRequestRateOk(ipAddress)) {
                    throw new TooManyRequest("Too Many Requests");
                }
                return Mono.empty(); // Trả về Mono<Void> thành công nếu không có ngoại lệ
            } catch (Exception e) {
                return Mono.error(e); // Trả về Mono<Void> với lỗi nếu có ngoại lệ
            }
        });
    }
}
