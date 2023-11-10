package net.risesoft.api.permission;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.risesoft.enums.AuthorityEnum;
import net.risesoft.model.Resource;
import net.risesoft.pojo.Y9Result;

/**
 * 岗位资源权限组件
 *
 * @author dingzhaojun
 * @author qinman
 * @author mengjuhua
 * @date 2022/2/10
 * @since 9.6.0
 */
@Validated
public interface PositionResourceApi {

    /**
     * 判断person对resource是否有指定的操作权限
     *
     * @param tenantId 租户id
     * @param positionId 操作者唯一标识
     * @param resourceId 资源唯一标识
     * @param authority 操作类型 {@link AuthorityEnum}
     * @return Boolean 是否有权限
     * @since 9.6.0
     */
    @GetMapping("/hasPermission")
    Y9Result<Boolean> hasPermission(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("positionId") @NotBlank String positionId,
        @RequestParam("resourceId") @NotBlank String resourceId, @RequestParam("authority") Integer authority);

    /**
     * 判断岗位对 customId 对应的资源是否有指定的操作权限
     *
     * @param tenantId 租户id
     * @param positionId 岗位id
     * @param customId 自定义id
     * @param authority 操作类型 {@link AuthorityEnum}
     * @return boolean
     * @since 9.6.0
     */
    @GetMapping("/hasPermissionByCustomId")
    Y9Result<Boolean> hasPermissionByCustomId(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("positionId") @NotBlank String positionId, @RequestParam("customId") @NotBlank String customId,
        @RequestParam("authority") Integer authority);

    /**
     * 获得某一资源下,主体对象有相应操作权限的子菜单
     *
     * @param tenantId 租户id
     * @param positionId 操作者唯一标识
     * @param authority 操作类型 {@link AuthorityEnum}
     * @param resourceId 资源唯一标识
     * @return List&lt;Resource&gt; 有操作权限的子菜单
     * @since 9.6.0
     */
    @GetMapping("/listSubMenus")
    Y9Result<List<Resource>> listSubMenus(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("positionId") @NotBlank String positionId, @RequestParam("authority") Integer authority,
        @RequestParam("resourceId") @NotBlank String resourceId);

    /**
     * 获得某一资源下,主体对象有相应操作权限的子节点
     *
     * @param tenantId 租户id
     * @param positionId 操作者唯一标识
     * @param authority 操作类型 {@link AuthorityEnum}
     * @param resourceId 资源唯一标识
     * @return List&lt;Resource&gt; 有操作权限的子节点
     * @since 9.6.0
     */
    @GetMapping("/listSubResources")
    Y9Result<List<Resource>> listSubResources(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("positionId") @NotBlank String positionId, @RequestParam("authority") Integer authority,
        @RequestParam(name = "resourceId", required = false) String resourceId);
}
