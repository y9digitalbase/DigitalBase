package net.risesoft.controller.org;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import net.risesoft.entity.Y9Position;
import net.risesoft.entity.relation.Y9PersonsToPositions;
import net.risesoft.log.OperationTypeEnum;
import net.risesoft.log.annotation.RiseLog;
import net.risesoft.pojo.Y9Result;
import net.risesoft.service.org.Y9PositionService;
import net.risesoft.service.relation.Y9PersonsToPositionsService;

/**
 * 岗位管理
 *
 * @author dingzhaojun
 * @author qinman
 * @author mengjuhua
 * @date 2022/2/14
 */
@Validated
@RestController
@RequestMapping(value = "/api/rest/position", produces = "application/json")
@RequiredArgsConstructor
public class PositionController {

    private final Y9PersonsToPositionsService y9PersonsToPositionsService;
    private final Y9PositionService y9PositionService;

    /**
     * 为岗位添加人员
     *
     * @param positionId 岗位id
     * @param personIds 人员id数组
     * @return
     */
    @RiseLog(operationName = "为岗位添加人员", operationType = OperationTypeEnum.ADD)
    @PostMapping(value = "/addPersons")
    public Y9Result<List<Y9PersonsToPositions>> addPersons(@RequestParam String positionId,
        @RequestParam String[] personIds) {
        List<Y9PersonsToPositions> orgPersonList = y9PersonsToPositionsService.addPersons(positionId, personIds);
        return Y9Result.success(orgPersonList, "为岗位添加人员成功");
    }

    /**
     * 根据岗位id，获取岗位的扩展属性
     *
     * @param positionId 岗位id
     * @return
     */
    @RiseLog(operationName = "获取扩展属性")
    @RequestMapping(value = "/getExtendProperties")
    public Y9Result<String> getExtendProperties(@RequestParam String positionId) {
        String properties = y9PositionService.getById(positionId).getProperties();
        return Y9Result.success(properties, "获取扩展属性成功");
    }

    /**
     * 根据岗位id，获取岗位信息
     *
     * @param positionId 岗位id
     * @return
     */
    @RiseLog(operationName = "根据岗位id，获取岗位信息信息")
    @RequestMapping(value = "/getPositionById")
    public Y9Result<Y9Position> getPositionById(@RequestParam String positionId) {
        return Y9Result.success(y9PositionService.getById(positionId), "根据岗位id，获取岗位信息成功");
    }

    /**
     * 根据父节点id，获取岗位列表
     *
     * @param parentId 父节点id
     * @return
     * @since 9.6.1
     */
    @RiseLog(operationName = "根据父节点id，获取岗位列表")
    @RequestMapping(value = "/listPositionsByParentId")
    public Y9Result<List<Y9Position>> listPositionsByParentId(@RequestParam String parentId) {
        return Y9Result.success(y9PositionService.listByParentId(parentId), "根据父节点id，获取岗位列表成功");
    }

    /**
     * 根据人员id，获取岗位列表
     *
     * @param personId 人员id
     * @return
     * @since 9.6.1
     */
    @RiseLog(operationName = "根据人员id，获取岗位列表")
    @RequestMapping(value = "/listPositionsByPersonId")
    public Y9Result<List<Y9Position>> listPositionsByPersonId(@RequestParam String personId) {
        return Y9Result.success(y9PositionService.listByPersonId(personId), "根据人员id，获取岗位列表成功");
    }

    /**
     * 移动岗位到新的节点
     *
     * @param positionId 岗位id
     * @param parentId 目标父节点id
     * @return
     */
    @RiseLog(operationName = "移动岗位", operationType = OperationTypeEnum.MODIFY)
    @PostMapping(value = "/move")
    public Y9Result<Y9Position> move(@RequestParam String positionId, @RequestParam String parentId) {
        Y9Position y9Position = y9PositionService.move(positionId, parentId);
        return Y9Result.success(y9Position, "移动岗位成功");
    }

    /**
     * 删除岗位
     *
     * @param ids 岗位id数组
     * @return
     */
    @RiseLog(operationName = "删除岗位", operationType = OperationTypeEnum.DELETE)
    @PostMapping(value = "/remove")
    public Y9Result<String> remove(@RequestParam String[] ids) {
        y9PositionService.delete(ids);
        return Y9Result.successMsg("删除岗位成功");
    }

    /**
     * 从岗位移除人员
     *
     * @param positionId 岗位id
     * @param personIds 人员id数组
     * @return
     */
    @RiseLog(operationName = "移除岗位的人员", operationType = OperationTypeEnum.DELETE)
    @PostMapping(value = "/removePersons")
    public Y9Result<String> removePersons(@RequestParam String positionId, @RequestParam String[] personIds) {
        y9PersonsToPositionsService.deletePersons(positionId, personIds);
        return Y9Result.successMsg("移除岗位的人员成功");
    }

    /**
     * 保存扩展属性(直接覆盖)
     *
     * @param positionId 岗位id
     * @param properties 扩展属性
     * @return
     */
    @RiseLog(operationName = "保存扩展属性", operationType = OperationTypeEnum.ADD)
    @PostMapping(value = "/saveExtendProperties")
    public Y9Result<String> saveExtendProperties(@RequestParam String positionId, @RequestParam String properties) {
        Y9Position y9Position = y9PositionService.saveProperties(positionId, properties);
        return Y9Result.success(y9Position.getProperties(), "保存扩展属性成成功");
    }

    /**
     * 保存新的序号
     *
     * @param positionIds 岗位ids
     * @param tabindexs 排序ids
     */
    @RiseLog(operationName = "保存岗位排序", operationType = OperationTypeEnum.MODIFY)
    @PostMapping(value = "/saveOrder")
    public Y9Result<String> saveOrder(@RequestParam String[] positionIds, @RequestParam String[] tabindexs) {
        y9PositionService.saveOrder(positionIds, tabindexs);
        return Y9Result.successMsg("保存岗位排序成功");
    }

    /**
     * 保存岗位的人员排序
     *
     * @param positionId 岗位id
     * @param personIds 人员id数组
     * @return
     */
    @RiseLog(operationName = "保存岗位的人员排序", operationType = OperationTypeEnum.MODIFY)
    @PostMapping(value = "/orderPersons")
    public Y9Result<String> saveOrderPersons(@RequestParam String positionId, @RequestParam String[] personIds) {
        y9PersonsToPositionsService.orderPersons(positionId, personIds);
        return Y9Result.successMsg("保存岗位的人员排序成功");
    }

    /**
     * 保存人员的岗位排序
     *
     * @param personId 人员id
     * @param positionIds 岗位id数组
     * @return
     */
    @RiseLog(operationName = "保存人员的岗位排序", operationType = OperationTypeEnum.MODIFY)
    @PostMapping(value = "/orderPositions")
    public Y9Result<String> saveOrderPositions(@RequestParam String personId, @RequestParam String[] positionIds) {
        y9PersonsToPositionsService.orderPositions(personId, positionIds);
        return Y9Result.successMsg("保存人员的岗位排序成功");
    }

    /**
     * 新建或者更新岗位信息
     *
     * @param position 岗位实体
     * @param parentId 父节点id
     * @return
     */
    @RiseLog(operationName = "新建或者更新岗位信息", operationType = OperationTypeEnum.ADD)
    @PostMapping(value = "/saveOrUpdate")
    public Y9Result<Y9Position> saveOrUpdate(@Validated Y9Position position, @NotBlank @RequestParam String parentId) {
        Y9Position returnPosition = y9PositionService.saveOrUpdate(position, parentId);
        return Y9Result.success(returnPosition, "保存岗位信息成功");
    }
}
