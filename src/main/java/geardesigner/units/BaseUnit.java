package geardesigner.units;

/**
 * @author SUPERSTATION
 */
public interface BaseUnit {
    /**
     * 获取量纲单位的中文名称
     *
     * @return
     */
    String getName();

    /**
     * 获取量纲单位的latex表示
     *
     * @return
     */
    String getDisplay();
}
