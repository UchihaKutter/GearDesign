package geardesigner.units;

/**
 * 角的数量单位
 *
 * @author SUPERSTATION
 */

public enum Angle implements ConvertibleUnit {
    /**
     * 定义设计中常用的角的大小单位度、弧度。以弧度作为基本单位
     */
    RADIANS("弧度", "rad", 1, null),
    DEGREES("度", "°", Math.PI / 180, null);

    private final String name;
    private final String display;
    private final double baseRadian;
    private final ValueRange range;

    private Angle(final String name, final String display, final double baseRadian, ValueRange range) {
        this.name = name;
        this.display = display;
        this.baseRadian = baseRadian;
        this.range = range;
    }

    /**
     * 获取量纲单位的中文名称
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 获取量纲单位的latex表示
     *
     * @return
     */
    @Override
    public String getDisplay() {
        return display;
    }

    /**
     * 将当前单位的数值，转换为同物理量对应基本单位的数值
     *
     * @param num 将当前单位的数值
     * @return 基本单位的数值
     */
    @Override
    public double toBaseUnit(final double num) {
        return num*baseRadian;
    }

    /**
     * 将当前单位的数值，转换为当前单位的数值表示
     *
     * @param base 基本单位的数值
     * @return 将当前单位的数值
     */
    @Override
    public double toCurrentUnit(final double base) {
        return base/baseRadian;
    }
}
