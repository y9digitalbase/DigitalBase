package org.apereo.cas.web.y9.authen;

import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.principal.PrincipalResolver;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.web.y9.service.Y9LoginUserService;
import org.apereo.cas.web.y9.service.Y9UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//@Component
public class Y9AuthenticationEventExecutionPlanConfigurer implements AuthenticationEventExecutionPlanConfigurer {

    //@Autowired
    //@Qualifier(PrincipalResolver.BEAN_NAME_PRINCIPAL_RESOLVER)
    PrincipalResolver defaultPrincipalResolver;

    //@Autowired
    //@Qualifier(ServicesManager.BEAN_NAME)
    ServicesManager servicesManager;

    //@Autowired
    Y9UserService y9UserService;

    //@Autowired
    Y9LoginUserService y9LoginUserService;


    @Override
    public void configureAuthenticationExecutionPlan(AuthenticationEventExecutionPlan plan) throws Exception {
        Y9AuthenticationHandler handler = new Y9AuthenticationHandler("y9AuthenticationHandler",
                servicesManager, 0, y9UserService, y9LoginUserService);
        plan.registerAuthenticationHandler(handler);
    }
}
