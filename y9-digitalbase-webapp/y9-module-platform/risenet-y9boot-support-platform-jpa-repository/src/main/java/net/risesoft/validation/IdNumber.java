package net.risesoft.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 校验证件号是否合法
 *
 * @author shidaobang
 * @date 2022/08/04
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = IdNumberValidator.class)
public @interface IdNumber {

    // 分组
    Class<?>[] groups() default {};

    // 默认校验错误提示
    String message() default "证件号不合法";

    // 负载
    Class<? extends Payload>[] payload() default {};
}
