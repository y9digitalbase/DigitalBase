package org.apereo.cas.rest.factory;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.BooleanUtils;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.RememberMeCredential;
import org.apereo.cas.authentication.credential.Y9Credential;
import org.apereo.cas.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

/**
 * This is {@link UsernamePasswordRestHttpRequestCredentialFactory}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
@Slf4j
@Getter
@Setter
public class UsernamePasswordRestHttpRequestCredentialFactory implements RestHttpRequestCredentialFactory {
    private int order = Integer.MIN_VALUE;

    @Override
    public List<Credential> fromRequest(final HttpServletRequest request,
        final MultiValueMap<String, String> requestBody) {
        if (requestBody == null || requestBody.isEmpty()) {
            LOGGER.debug("Skipping {} because the requestBody is null or empty", getClass().getSimpleName());
            return new ArrayList<>(0);
        }
        final String username = requestBody.getFirst(RestHttpRequestCredentialFactory.PARAMETER_USERNAME);
        final String password = requestBody.getFirst(RestHttpRequestCredentialFactory.PARAMETER_PASSWORD);
        val rememberMe = requestBody.getFirst(RememberMeCredential.REQUEST_PARAMETER_REMEMBER_ME);
        final String tenantShortName = requestBody.getFirst("tenantShortName");
        final String deptId = requestBody.getFirst("deptId");
        final String positionId = requestBody.getFirst("positionId");
        final String loginType = requestBody.getFirst("loginType");
        final String screenDimension = requestBody.getFirst("screenDimension");
        final String systemName = requestBody.getFirst("systemName");

        if (username == null || password == null) {
            LOGGER.debug("Invalid payload. 'username' and 'password' form fields are required.");
            return new ArrayList<>(0);
        }

        Y9Credential c = new Y9Credential();
        c.setTenantShortName(tenantShortName);
        c.setUsername(username);
        c.assignPassword(password);
        c.setNoLoginScreen("true");

        c.setDeptId(deptId);
        c.setPositionId(positionId);
        c.setLoginType(loginType);
        c.setScreenDimension(screenDimension);
        c.setSystemName(systemName);
        c.setRememberMe(BooleanUtils.toBoolean(rememberMe));

        return CollectionUtils.wrap(c);
    }
}
