package com.techx7.techstore.service.impl;

import com.techx7.techstore.service.MonitoringService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MonitoringServiceImpl implements MonitoringService {

    private final Counter usersLogins;

    public MonitoringServiceImpl(MeterRegistry meterRegistry) {
        this.usersLogins = Counter
                .builder("users_login_counter")
                .description("How many user logins have been made")
                .register(meterRegistry);
    }

    @Override
    public void logUsersLogins() {
        usersLogins.increment();
    }

    @Override
    public int getUsersLogins() {
        return (int) usersLogins.count();
    }

}
