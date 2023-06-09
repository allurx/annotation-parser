package red.zyc.parser.handler;

import java.lang.reflect.AnnotatedElement;

/**
 * 注解出现在目标对象上的位置
 *
 * @author zyc
 * @see AnnotatedElement
 */
public enum Location {

    /**
     * 直接存在
     */
    DIRECTLY_PRESENT,

    /**
     * 间接存在
     */
    INDIRECTLY_PRESENT,

    /**
     * 存在
     */
    PRESENT,

    /**
     * 关联的
     */
    ASSOCIATED

}
