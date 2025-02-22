package net.risesoft.controller.identity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import net.risesoft.controller.identity.vo.RolePermissionVO;
import net.risesoft.entity.identity.position.Y9PositionToRole;
import net.risesoft.enums.platform.ManagerLevelEnum;
import net.risesoft.permission.annotation.IsAnyManager;
import net.risesoft.pojo.Y9Result;
import net.risesoft.service.identity.Y9PositionToRoleService;

/**
 * 权限展示
 *
 * @author dingzhaojun
 * @author qinman
 * @author mengjuhua
 * @date 2022/2/14
 */
@RestController
@RequestMapping(value = "/api/rest/positionRoles", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@IsAnyManager({ManagerLevelEnum.SYSTEM_MANAGER, ManagerLevelEnum.SECURITY_MANAGER})
public class PositionRolesController {

    private final RolePermissionVOBuilder rolePermissionVOBuilder;
    private final Y9PositionToRoleService y9PositionToRoleService;

    /**
     * 根据岗位id，获取岗位角色列表
     * 
     * @param positionId 岗位id
     * @return {@code Y9Result<List<RolePermissionVO>>}
     */
    @GetMapping("/getByPositionId")
    public Y9Result<List<RolePermissionVO>> getByPositionId(@RequestParam @NotBlank String positionId) {
        List<Y9PositionToRole> rolePermissionVOList = y9PositionToRoleService.listByPositionId(positionId);
        return Y9Result
            .success(rolePermissionVOBuilder.buildRolePermissionVOList(new ArrayList<>(rolePermissionVOList)));
    }

}
