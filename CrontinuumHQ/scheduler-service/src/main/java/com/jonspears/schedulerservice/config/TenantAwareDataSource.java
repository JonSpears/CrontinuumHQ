package com.jonspears.schedulerservice.config;

import com.jonspears.sharedlibs.tenant.TenantContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class TenantAwareDataSource extends AbstractDataSource {
    private final DataSource delegate;

    public TenantAwareDataSource(DataSource delegate) {
        this.delegate = delegate;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = delegate.getConnection();
        applyTenantContext(connection);
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection = delegate.getConnection(username, password);
        applyTenantContext(connection);
        return connection;
    }

    private void applyTenantContext(Connection connection) throws SQLException {
        String tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            connection.createStatement().execute(
                    "SET app.current_tenant = '%s'".formatted(tenantId)
            );
        }
    }
}
