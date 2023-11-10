package net.risesoft.api.org;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.risesoft.model.Department;
import net.risesoft.model.OrgUnit;
import net.risesoft.model.Organization;
import net.risesoft.pojo.Y9Result;

/**
 * 组织节点组件
 *
 * @author dingzhaojun
 * @author qinman
 * @author mengjuhua
 * @date 2022/2/10
 * @since 9.6.0
 */
@Validated
public interface OrgUnitApi {

    /**
     * 根据租户id和节点id获取委办局
     *
     * @param tenantId 租户id
     * @param orgUnitId 组织节点唯一标识
     * @return OrgUnit 组织节点对象（部门或组织机构）
     * @since 9.6.0
     */
    @GetMapping("/getBureau")
    Y9Result<OrgUnit> getBureau(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("orgUnitId") @NotBlank String orgUnitId);

    /**
     * 获得部门树
     *
     * @param tenantId 租户id
     * @param orgUnitId 组织节点唯一标识(可能是机构ID,也可能是部门ID)
     * @return List<Department> 部门对象集合
     * @since 9.6.0
     */
    @GetMapping("/getDeptTrees")
    Y9Result<List<Department>> getDeptTrees(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("orgUnitId") @NotBlank String orgUnitId);

    /**
     * 获取组织节点所在的组织机构
     *
     * @param tenantId 租户id
     * @param orgUnitId 组织节点唯一标识
     * @return Organization 组织机构对象
     * @since 9.6.0
     */
    @GetMapping("/getOrganization")
    Y9Result<Organization> getOrganization(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("orgUnitId") @NotBlank String orgUnitId);

    /**
     * 根据id获得组织节点对象
     *
     * @param tenantId 租户id
     * @param orgUnitId 组织节点唯一标识
     * @return OrgUnit 组织节点对象
     * @since 9.6.0
     */
    @GetMapping("/get")
    Y9Result<OrgUnit> getOrgUnit(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("orgUnitId") @NotBlank String orgUnitId);

    /**
     * 根据id，获取已删除的组织节点
     *
     * @param tenantId 租户id
     * @param orgUnitId 组织节点唯一标识
     * @return OrgUnit 组织节点对象
     * @since 9.6.2
     */
    @GetMapping("/getOrgUnitDeletedById")
    Y9Result<OrgUnit> getOrgUnitDeletedById(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("orgUnitId") @NotBlank String orgUnitId);

    /**
     * 根据id获得父对象
     *
     * @param tenantId 租户id
     * @param orgUnitId 组织节点唯一标识
     * @return OrgUnit 组织节点对象（部门或组织机构）
     * @since 9.6.0
     */
    @GetMapping("/getParent")
    Y9Result<OrgUnit> getParent(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("orgUnitId") @NotBlank String orgUnitId);

    /**
     * 获得子节点
     *
     * @param tenantId 租户id
     * @param orgUnitId 组织节点唯一标识
     * @param treeType 树的类型:tree_type_org(组织机构)，tree_type_dept（部门） tree_type_group（用户组）, tree_type_position（岗位）
     *            tree_type_person（人员）, tree_type_bureau（委办局）
     * @return List<OrgUnit> 组织节点对象集合
     * @since 9.6.0
     */
    @GetMapping("/getSubTree")
    Y9Result<List<OrgUnit>> getSubTree(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("orgUnitId") @NotBlank String orgUnitId, @RequestParam("treeType") @NotBlank String treeType);

    /**
     * 根据节点名称，和树类型查询组织节点
     *
     * @param tenantId 租户id
     * @param name 组织节点名称
     * @param treeType 树的类型:tree_type_org(组织机构)，tree_type_dept（部门），tree_type_group（用户组），tree_type_position（岗位）
     *            tree_type_person（人员），tree_type_bureau（委办局）
     * @return List<OrgUnit> 组织节点对象集合
     * @since 9.6.0
     */
    @GetMapping("/treeSearch")
    Y9Result<List<OrgUnit>> treeSearch(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("name") @NotBlank String name, @RequestParam("treeType") @NotBlank String treeType);

    /**
     * 根据name，和结构树类型查询组织节点
     *
     * @param tenantId 租户id
     * @param name 组织节点名称
     * @param dnName 路径名称
     * @param treeType 节点树的类型:tree_type_org(组织机构)，tree_type_dept（部门） tree_type_group（用户组）, tree_type_position（岗位）
     *            tree_type_person（人员）, tree_type_bureau（委办局）
     * @return List<OrgUnit> 组织节点对象集合
     * @since 9.6.0
     */
    @GetMapping("/treeSearchByDn")
    Y9Result<List<OrgUnit>> treeSearchByDn(@RequestParam("tenantId") @NotBlank String tenantId,
        @RequestParam("name") @NotBlank String name, @RequestParam("treeType") @NotBlank String treeType,
        @RequestParam("dnName") @NotBlank String dnName);

}
