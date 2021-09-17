package geardesigner.units;

/**
 * @author SUPERSTATION
 */
public interface ConvertibleUnit extends UnitBase {
    /**
     * 将当前单位的数值，转换为同物理量对应基本单位的数值
     *
     * @param num 将当前单位的数值
     * @return 基本单位的数值
     */
    double toBaseUnit(double num);

    /**
     * 将当前单位的数值，转换为当前单位的数值表示
     *
     * @param base 基本单位的数值
     * @return 将当前单位的数值
     */
    double toCurrentUnit(double base);
}
