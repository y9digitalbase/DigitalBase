package y9;

import java.util.List;

import javax.servlet.DispatcherType;
import javax.sql.DataSource;

import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.RiseAuthenticationHandler;
import org.apereo.cas.authentication.adaptive.AdaptiveAuthenticationPolicy;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.authentication.principal.PrincipalResolver;
import org.apereo.cas.authentication.principal.RisePersonDirectoryPrincipalResolver;
import org.apereo.cas.jpa.JpaPersistenceProviderConfigurer;
import org.apereo.cas.services.JpaRegisteredServiceEntity;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.services.Y9KeyValue;
import org.apereo.cas.services.Y9LoginUser;
import org.apereo.cas.services.Y9User;
import org.apereo.cas.util.CollectionUtils;
import org.apereo.cas.util.spring.beans.BeanCondition;
import org.apereo.cas.util.spring.beans.BeanSupplier;
import org.apereo.cas.web.ProtocolEndpointWebSecurityConfigurer;
import org.apereo.cas.web.flow.actions.RiseCredentialNonInteractiveCredentialsAction;
import org.apereo.cas.web.flow.resolver.CasDelegatingWebflowEventResolver;
import org.apereo.cas.web.flow.resolver.CasWebflowEventResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWarDeployment;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.Ordered;
import org.springframework.integration.transaction.PseudoTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.webflow.execution.Action;

import lombok.val;

import y9.service.Y9KeyValueService;
import y9.service.Y9LoginUserService;
import y9.service.Y9UserService;
import y9.service.impl.Y9JpaKeyValueServiceImpl;
import y9.util.Y9Context;

@Lazy(false)
@Configuration(proxyBeanMethods = false)
public class Y9Config {

    @Configuration
    public static class Y9AuthenticationConfiguration {
        @Bean
        public AuthenticationEventExecutionPlanConfigurer riseAuthenticationEventExecutionPlanConfigurer(
            @Qualifier(ServicesManager.BEAN_NAME) final ServicesManager servicesManager,
            PrincipalResolver risePersonDirectoryPrincipalResolver, Y9UserService y9UserService,
            Y9LoginUserService y9LoginUserService) {
            RiseAuthenticationHandler handler = new RiseAuthenticationHandler("y9AuthenticationHandler",
                servicesManager, risePrincipalFactory(), 0, y9UserService, y9LoginUserService);
            return plan -> plan.registerAuthenticationHandlerWithPrincipalResolver(handler,
                risePersonDirectoryPrincipalResolver);
        }

        @Bean
        public Action riseCredentialNonInteractiveCredentialsAction(
            @Qualifier("adaptiveAuthenticationPolicy") final AdaptiveAuthenticationPolicy adaptiveAuthenticationPolicy,
            @Qualifier("serviceTicketRequestWebflowEventResolver") final CasWebflowEventResolver serviceTicketRequestWebflowEventResolver,
            @Qualifier("initialAuthenticationAttemptWebflowEventResolver") final CasDelegatingWebflowEventResolver initialAuthenticationAttemptWebflowEventResolver) {
            return new RiseCredentialNonInteractiveCredentialsAction(initialAuthenticationAttemptWebflowEventResolver,
                serviceTicketRequestWebflowEventResolver, adaptiveAuthenticationPolicy);
        }

        @Bean
        public PrincipalResolver risePersonDirectoryPrincipalResolver() {
            return new RisePersonDirectoryPrincipalResolver();
        }

        @Bean
        public PrincipalFactory risePrincipalFactory() {
            return new DefaultPrincipalFactory();
        }
    }

    @Configuration
    public static class Y9CasWebSecurityConfigurer {

