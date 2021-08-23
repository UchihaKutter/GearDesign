package geardesigner.units;

/**
 * 角的数量单位
 *
 * @author SUPERSTATION
 */

public enum Angle implements BaseUnit {
    RADIANS("弧度", "rad", null),
    DEGREES("度", "°", null);

    private final String name;
    private final String display;
    private final ValueRange range;

    private Angle(String name, String display, ValueRange range) {
        this.name = name;
        this.display = display;
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
}
