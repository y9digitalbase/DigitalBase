package net.risesoft.y9.configuration.feature.oauth2.resource;

import java.util.List;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import net.risesoft.y9.configuration.feature.oauth2.resource.basic.Y9Oauth2ResourceBasicAuthenProperties;
import net.risesoft.y9.configuration.feature.oauth2.resource.jwt.Y9Oauth2ResourceJwtTokenProperties;
import net.risesoft.y9.configuration.feature.oauth2.resource.opaque.Y9Oauth2ResourceOpaqueTokenProperties;

/**
 * oauth2 资源服务器配置
 *
 * @author liansen
 * @date 2022/09/28
 */
@Getter
@Setter
public class Y9Oauth2ResourceProperties {

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 是否保存在线消息
     */
    private boolean saveOnlineMessage = true;

    /**
     * 在线消息推送方式
     */
    private OnlineMessagePushType onlineMessagePushType = OnlineMessagePushType.API;

    /**
     * 是否允许从请求体参数中获取访问令牌
     */
    private boolean allowFormEncodedBodyParameter;

    /**
     * 是否允许从 uri 查询参数中获取访问令牌
     */
    private boolean allowUriQueryParameter;

    /**
     * 允许基本身份验证
     */
    private boolean allowBasicAuthentication;

    /**
     * 受保护 url 正则
     */
    private List<String> protectedUrlPatterns;

    /**
     * baisc
     */
    @NestedConfigurationProperty
    private Y9Oauth2ResourceBasicAuthenProperties basic = new Y9Oauth2ResourceBasicAuthenProperties();

    /**
     * jwt
     */
    @NestedConfigurationProperty
    private Y9Oauth2ResourceJwtTokenProperties jwt = new Y9Oauth2ResourceJwtTokenProperties();

    /**
     * opaque
     */
    @NestedConfigurationProperty
    private Y9Oauth2ResourceOpaqueTokenProperties opaque = new Y9Oauth2ResourceOpaqueTokenProperties();

    @Getter
    @AllArgsConstructor
    public enum OnlineMessagePushType {
        API, KAFKA
    }
}
