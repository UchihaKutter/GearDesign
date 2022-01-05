package geardesigner.beans;

import geardesigner.MathUtils;

/**
 * 计算结果。角全部以弧度制存储
 *
 * @author SuperNote
 */
public record Gear(
        Specifications specs,//设计规格
        double d,//分度圆直径
        double da,//齿顶圆直径
        double df,//齿根圆直径
        double dm,//中圆直径
        double alphaT,//端面压力角
        double db,//基圆直径
        double Zp,//当量齿数
        int k,//跨齿数（基准）
        BaseTangent baseTangent,//公法线
        Span span//跨棒距
) {

    public Gear(Specifications specs) {
        this.specs = specs;
        baseTangent = new BaseTangent();
        span = new Span();
    }

    public Gear calculate() {
        calGear();
        baseTangent.calculate();
        span.calculate();
        return this;
    }

    public Gear calculateDeviation() {
        baseTangent.calculateDeviation();
        span.calculateDeviation();
        return this;
    }

    private void calGear() {
        d = specs.Mn * Math.abs(specs.Z) / Math.cos(specs.beta);
        da = calDa(specs);
        df = calDf(specs);
        alphaT = Math.atan(Math.tan(specs.alphaN) / Math.cos(specs.beta));
        db = d * Math.cos(alphaT);
        Zp = Math.abs(specs.Z) * (Math.tan(alphaT) - alphaT) / (Math.tan(specs.alphaN) - specs.alphaN);
        /**
         * 只允许正数，向下取整
         */
        k = (int) Math.floor(specs.alphaN / Math.PI * Zp + 1);
    }

    private double calDa(Specifications specs) {
        double da = d + 2 * specs.Xn * specs.Mn;
        if (specs.Z > 0) {
            da += 2 * specs.Mn * specs.ha;
        } else {
            da -= 2 * specs.Mn * specs.ha;
        }
        return da;
    }

    private double calDf(Specifications specs) {
        double df = d + 2 * specs.Xn * specs.Mn;
        if (specs.Z > 0) {
            df -= 2 * specs.Mn * (specs.hf + specs.Cf);
        } else {
            df += 2 * specs.Mn * (specs.hf + specs.Cf);
        }
        return df;
    }

    public Double getWk() {
        return baseTangent.Wk;
    }

    public Double getDWk() {
        return baseTangent.dWk;
    }

    public Double getDkm() {
        return span.dkm;
    }

    public Double getX1() {
        return baseTangent.x1;
    }

    public Double getM1() {
        return baseTangent.M1;
    }

    public Double getMs() {
        return baseTangent.Ms;
    }

    public Double getX2() {
        return baseTangent.x2;
    }

    public Double getM2() {
        return baseTangent.M2;
    }

    public Double getMx() {
        return baseTangent.Mx;
    }

    public Double getAlphaM1() {
        return span.alphaM1;
    }

    public Double getWs() {
        return span.Ws;
    }

    public Double getAlphaM2() {
        return span.alphaM2;
    }

    public Double getWx() {
        return span.Wx;
    }

    public Double getM() {
        return span.M;
    }

    /**
     * 计算跨棒距初值
     *
     * @param specs 输入参数
     * @param db    基圆直径
     * @param alpha 渐开线压力角
     * @return
     */
    private double calMi(Specifications specs, double db, double alpha) {
        if ((specs.Z & 1) == 1) {
            return db / Math.cos(alpha) * Math.cos(Math.PI / 2 / Math.abs(specs.Z)) + specs.dp;
        } else {
            return db / Math.cos(alpha) + specs.dp;
        }
    }

    /**
     * 计算跨棒距
     *
     * @param specs
     * @param Mi
     * @return
     */
    private double calM(Specifications specs, double Mi) {
        return (specs.Z > 0) ? Mi : (Mi - 2 * specs.dp);
    }

    /**
     * 公法线
     * 需要先计算公法线基础部分和跨棒距基础部分，才能计算偏差部分
     *
     * @author SuperNote
     */
    private class BaseTangent {
        /**
         * 公法线长度
         */
        double Wk;
        /**
         * 公法线处直径
         */
        double dWk;
        double dWkInvA;
        double alphaM;
        /**
         * 公法线上偏差
         */
        double x1;
        double x1InvA;
        double alphaM1;
        double M1;
        double Ms;
        /**
         * 公法线下偏差
         */
        double x2;
        double x2InvA;
        double alphaM2;
        double M2;
        double Mx;
        private double Mi1;
        private double Mi2;

        /**
         * 计算公法线长度
         *
         * @param specs 输入参数
         * @param k     跨齿数
         * @param Zp    当量齿数
         * @return
         */
        private double calWk(Specifications specs, double k, double Zp) {
            return specs.Mn * Math.cos(specs.alphaN) * ((k - 0.5) * Math.PI + Zp * (Math.tan(specs.alphaN) - specs.alphaN)) + 2 * specs.Xn * specs.Mn * Math.sin(specs.alphaN);
        }

        /**
         * 计算公法线长度处直径
         *
         * @param db   基圆直径
         * @param Wk   公法线长度
         * @param beta 螺旋角
         * @return
         */
        private double calDWk(double db, double Wk, double beta) {
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
        private double calInv(Specifications specs, double deviation, double alpha) {
            return Math.tan(alpha)
                    - alpha
                    + 2 * (specs.Xn + deviation) * Math.tan(specs.alphaN) / Math.abs(specs.Z)
                    + specs.dp / specs.Mn / specs.Z / Math.cos(specs.alphaN)
                    - Math.PI / 2 / specs.Z;
        }

        BaseTangent calculate() {
            Wk = calWk(specs, k, Zp);
            dWk = calDWk(db, Wk, specs.beta);
            dWkInvA = calInv(specs, 0, alphaT);
            alphaM = MathUtils.NewtonCalAlpha(dWkInvA);
            return this;
        }

        /**
         * 计算偏差尺寸x
         *
         * @param specs     输入参数
         * @param deviation 偏差值
         * @return
         */
        private double calX(Specifications specs, double deviation) {
            return deviation / specs.Mn / 2 / Math.sin(specs.alphaN);
        }

        /**
         * 计算跨棒距偏差
         *
         * @param M     当前跨棒距
         * @param Mbase 标准跨棒距
         * @return
         */
        private double calDM(double M, double Mbase) {
            return M - Mbase;
        }

        BaseTangent calculateDeviation() {
            /**
             * 上偏差
             */
            x1 = calX(specs, specs.Ws);
            x1InvA = calInv(specs, x1, alphaT);
            alphaM1 = MathUtils.NewtonCalAlpha(x1InvA);
            Mi1 = calMi(specs, db, alphaM1);
            M1 = calM(specs, Mi1);
            Ms = calDM(M1, span.M);
            /**
             * 下偏差
             */
            x2 = calX(specs, specs.Wx);
            x2InvA = calInv(specs, x2, alphaT);
            alphaM2 = MathUtils.NewtonCalAlpha(x2InvA);
            Mi2 = calMi(specs, db, alphaM2);
            M2 = calM(specs, Mi2);
            Mx = calDM(M2, span.M);
            return this;
        }
    }

    /**
     * 跨棒距
     * 需要先计算公法线基础部分和跨棒距基础部分，才能计算偏差部分
     *
     * @author SuperNote
     */
    private class Span {
        /**
         * 跨棒距测量点直径
         */
        double dkm;
        /**
         * 跨棒距
         */
        double M;
        /**
         * 跨棒距上偏差
         */
        double alphaM1;
        double x1;
        double Ws;
        /**
         * 跨棒距下偏差
         */
        double alphaM2;
        double x2;
        double Wx;
        /**
         * 跨棒距初算
         */
        private double Mi;

        private double calDkm(double db, double alphaM) {
            return db / Math.cos(alphaM);
        }

        Span calculate() {
            dkm = calDkm(db, baseTangent.alphaM);
            Mi = calMi(specs, db, baseTangent.alphaM);
            M = calM(specs, Mi);
            return this;
        }

        /**
         * 上偏差
         */

        /**
         * 计算压力角（从跨棒距）
         *
         * @param specs
         * @param db
         * @param Mi
         */
        private double calAlphaM(Specifications specs, double db, double Mi, double deviation) {
            if ((specs.Z & 1) == 1) {
                return Math.acos(db * Math.cos(Math.PI / 2 / Math.abs(specs.Z)) / (Mi + deviation - specs.dp));//Excel
                // 实际公式与标称公式不符
            } else {
                return Math.acos(db / (Mi + deviation - specs.dp));
            }
        }

        private double calX(Specifications specs, double dWkInvA, double alphaM) {
            return (Math.tan(alphaM) - alphaM - dWkInvA) * Math.abs(specs.Z) / 2 / Math.tan(specs.alphaN);
        }

        /**
         * 计算偏差（从跨棒距）
         *
         * @param Mn     法向模数
         * @param alphaN 法向压力角
         * @param dx     偏差设定
         * @return
         */
        private double calDeviation(double Mn, double alphaN, double dx) {
            return 2 * dx * Mn * Math.sin(alphaN);
        }

        Span calculateDeviation() {
            /**
             * 上偏差
             **/
            alphaM1 = calAlphaM(specs, db, Mi, specs.Ms);
            x1 = calX(specs, baseTangent.dWkInvA, alphaM1);
            Ws = calDeviation(specs.Mn, specs.alphaN, x1);
            /**
             * 下偏差
             */
            alphaM2 = calAlphaM(specs, db, Mi, specs.Mx);
            x2 = calX(specs, baseTangent.dWkInvA, alphaM2);
            Wx = calDeviation(specs.Mn, specs.alphaN, x2);
            return this;
        }

    }

    /**
     * 任一圆齿厚计算，非惰性
     */
    public class AnyCircle {
        /**
         * 任一圆直径
         */
        private double da1;
        /**
         * 齿顶圆端面压力角
         */
        private double alphaT1;
        /**
         * 分度圆弧齿厚
         */
        private double s;
        /**
         * 任一圆弧齿厚
         */
        private double sa1;
        /**
         * 任一圆螺旋角
         */
        private double beta1;
        /**
         * 任一圆处法向弦齿厚
         */
        private double sn1;

        public AnyCircle(Double da) {
            this.da1 = da;
        }

        /**
         * 齿顶圆端面压力角
         *
         * @param db 基圆直径
         * @param da 任一圆直径
         * @return
         */
        private double calAlphaT(double db, double da) {
            return Math.acos(db / da);
        }

        /**
         * 计算分度圆处弧齿厚
         *
         * @param specs 输入参数
         * @return
         */
        private double calS(Specifications specs) {
            return (Math.PI / 2 + 2 * specs.Xn * Math.tan(specs.alphaN)) * specs.Mn / Math.cos(specs.beta);
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
        private double calSa(double d, double alphaT, int z, double alphaT1, double s, double da) {
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
        private double calBeta(double beta0, double alphaT, double alphaT1) {
            return Math.atan(Math.tan(beta0) * Math.cos(alphaT) / Math.cos(alphaT1));
        }

        /**
         * 任一圆处法向弦齿厚
         *
         * @param sa1   任一圆处弧齿厚
         * @param beta1 任一圆螺旋角
         * @return
         */
        private double calSn(double sa1, double beta1) {
            return sa1 * Math.cos(beta1);
        }

        AnyCircle calculate() {
            alphaT1 = calAlphaT(db, da1);
            s = calS(specs);
            //
            sa1 = calSa(d, alphaT, specs.Z, alphaT1, s, da1);
            beta1 = calBeta(specs.beta, alphaT, alphaT1);
            sn1 = calSn(sa1, beta1);
            return this;
        }

        public Double getDa1() {
            return da1;
        }

        public Double getAlphaT1() {
            return alphaT1;
        }

        public Double getS() {
            return s;
        }

        public Double getSa1() {
            return sa1;
        }

        public Double getBeta1() {
            return beta1;
        }

        public Double getSn1() {
            return sn1;
        }
    }
}
