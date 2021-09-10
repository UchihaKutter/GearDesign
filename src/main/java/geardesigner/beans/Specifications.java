package geardesigner.beans;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.Map;

/**
 * 存储计算参数。
 * 角全部以弧度制存储
 *
 * @author SuperNote
 */
public strictfp class Specifications implements Serializable {
    private static final long serialVersionUID = 5572715615929878142L;
    /**
     * 法向模数
     */
    public final int Mn;
    /**
     * 齿数（内齿为负）
     */
    public final int Z;
    /**
     * 法向压力角
     */
    public final double alphaN;
    /**
     * 螺旋角
     */
    public final double beta;
    /**
     * 法向变位系数
     */
    public final double Xn;
    /**
     * 齿顶高系数
     */
    public final double ha;
    /**
     * 齿根高系数
     */
    public final double hf;
    /**
     * 顶隙系数
     */
    public final double Cf;
    /**
     * 量棒直径
     */
    public final double dp;
    /**
     * 公法线上偏差
     */
    public final double Ws;
    /**
     * 公法线下偏差
     */
    public final double Wx;
    /**
     * 跨棒距上偏差
     */
    public final double Ms;
    /**
     * 跨棒距下偏差
     */
    public final double Mx;

    @ConstructorProperties({"Mn", "Z", "alphaN", "beta", "Xn", "ha", "hf", "Cf", "dp", "Ws", "Wx", "Ms", "Mx"})
    public Specifications(final int mn,
                          final int z,
                          final double alphaN,
                          final double beta,
                          final double xn,
                          final double ha,
                          final double hf,
                          final double cf,
                          final double dp,
                          final double ws,
                          final double wx,
                          final double ms,
                          final double mx) {
        Mn = mn;
        Z = z;
        this.alphaN = alphaN;
        this.beta = beta;
        Xn = xn;
        this.ha = ha;
        this.hf = hf;
        Cf = cf;
        this.dp = dp;
        Ws = ws;
        Wx = wx;
        Ms = ms;
        Mx = mx;
    }


    public Specifications(@NotNull Map<String, Number> specs) throws InputException {
        try {
            Mn = specs.get("法向模数").intValue();
            Z = specs.get("齿数(内齿为负)").intValue();
            alphaN = specs.get("法向压力角").doubleValue();
            beta = specs.get("螺旋角").doubleValue();
            Xn = specs.get("法向变位系数").doubleValue();
            ha = specs.get("齿顶高系数").doubleValue();
            hf = specs.get("齿根高系数").doubleValue();
            Cf = specs.get("顶隙系数").doubleValue();
            dp = specs.get("量棒直径").doubleValue();
            Ws = specs.get("公法线上偏差").doubleValue();
            Wx = specs.get("公法线下偏差").doubleValue();
            Ms = specs.get("跨棒距上偏差").doubleValue();
            Mx = specs.get("跨棒距下偏差").doubleValue();
        } catch (NullPointerException e) {
            throw new InputException("计算参数输入不完整");
        }
        if (!isValid(this)) {
            throw new InputException("计算参数不合法");
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

    @Override
    public String toString() {
        return "Specifications{" +
                "Mn=" + Mn +
                ", Z=" + Z +
                ", alphaN=" + alphaN +
                ", beta=" + beta +
                ", Xn=" + Xn +
                ", ha=" + ha +
                ", hf=" + hf +
                ", Cf=" + Cf +
                ", dp=" + dp +
                ", Ws=" + Ws +
                ", Wx=" + Wx +
                ", Ms=" + Ms +
                ", Mx=" + Mx +
                '}';
    }
}
