package geardesigner;

import geardesigner.beans.Decimal;

/**
 * @author SUPERSTATION
 */
public final class MathUtils {
    private MathUtils(){}
    public static final Decimal toDegrees(Decimal rad){
        return Decimal.valueOf(Math.toDegrees(rad.doubleValue()));
    }

    /**
     * 牛顿法求解渐开线压力角，默认参数角取值范围(0,PI)，已测试
     *
     * @param theta 极角（弧度制）
     * @return 压力角（弧度制）
     */
    public static double NewtonCalAlpha(double theta) {
        /**
         * 迭代初始值
         */
        double alpha0 = 0.0001;
        double alpha = 1;
        /**
         * 求解精度设置
         */
        double precision = 0.000001;
        while (Math.abs(alpha - alpha0) > precision) {
            alpha0 = alpha;
            alpha = alpha0 - (Math.tan(alpha0) - alpha0 - theta) / (Math.tan(alpha0) * Math.tan(alpha0));
        }
        return alpha;
    }
}
