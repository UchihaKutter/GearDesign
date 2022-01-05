package geardesigner.beans;

import geardesigner.MathUtils;

/**
 * 计算结果。角全部以弧度制存储
 *
 * @author SuperNote
 */
public class Gear {
    /**
     * 设计规格
     */
    private final Specifications specs;
    /**
     * 公法线
     */
    BaseTangent baseTangent;
    /**
     * 公法线偏差
     */
    BaseTangent.Deviation btDeviation;
    /**
     * 跨棒距
     */
    Span span;
    /**
     * 跨棒距偏差
     */
    Span.Deviation sDeviation;
    /**
     * 分度圆直径
     */
    private double d;
    /**
     * 齿顶圆直径
     */
    private double da;
    /**
     * 齿根圆直径
     */
    private double df;
    /**
     * 中圆直径
     */
    private double dm;
    /**
     * 端面压力角
     */
    private double alphaT;
    /**
     * 基圆直径
     */
    private double db;
    /**
     * 当量齿数
     */
    private double Zp;
    /**
     * 跨齿数（基准）
     */
    private int k;


    public Gear(Specifications specs) {
        this.specs = specs;
    }

    /**
     * 计算跨棒距初值
     *
     * @param specs 输入参数
     * @param db    基圆直径
     * @param alpha 渐开线压力角
     * @return
     */
    private static double calMi(Specifications specs, double db, double alpha) {
        if ((specs.Z() & 1) == 1) {
            return db / Math.cos(alpha) * Math.cos(Math.PI / 2 / Math.abs(specs.Z())) + specs.dp();
        } else {
            return db / Math.cos(alpha) + specs.dp();
        }
    }

    /**
     * 计算跨棒距
     *
     * @param specs
     * @param Mi
     * @return
     */
    private static double calM(Specifications specs, double Mi) {
        return (specs.Z() > 0) ? Mi : (Mi - 2 * specs.dp());
    }

    public void calculate() {
        calcGear();
        baseTangent = BaseTangent.calculate(specs, k, Zp, db, alphaT);
        span = Span.calculate(specs, baseTangent, db);
    }

    public void calculateDeviation() {
        btDeviation = BaseTangent.Deviation.calculate(specs, span, db, alphaT);
        sDeviation = Span.Deviation.calculate(specs, span, baseTangent, db);
    }

    private void calcGear() {
        d = specs.Mn() * Math.abs(specs.Z()) / Math.cos(specs.beta());
        da = calDa(specs);
        df = calDf(specs);
        alphaT = Math.atan(Math.tan(specs.alphaN()) / Math.cos(specs.beta()));
        db = d * Math.cos(alphaT);
        Zp = Math.abs(specs.Z()) * (Math.tan(alphaT) - alphaT) / (Math.tan(specs.alphaN()) - specs.alphaN());
        /**
         * 只允许正数，向下取整
         */
        k = (int) Math.floor(specs.alphaN() / Math.PI * Zp + 1);
    }

    private double calDa(Specifications specs) {
        double da = d + 2 * specs.Xn() * specs.Mn();
        if (specs.Z() > 0) {
            da += 2 * specs.Mn() * specs.ha();
        } else {
            da -= 2 * specs.Mn() * specs.ha();
        }
        return da;
    }

    private double calDf(Specifications specs) {
        double df = d + 2 * specs.Xn() * specs.Mn();
        if (specs.Z() > 0) {
            df -= 2 * specs.Mn() * (specs.hf() + specs.Cf());
        } else {
            df += 2 * specs.Mn() * (specs.hf() + specs.Cf());
        }
        return df;
    }

    public double d() {
        return d;
    }

    public double da() {
        return da;
    }

    public double df() {
        return df;
    }

    public double dm() {
        return dm;
    }

    public double alphaT() {
        return alphaT;
    }

    public double db() {
        return db;
    }

    public double zp() {
        return Zp;
    }

    public int k() {
        return k;
    }

    public BaseTangent baseTangent() {
        return baseTangent;
    }

    public BaseTangent.Deviation btDeviation() {
        return btDeviation;
    }

    public Span span() {
        return span;
    }

    public Span.Deviation sDeviation() {
        return sDeviation;
    }

    public AnyCircle calcAnyCircle(final double da1) {
        final double alphaT1 = AnyCircle.calAlphaT(db, da1);
        final double s = AnyCircle.calS(specs);
        final double sa1 = AnyCircle.calSa(d, alphaT, specs.Z(), alphaT1, s, da1);
        final double beta1 = AnyCircle.calBeta(specs.beta(), alphaT, alphaT1);
        final double sn1 = AnyCircle.calSn(sa1, beta1);
        return new AnyCircle(da1, alphaT1, s, sa1, beta1, sn1);
    }

