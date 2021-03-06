package geardesigner.units;

/**
 * 长度单位
 *
 * @author SUPERSTATION
 */

public enum Length implements ConvertibleUnit {
    /**
     * 定义设计中常用的长度单位千米、米、分米、厘米、毫米、微米。以毫米作为基本单位
     */
    KILOMETERS("千米", "km", 1000_000_000),
    METERS("米", "m", 1000_000),
    DECIMETERS("分米", "dm", 100_000),
    CENTIMETERS("厘米", "cm", 10_000),
    MILLIMETERS("毫米", "mm", 1000),
    MICROMETERS("微米", "μm", 1);

    private final String name;
    private final String display;
    private final long baseMicro;

    Length(final String name, final String display, final long baseMicro) {
        this.name = name;
        this.display = display;
        this.baseMicro = baseMicro;
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

    public boolean isValid(double num) {
        return num >= 0;
    }

    public boolean isValid(long num) {
        return num >= 0;
    }

    /**
     * 将当前单位的数值，转换为同物理量对应基本单位的数值
     *
     * @param num 将当前单位的数值
     * @return 基本单位的数值
     */
    @Override
    public double toBaseUnit(final double num) {
        return num / 1000 * baseMicro;
    }

    /**
     * 将当前单位的数值，转换为当前单位的数值表示
     *
     * @param base 基本单位的数值
     * @return 将当前单位的数值
     */
    @Override
    public double toCurrentUnit(final double base) {
        return base * 1000 / baseMicro;
    }
}
