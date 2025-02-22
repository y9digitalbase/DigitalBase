package net.risesoft.config;

import java.sql.Connection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.engine.spi.SessionImplementor;
import org.javers.repository.sql.ConnectionProvider;

public class Y9JpaHibernateConnectionProvider implements ConnectionProvider {

    @PersistenceContext(unitName = "y9Public")
    private EntityManager entityManager;

    @Override
    public Connection getConnection() {

        SessionImplementor session = entityManager.unwrap(SessionImplementor.class);

        return session.connection();
    }

}
