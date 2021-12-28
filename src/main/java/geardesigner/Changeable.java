package geardesigner;

import java.util.function.Consumer;

/**
 * @author SUPERTOP
 */
public interface Changeable {

    /**
     * 设置发生变化时的处理器
     *
     * @param consumer
     */
    void setOnChanged(Consumer<Object> consumer);
}