        /**
         * 针对经过反向代理的请求不能正确获得一些原始的请求信息，例如不能正确获得原始的 schema 导致重定向的 url 错误 <br/>
         * 此过滤器更多是为了减少外部 servlet 容器的配置 <br/>
         *
         * @return {@code FilterRegistrationBean<ForwardedHeaderFilter> }
         * @see <a href="https://docs.spring.io/spring-security/reference/servlet/appendix/proxy-server.html">Proxy
         *      Server Configuration</a>
         */
        @Bean
        @ConditionalOnWarDeployment
        FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
            ForwardedHeaderFilter filter = new ForwardedHeaderFilter();
            FilterRegistrationBean<ForwardedHeaderFilter> registration = new FilterRegistrationBean<>(filter);
            registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR);
            registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
            return registration;
        }

        @Bean
        public ProtocolEndpointWebSecurityConfigurer<Void> y9ResourceConfigurer() {
            return new ProtocolEndpointWebSecurityConfigurer<>() {
                @Override
                public List<String> getIgnoredEndpoints() {
                    return List.of("/y9static/**", "/api/**");
                }
            };
        }
    }

    @Configuration
    public static class Y9JpaConfig {
        private static final BeanCondition CONDITION =
            BeanCondition.on("cas.service-registry.jpa.enabled").isTrue().evenIfMissing();
        private static final BeanCondition CONDITION2 =
            BeanCondition.on("cas.ticket.registry.jpa.enabled").isTrue().evenIfMissing();

        @Bean
        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
        public JpaPersistenceProviderConfigurer
            jpaServicePersistenceProviderConfigurer(final ConfigurableApplicationContext applicationContext) {
            return BeanSupplier.of(JpaPersistenceProviderConfigurer.class)
                .when(CONDITION.given(applicationContext.getEnvironment())).supply(() -> context -> {
                    String s1 = JpaRegisteredServiceEntity.class.getName();
                    String s2 = Y9LoginUser.class.getName();
                    String s3 = Y9User.class.getName();
                    String s4 = Y9KeyValue.class.getName();
                    val entities = CollectionUtils.wrapList(s1, s2, s3, s4);
                    context.getIncludeEntityClasses().addAll(entities);
                }).otherwiseProxy().get();
        }

        @Primary
        @Bean
        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
        public PlatformTransactionManager jdbcLockTransactionManager(
            final ConfigurableApplicationContext applicationContext,
            @Qualifier("dataSourceTicket") DataSource dataSourceTicket) {
            return BeanSupplier.of(PlatformTransactionManager.class)
                .when(CONDITION2.given(applicationContext.getEnvironment()))
                .supply(() -> new DataSourceTransactionManager(dataSourceTicket))
                .otherwise(PseudoTransactionManager::new).get();
        }
    }

    @Configuration(proxyBeanMethods = false)
    public static class Y9KeyValueConfiguration {
        @Bean
        public Y9Context y9Context() {
            return new Y9Context();
        }

        @Configuration(proxyBeanMethods = false)
        @ConditionalOnProperty(name = "cas.ticket.registry.jpa.enabled", havingValue = "true", matchIfMissing = false)
        public static class Y9JpaKeyValueConfiguration {

            @Bean
            public Y9KeyValueService y9JpaKeyValueService(
                @Qualifier("jdbcServiceRegistryTransactionTemplate") TransactionOperations transactionTemplate) {
                return new Y9JpaKeyValueServiceImpl(transactionTemplate);
            }

            @EnableScheduling
            @Configuration(proxyBeanMethods = false)
            class Y9KeyValueCleanupConfiguration implements SchedulingConfigurer {
                // 每分钟执行一次
                private final String cleanupCron = "0 * * * * *";

                private final Y9KeyValueService y9KeyValueService;

                public Y9KeyValueCleanupConfiguration(Y9KeyValueService y9KeyValueService) {
                    this.y9KeyValueService = y9KeyValueService;
                }

                @Override
                public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
                    taskRegistrar.addCronTask(this.y9KeyValueService::cleanUpExpiredKeyValue, this.cleanupCron);
                }

            }
        }
    }

}
