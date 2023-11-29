package net.risesoft;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import net.risesoft.service.init.InitTenantDataService;
import net.risesoft.y9.Y9LoginUserHolder;

import y9.autoconfiguration.liquibase.TenantDataInitializer;

/**
 * 租户数据初始化器
 *
 * @author shidaobang
 * @date 2023/11/28
 * @since 9.6.3
 */
@RequiredArgsConstructor
@Component
public class PlatformTenantDataInitializer implements TenantDataInitializer {

    private final InitTenantDataService initTenantDataService;

    @Override
    public void init(String tenantId) {
        Y9LoginUserHolder.setTenantId(tenantId);
        initTenantDataService.initAll(tenantId);
    }
}
