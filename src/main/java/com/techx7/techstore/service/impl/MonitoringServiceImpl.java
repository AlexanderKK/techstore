package com.techx7.techstore.service.impl;

import com.techx7.techstore.service.MonitoringService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MonitoringServiceImpl implements MonitoringService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringServiceImpl.class);
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
        int userLoginsCount = (int) usersLogins.count();

        LOGGER.info("User logins count: " + userLoginsCount);

        return userLoginsCount;
    }

}
