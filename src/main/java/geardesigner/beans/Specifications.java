package geardesigner.beans;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;

/**
 * 存储计算参数。
 * 角全部以弧度制存储
 *
 * @author SuperNote
 */
public record Specifications
        (
                int Mn,//法向模数
                int Z,//齿数
                double alphaN,//法向压力角
                double beta,//螺旋角
                double Xn,//法向变位系数
                double ha,//齿顶高系数
                double hf,//齿根高系数
                double Cf,//顶隙系数
                double dp,//量棒直径
                double Ws,//公法线上偏差
                double Wx,//公法线下偏差
                double Ms,//跨棒距上偏差
                double Mx//跨棒距下偏差
        ) implements Serializable {

    public static Specifications toSpecifications(@NotNull Map<String, Number> specs) throws InputException {
        try {
            final int Mn = specs.get("法向模数").intValue();
            final int Z = specs.get("齿数(内齿为负)").intValue();
            final double alphaN = specs.get("法向压力角").doubleValue();
            final double beta = specs.get("螺旋角").doubleValue();
            final double Xn = specs.get("法向变位系数").doubleValue();
            final double ha = specs.get("齿顶高系数").doubleValue();
            final double hf = specs.get("齿根高系数").doubleValue();
            final double Cf = specs.get("顶隙系数").doubleValue();
            final double dp = specs.get("量棒直径").doubleValue();
            final double Ws = specs.get("公法线上偏差").doubleValue();
            final double Wx = specs.get("公法线下偏差").doubleValue();
            final double Ms = specs.get("跨棒距上偏差").doubleValue();
            final double Mx = specs.get("跨棒距下偏差").doubleValue();
            final Specifications ss = new Specifications(Mn, Z, alphaN, beta, Xn, ha, hf, Cf, dp, Ws, Wx, Ms, Mx);
            if (!isValid(ss)) {
                throw new InputException("计算参数不合法");
            }
            return ss;
        } catch (NullPointerException e) {
            throw new InputException("计算参数输入不完整");
        }
    }

    /**
     * 检查输入参数值是否是合法
     *
     * @param specs
     * @return
     */
    @Contract(value = "null->false", pure = true)
    public static boolean isValid(Specifications specs) {
        return true;//// TODO: 2020/2/9 检查参数设置是否合法
    }

    public @NotNull Map<String, Number> toValueMap() {
        return Map.ofEntries(
                Map.entry("法向模数", Mn),
                Map.entry("齿数(内齿为负)", Z),
                Map.entry("法向压力角", alphaN),
                Map.entry("螺旋角", beta),
                Map.entry("法向变位系数", Xn),
                Map.entry("齿顶高系数", ha),
                Map.entry("齿根高系数", hf),
                Map.entry("顶隙系数", Cf),
                Map.entry("量棒直径", dp),
                Map.entry("公法线上偏差", Ws),
                Map.entry("公法线下偏差", Wx),
                Map.entry("跨棒距上偏差", Ms),
                Map.entry("跨棒距下偏差", Mx));
    }
}