    /**
     * 公法线
     * 需要先计算公法线基础部分和跨棒距基础部分，才能计算偏差部分
     *
     * @author SuperNote
     */
    public record BaseTangent(
            double Wk,//公法线长度
            double dWk,//公法线处直径
            double dWkInvA,//
            double alphaM
    ) {
        /**
         * 计算公法线长度
         *
         * @param specs 输入参数
         * @param k     跨齿数
         * @param Zp    当量齿数
         * @return
         */
        private static double calWk(Specifications specs, double k, double Zp) {
            return specs.Mn() * Math.cos(specs.alphaN()) * ((k - 0.5) * Math.PI + Zp * (Math.tan(specs.alphaN()) - specs.alphaN())) + 2 * specs.Xn() * specs.Mn() * Math.sin(specs.alphaN());
        }

        /**
         * 计算公法线长度处直径
         *
         * @param db   基圆直径
         * @param Wk   公法线长度
         * @param beta 螺旋角
         * @return
         */
        private static double calDWk(double db, double Wk, double beta) {
            return Math.sqrt(db * db + Math.pow(Wk * Math.cos(beta), 2));
        }

        /**
         * 计算极角值（弧度）
         *
         * @param specs
         * @param deviation
         * @param alpha
         * @return
         */
        static double calInv(Specifications specs, double deviation, double alpha) {
            return Math.tan(alpha)
                    - alpha
                    + 2 * (specs.Xn() + deviation) * Math.tan(specs.alphaN()) / Math.abs(specs.Z())
                    + specs.dp() / specs.Mn() / specs.Z() / Math.cos(specs.alphaN())
                    - Math.PI / 2 / specs.Z();
        }

        static BaseTangent calculate(Specifications specs,
                                     final int k, final double Zp,
                                     final double db, final double alphaT) {
            final double Wk = calWk(specs, k, Zp);
            final double dWk = calDWk(db, Wk, specs.beta());
            final double dWkInvA = calInv(specs, 0, alphaT);
            final double alphaM = MathUtils.NewtonCalAlpha(dWkInvA);
            return new BaseTangent(Wk, dWk, dWkInvA, alphaM);
        }

        public record Deviation(
                //公法线上偏差
                double x1,
                double x1InvA,
                double alphaM1,
                double M1,
                double Ms,
                double Mi1,
                //公法线下偏差
                double x2,
                double x2InvA,
                double alphaM2,
                double M2,
                double Mx,
                double Mi2
        ) {

            /**
             * 计算偏差尺寸x
             *
             * @param specs     输入参数
             * @param deviation 偏差值
             * @return
             */
            private static double calX(Specifications specs, double deviation) {
                return deviation / specs.Mn() / 2 / Math.sin(specs.alphaN());
            }

            /**
             * 计算跨棒距偏差
             *
             * @param M     当前跨棒距
             * @param Mbase 标准跨棒距
             * @return
             */
            private static double calDM(double M, double Mbase) {
                return M - Mbase;
            }

            static Deviation calculate(Specifications specs, Span span, final double db, final double alphaT) {
                /**
                 * 上偏差
                 */
                final double x1 = calX(specs, specs.Ws());
                final double x1InvA = calInv(specs, x1, alphaT);
                final double alphaM1 = MathUtils.NewtonCalAlpha(x1InvA);
                final double Mi1 = calMi(specs, db, alphaM1);
                final double M1 = calM(specs, Mi1);
                final double Ms = calDM(M1, span.M);
                /**
                 * 下偏差
                 */
                final double x2 = calX(specs, specs.Wx());
                final double x2InvA = calInv(specs, x2, alphaT);
                final double alphaM2 = MathUtils.NewtonCalAlpha(x2InvA);
                final double Mi2 = calMi(specs, db, alphaM2);
                final double M2 = calM(specs, Mi2);
                final double Mx = calDM(M2, span.M);
                return new Deviation(x1, x1InvA, alphaM1, M1, Ms, Mi1, x2, x2InvA, alphaM2, M2, Mx, Mi2);
            }
        }
    }

