package y9;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apereo.cas.CentralAuthenticationService;
import org.apereo.cas.authentication.AuthenticationSystemSupport;
import org.apereo.cas.authentication.principal.ServiceFactory;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.web.CasWebSecurityConfigurer;
import org.apereo.cas.web.cookie.CasCookieBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWarDeployment;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.web.filter.ForwardedHeaderFilter;
import y9.controller.*;
import y9.service.Y9KeyValueService;
import y9.service.Y9LoginUserService;
import y9.service.Y9UserService;
import y9.service.impl.Y9KeyValueServiceImpl;
import y9.service.impl.Y9LoginUserServiceImpl;
import y9.service.impl.Y9UserServiceImpl;
import y9.util.Y9Context;

import java.util.List;

@EnableConfigurationProperties(CasConfigurationProperties.class)
@EnableTransactionManagement(proxyTargetClass = false)
@Configuration
public class Y9AutoConfigService {
    /**
     * 针对经过反向代理的请求不能正确获得一些原始的请求信息，例如不能正确获得原始的 schema 导致重定向的 url 错误 <br/>
     * 此过滤器更多是为了减少外部 servlet 容器的配置 <br/>
     *
     * @return {@code FilterRegistrationBean<ForwardedHeaderFilter> }
     * @see <a href="https://docs.spring.io/spring-security/reference/servlet/appendix/proxy-server.html">Proxy
     * Server Configuration</a>
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
    public CasWebSecurityConfigurer<Void> y9ResourceConfigurer() {
        return new CasWebSecurityConfigurer<>() {
            @Override
            public List<String> getIgnoredEndpoints() {
                return List.of("/y9static/**", "/api/**");
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(value = Y9Context.class)
    public Y9Context y9Context() {
        return new Y9Context();
    }

    @Bean
    @ConditionalOnMissingBean(name = "y9UserService")
    public Y9UserService y9UserService() {
        return new Y9UserServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "y9LoginUserService")
    public Y9LoginUserService y9LoginUserService() {
        return new Y9LoginUserServiceImpl();
    }

    @Bean
    public Y9KeyValueService y9KeyValueService(
            @Qualifier("jdbcServiceRegistryTransactionTemplate") TransactionOperations transactionTemplate) {
        return new Y9KeyValueServiceImpl(transactionTemplate);
    }

    @Bean
    public QRCodeController qRCodeController(Y9KeyValueService y9KeyValueService) {
        return new QRCodeController(y9KeyValueService);
    }

    @Bean
    public LoginController loginController(CentralAuthenticationService centralAuthenticationService,
                                           @Qualifier("ticketGrantingTicketCookieGenerator") CasCookieBuilder ticketGrantingTicketCookieGenerator,
                                           @Qualifier("defaultAuthenticationSystemSupport") AuthenticationSystemSupport authenticationSystemSupport,
                                           @Qualifier("webApplicationServiceFactory") ServiceFactory webApplicationServiceFactory,
                                           Y9UserService y9UserService) {
        return new LoginController(centralAuthenticationService,
                ticketGrantingTicketCookieGenerator,
                authenticationSystemSupport,
                webApplicationServiceFactory,
                y9UserService);
    }

    @Bean
    public ServiceController serviceController(ServicesManager servicesManager,
                                               CasConfigurationProperties casConfigurationProperties) {
        return new ServiceController(servicesManager, casConfigurationProperties);
    }

    @Bean
    public TenantController tenantController(Y9UserService y9UserService) {
        return new TenantController(y9UserService);
    }

    @Bean
    public CheckController checkController() {
        return new CheckController();
    }

    @Bean
    public OnApplicationReady onApplicationReady(Y9UserService y9UserService) {
        return new OnApplicationReady(y9UserService);
    }


    @Bean
    public Runnable Y9KeyValueCleaner(Y9KeyValueService y9KeyValueService) {
        return new Y9KeyValueCleaner(y9KeyValueService);
    }

    @Slf4j
    @RequiredArgsConstructor
    @EnableScheduling
    static class Y9KeyValueCleaner implements Runnable {
        private final Y9KeyValueService y9KeyValueService;

        @Override
        @Scheduled(
                cron = "0 * * * * *"
        )
        public void run() {
            //Y9KeyValueService y9KeyValueService = Y9Context.getBean(Y9KeyValueService.class);
            y9KeyValueService.cleanUpExpiredKeyValue();
        }
    }

}
