package net.risesoft.api.dictionary;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.risesoft.model.OptionValue;

/**
 * 字典表管理组件
 *
 * @author dingzhaojun
 * @author qinman
 * @author mengjuhua
 * @date 2022/2/10
 * @since 9.6.0
 */
public interface OptionValueApi {

    /**
     * 根据类型查找
     *
     * @param tenantId 租户id
     * @param type 类型
     * @return List&lt;OptionValue&gt;
     * @since 9.6.0
     */
    @GetMapping("/listByType")
    List<OptionValue> listByType(@RequestParam("tenantId") String tenantId, @RequestParam("type") String type);
}