    /**
     * 跨棒距
     * 需要先计算公法线基础部分和跨棒距基础部分，才能计算偏差部分
     *
     * @author SuperNote
     */
    public record Span
            (
                    double dkm,//跨棒距测量点直径
                    double M,//跨棒距
                    /**
                     * 跨棒距初算
                     */
                    double Mi
            ) {
        private static double calDkm(double db, double alphaM) {
            return db / Math.cos(alphaM);
        }

        static Span calculate(Specifications specs, BaseTangent baseTangent, final double db) {
            final double dkm = calDkm(db, baseTangent.alphaM);
            final double Mi = calMi(specs, db, baseTangent.alphaM);
            final double M = calM(specs, Mi);
            return new Span(dkm, M, Mi);
        }

        public record Deviation(
                /**
                 * 跨棒距上偏差
                 */
                double alphaM1,
                double x1,
                double Ws,
                /**
                 * 跨棒距下偏差
                 */
                double alphaM2,
                double x2,
                double Wx
        ) {
            /**
             * 计算压力角（从跨棒距）
             *
             * @param specs
             * @param db
             * @param Mi
             */
            private static double calAlphaM(Specifications specs, double db, double Mi, double deviation) {
                if ((specs.Z() & 1) == 1) {
                    return Math.acos(db * Math.cos(Math.PI / 2 / Math.abs(specs.Z())) / (Mi + deviation - specs.dp()));//Excel
                    // 实际公式与标称公式不符
                } else {
                    return Math.acos(db / (Mi + deviation - specs.dp()));
                }
            }

            private static double calX(Specifications specs, double dWkInvA, double alphaM) {
                return (Math.tan(alphaM) - alphaM - dWkInvA) * Math.abs(specs.Z()) / 2 / Math.tan(specs.alphaN());
            }

            /**
             * 计算偏差（从跨棒距）
             *
             * @param Mn     法向模数
             * @param alphaN 法向压力角
             * @param dx     偏差设定
             * @return
             */
            private static double calDeviation(double Mn, double alphaN, double dx) {
                return 2 * dx * Mn * Math.sin(alphaN);
            }

            static Deviation calculate(Specifications specs, Span span, BaseTangent baseTangent, final double db) {
                /**
                 * 上偏差
                 **/
                final double alphaM1 = calAlphaM(specs, db, span.Mi, specs.Ms());
                final double x1 = calX(specs, baseTangent.dWkInvA, alphaM1);
                final double Ws = calDeviation(specs.Mn(), specs.alphaN(), x1);
                /**
                 * 下偏差
                 */
                final double alphaM2 = calAlphaM(specs, db, span.Mi, specs.Mx());
                final double x2 = calX(specs, baseTangent.dWkInvA, alphaM2);
                final double Wx = calDeviation(specs.Mn(), specs.alphaN(), x2);
                return new Deviation(alphaM1, x1, Ws, alphaM2, x2, Wx);
            }
        }
    }

    /**
     * 任一圆齿厚计算，非惰性
     */
    public record AnyCircle(
            double da1,//任一圆直径
            double alphaT1,//分度圆弧齿厚
            double s,//分度圆弧齿厚
            double sa1,//任一圆弧齿厚
            double beta1,//任一圆螺旋角
            double sn1//任一圆处法向弦齿厚
    ) {
        /**
         * 齿顶圆端面压力角
         *
         * @param db 基圆直径
         * @param da 任一圆直径
         * @return
         */
        private static double calAlphaT(double db, double da) {
            return Math.acos(db / da);
        }

        /**
         * 计算分度圆处弧齿厚
         *
         * @param specs 输入参数
         * @return
         */
        private static double calS(Specifications specs) {
            return (Math.PI / 2 + 2 * specs.Xn() * Math.tan(specs.alphaN())) * specs.Mn() / Math.cos(specs.beta());
        }

        /**
         * 计算任一圆处弧齿厚
         *
         * @param d       分度圆直径
         * @param alphaT  端面压力角
         * @param z       齿数
         * @param alphaT1 齿顶圆端面压力角
         * @param s       分度圆处弧齿厚
         * @param da      任一圆直径
         * @return
         */
        private static double calSa(double d, double alphaT, int z, double alphaT1, double s, double da) {
            double sa = s * da / d;
            double part = da * (Math.tan(alphaT1) - alphaT1 - Math.tan(alphaT) + alphaT);
            if (z > 0) {
                sa -= part;
            } else {
                sa += part;
            }
            return sa;
        }

        /**
         * 任一圆螺旋角
         *
         * @param beta0   螺旋角
         * @param alphaT  端面压力角
         * @param alphaT1 齿顶圆端面压力角
         * @return
         */
        private static double calBeta(double beta0, double alphaT, double alphaT1) {
            return Math.atan(Math.tan(beta0) * Math.cos(alphaT) / Math.cos(alphaT1));
        }

        /**
         * 任一圆处法向弦齿厚
         *
         * @param sa1   任一圆处弧齿厚
         * @param beta1 任一圆螺旋角
         * @return
         */
        private static double calSn(double sa1, double beta1) {
            return sa1 * Math.cos(beta1);
        }
    }
}
