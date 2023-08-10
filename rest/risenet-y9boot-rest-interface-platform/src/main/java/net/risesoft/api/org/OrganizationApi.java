package net.risesoft.api.org;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.risesoft.model.Department;
import net.risesoft.model.Group;
import net.risesoft.model.Organization;
import net.risesoft.model.Person;
import net.risesoft.model.Position;

/**
 * 机构服务组件
 *
 * @author dingzhaojun
 * @author qinman
 * @author mengjuhua
 * @date 2022/2/10
 * @since 9.6.0
 */
@Validated
public interface OrganizationApi {

    /**
     * 根据id获得机构对象
     *
     * @param tenantId 租户id
     * @param organizationId 机构唯一标识
     * @return Organization 对象
     * @since 9.6.0
     */
    @GetMapping("/get")
    Organization getOrganization(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("organizationId") @NotBlank String organizationId);

    /**
     * 返回所有委办局
     *
     * @param tenantId 租户id
     * @param organizationId 机构id
     * @return List&lt;Department&gt; 部门对象集合
     * @since 9.6.0
     */
    @GetMapping("/listAllBureaus")
    List<Department> listAllBureaus(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("organizationId") @NotBlank String organizationId);

    /**
     * 根据租户id获取所有机构
     *
     * @param tenantId 租户id
     * @return List&lt;Organization&gt; 机构对象集合
     * @since 9.6.0
     */
    @GetMapping("/listAllOrganizations")
    List<Organization> listAllOrganizations(@RequestParam("tenantId") @NotBlank String tenantId);

    /**
     * 通过类型，获取组织架构列表
     *
     * @param tenantId 租户id
     * @param virtual 是否为虚拟组织
     * @return List&lt;Organization&gt; 组织架构对象集合
     * @since 9.6.0
     */
    @GetMapping("/listByType")
    List<Organization> listByType(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("virtual") Boolean virtual);

    /**
     * 获取机构下的部门（下一级）
     *
     * @param tenantId 租户id
     * @param organizationId 机构唯一标识
     * @return List&lt;Department&gt; 部门对象集合
     * @since 9.6.0
     */
    @GetMapping("/listDepartments")
    List<Department> listDepartments(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("organizationId") @NotBlank String organizationId);

    /**
     * 获取用户组（下一级）
     *
     * @param tenantId 租户id
     * @param organizationId 机构唯一标识
     * @return List&lt;Group&gt; 用户组对象集合
     * @since 9.6.0
     */
    @GetMapping("/listGroups")
    List<Group> listGroups(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("organizationId") @NotBlank String organizationId);

    /**
     * 获取人员（下一级）
     *
     * @param tenantId 租户id
     * @param organizationId 机构唯一标识
     * @return List&lt;Person&gt; 人员对象集合
     * @since 9.6.0
     */
    @GetMapping("/listPersons")
    List<Person> listPersons(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("organizationId") @NotBlank String organizationId);

    /**
     * 获取岗位（下一级）
     *
     * @param tenantId 租户id
     * @param organizationId 机构唯一标识
     * @return List&lt;Position&gt; 岗位对象集合
     * @since 9.6.0
     */
    @GetMapping("/listPositions")
    List<Position> listPositions(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("organizationId") @NotBlank String organizationId);

}