package y9.authen;

import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.principal.PrincipalResolver;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;
import y9.service.Y9LoginUserService;
import y9.service.Y9UserService;

@Configuration(proxyBeanMethods = false)
public class Y9AuthenticationConfiguration {

    @Bean
    @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
    public AuthenticationEventExecutionPlanConfigurer riseAuthenticationEventExecutionPlanConfigurer(
            @Qualifier(PrincipalResolver.BEAN_NAME_PRINCIPAL_RESOLVER)
            final PrincipalResolver defaultPrincipalResolver,
            @Qualifier(ServicesManager.BEAN_NAME)
            final ServicesManager servicesManager,
            Y9UserService y9UserService,
            Y9LoginUserService y9LoginUserService) {
        Y9AuthenticationHandler handler = new Y9AuthenticationHandler("y9AuthenticationHandler",
                servicesManager, 0, y9UserService, y9LoginUserService);
        return plan -> plan.registerAuthenticationHandler(handler);
    }



}
