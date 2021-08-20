package geardesigner.beans;

import geardesigner.InputException;
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


    public Specifications(@NotNull Map<String, Decimal> specs) throws InputException {
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

    public @NotNull Map<String, Decimal> toValueMap() {
        return Map.ofEntries(
                Map.entry("法向模数", Decimal.valueOf(Mn)),
                Map.entry("齿数(内齿为负)", Decimal.valueOf(Z)),
                Map.entry("法向压力角", Decimal.valueOf(alphaN)),
                Map.entry("螺旋角", Decimal.valueOf(beta)),
                Map.entry("法向变位系数", Decimal.valueOf(Xn)),
                Map.entry("齿顶高系数", Decimal.valueOf(ha)),
                Map.entry("齿根高系数", Decimal.valueOf(hf)),
                Map.entry("顶隙系数", Decimal.valueOf(Cf)),
                Map.entry("量棒直径", Decimal.valueOf(dp)),
                Map.entry("公法线上偏差", Decimal.valueOf(Ws)),
                Map.entry("公法线下偏差", Decimal.valueOf(Wx)),
                Map.entry("跨棒距上偏差", Decimal.valueOf(Ms)),
                Map.entry("跨棒距下偏差", Decimal.valueOf(Mx)));
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
