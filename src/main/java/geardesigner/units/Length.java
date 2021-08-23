package geardesigner.units;

/**
 * 长度单位
 *
 * @author SUPERSTATION
 */

public enum Length implements BaseUnit {
    /**
     * 定义设计中常用的长度单位千米、米、分米、厘米、毫米、微米
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
}
