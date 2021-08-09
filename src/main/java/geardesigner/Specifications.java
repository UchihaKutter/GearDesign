package geardesigner;

import geardesigner.beans.Decimal;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * 存储计算参数。
 * 角全部以弧度制存储
 *
 * @author SuperNote
 */
public strictfp class Specifications {
    /**
     * 法向模数
     */
    final int Mn;
    /**
     * 齿数（内齿为负）
     */
    final int Z;
    /**
     * 法向压力角
     */
    final double alphaN;
    /**
     * 螺旋角
     */
    final double beta;
    /**
     * 法向变位系数
     */
    final double Xn;
    /**
     * 齿顶高系数
     */
    final double ha;
    /**
     * 齿根高系数
     */
    final double hf;
    /**
     * 顶隙系数
     */
    final double Cf;
    /**
     * 量棒直径
     */
    final double dp;
    /**
     * 公法线上偏差
     */
    final double Ws;
    /**
     * 公法线下偏差
     */
    final double Wx;
    /**
     * 跨棒距上偏差
     */
    final double Ms;
    /**
     * 跨棒距下偏差
     */
    final double Mx;

    /**
     * 检查输入参数值是否是合法
     * @param specs
     * @return
     */
    @Contract(value = "null->false",pure = true)
    public static boolean isValid(Specifications specs) {
        return true;//// TODO: 2020/2/9 检查参数设置是否合法
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
        }catch (NullPointerException e){
            throw new InputException("计算参数输入不完整");
        }
        if (!isValid(this)){
            throw new InputException("计算参数不合法");
        }
    }
}
