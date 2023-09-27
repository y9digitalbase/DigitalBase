package net.risesoft.y9.tenant.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.util.StringUtils;

import com.zaxxer.hikari.HikariDataSource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.risesoft.y9.Y9LoginUserHolder;

/**
 * @author dingzhaojun
 * @author qinman
 * @author mengjuhua
 * @author shidaobang
 *
 */
@Slf4j
@RequiredArgsConstructor
@Getter
public class Y9TenantDataSource extends AbstractDataSource {

    private final HikariDataSource defaultDataSource;
    private final Y9TenantDataSourceLookup dataSourceLookup;

    public HikariDataSource determineTargetDataSource() {
    	HikariDataSource dataSource = defaultDataSource;

        String lookupKey = Y9LoginUserHolder.getTenantId();
        if (StringUtils.hasText(lookupKey)) {
        	HikariDataSource tenantDataSource = (HikariDataSource)this.dataSourceLookup.getDataSource(lookupKey);
            if (tenantDataSource == null) {
                LOGGER.warn("租户[{}]未租用系统[{}]，将使用默认数据源", lookupKey, this.dataSourceLookup.getSystemName());
            } else {
                dataSource = tenantDataSource;
            }
        } else {
            LOGGER.warn("当前线程中租户ID为空，将使用默认数据源");
        }

        return dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
    	HikariDataSource ds = determineTargetDataSource();
        return ds.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
    	HikariDataSource ds = determineTargetDataSource();
        return ds.getConnection(username, password);
    }

}
