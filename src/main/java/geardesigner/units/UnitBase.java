package geardesigner.units;

/**
 * 基本的单位接口
 *
 * @author SUPERSTATION
 */
public interface UnitBase {
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
