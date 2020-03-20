package geardesigner;

/**
 * @author SuperNote
 */
public strictfp class Gear {
    /**
     * 设计规格
     */
    Specifications specs;
    /**
     * 分度圆直径
     */
    double d;
    /**
     * 齿顶圆直径
     */
    double da;
    /**
     * 齿根圆直径
     */
    double df;
    /**
     * 中圆直径
     */
    double dm;
    /**
     * 断面压力角
     */
    double alphaT;
    /**
     * 基圆直径
     */
    double db;
    /**
     * 当量齿数
     */
    double Zp;
    /**
     * 跨齿数（基准）
     */
    int k;

    /**
     * 公法线
     */
    BaseTangent baseTangent;

    /**
     * 跨棒距
     */
    Span span;

    public Gear(Specifications specs) {
        this.specs = specs;
        baseTangent=new BaseTangent();
        span=new Span();
    }

    public Gear calculate(){
        calD();
        calDa();
        calDf();
        calAlphaT();
        calDb();
        calZp();
        calK();
        baseTangent.calculate();
        span.calculate();
        return this;
    }

    public Gear calculateDeviation(){
        baseTangent.calculateDeviation();
        span.calculateDeviation();
        return this;
    }

    private void calD() {
        d = specs.Mn * Math.abs(specs.Z)/ Math.cos(specs.beta);
    }

    private void calDa() {
        da = d + 2 * specs.Xn * specs.Mn;
        if (specs.Z > 0) {
            da += 2 * specs.Mn * specs.ha;
        } else {
            da -= 2 * specs.Mn * specs.ha;
        }
    }

    private void calDf() {
        df = d + 2 * specs.Xn * specs.Mn;
        if (specs.Z > 0) {
            df -= 2 * specs.Mn * (specs.hf + specs.Cf);
        } else {
            df += 2 * specs.Mn * (specs.hf + specs.Cf);
        }
    }

    private void calAlphaT() {
        alphaT = Math.atan(Math.tan(specs.alphaN) / Math.cos(specs.beta));
    }

    private void calDb() {
        db = d * Math.cos(alphaT);
    }

    private void calZp() {
        Zp = Math.abs(specs.Z) * (Math.tan(alphaT) - alphaT) / (Math.tan(specs.alphaN) - specs.alphaN);
    }

    private void calK() {
        /**
         * 只允许正数，向下取整
         */
        k = (int) Math.floor(specs.alphaN / Math.PI * Zp + 1);
    }

    public double getWk(){
        return baseTangent.Wk;
    }
    public double getDWk(){
        return baseTangent.dWk;
    }
    public double getDkm(){
        return span.dkm;
    }
    public double getM(){
        return span.M;
    }

    public double getX1() {
        return baseTangent.x1;
    }

    public double getM1() {
        return baseTangent.M1;
    }

    public double getMs() {
        return baseTangent.Ms;
    }

    public double getX2() {
        return baseTangent.x2;
    }

    public double getM2() {
        return baseTangent.M2;
    }

    public double getMx() {
        return baseTangent.Mx;
    }

    public double getAlphaM1() {
        return span.alphaM1;
    }

    public double getWs() {
        return span.Ws;
    }

    public double getAlphaM2() {
        return span.alphaM2;
    }

    public double getWx() {
        return span.Wx;
    }

    /**
     * 牛顿法求解渐开线参数角，默认参数角取值范围(0,PI)，已测试
     *
     * @param theta
     * @return 参数角（弧度制）
     */
    public static double NewtonCalAlpha(double theta) {
        /**
         * 迭代初始值
         */
        double x = 0.0001;
        double x1 = 1;
        /**
         * 求解精度设置
         */
        double precision = 0.000001;
        while (Math.abs(x1 - x) > precision) {
            x = x1;
            x1 = x - (Math.tan(x) - x - theta) / (Math.tan(x) * Math.tan(x));
        }
        return x1;
    }

    /**
     * 公法线
     * 需要先计算公法线基础部分和跨棒距基础部分，才能计算偏差部分
     * @author SuperNote
     */
    private strictfp class BaseTangent {
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

        private void calWk() {
            Wk = specs.Mn * Math.cos(specs.alphaN) * ((k - 0.5) * Math.PI + Zp * (Math.tan(specs.alphaN) - specs.alphaN)) + 2 * specs.Xn * specs.Mn * Math.sin(specs.alphaN);
        }

        private void calDWk() {
            dWk = Math.sqrt(db * db + Math.pow(Wk * Math.cos(specs.beta), 2));
        }

        private void calDWkInvA() {
            dWkInvA = Math.tan(alphaT)
                    - alphaT
                    + 2 * specs.Xn * Math.tan(specs.alphaN) / Math.abs(specs.Z)
                    + specs.dp / specs.Mn / specs.Z / Math.cos(specs.alphaN)
                    - Math.PI / 2/specs.Z;
        }

        private void calAlphaAm() {
            alphaM = NewtonCalAlpha(dWkInvA);
        }

        BaseTangent calculate(){
            calWk();
            calDWk();
            calDWkInvA();
            calAlphaAm();
            return this;
        }

        /**
         * 公法线上偏差
         */
        double x1;
        double x1InvA;
        double alphaM1;
        private double Mi1;
        double M1;
        double Ms;

        /**
         * 公法线下偏差
         */
        double x2;
        double x2InvA;
        double alphaM2;
        private double Mi2;
        double M2;
        double Mx;

        /**
         * 上偏差
         */
        private void calX1() {
            x1 = specs.Ws / specs.Mn / 2 / Math.sin(specs.alphaN);
        }

        private void calX1InvA() {
            x1InvA = Math.tan(alphaT)
                    - alphaT
                    + 2 * (specs.Xn + x1) * Math.tan(specs.alphaN) / Math.abs(specs.Z)
                    + specs.dp / specs.Mn / specs.Z / Math.cos(specs.alphaN)
                    - Math.PI / 2 / specs.Z;
        }

        private void calAlphaM1() {
            alphaM1 = NewtonCalAlpha(x1InvA);
        }

        private void calMi1() {
            if ((specs.Z & 1) == 1) {
                Mi1 = db / Math.cos(alphaM1) * Math.cos(Math.PI / 2 / Math.abs(specs.Z)) + specs.dp;
            } else {
                Mi1 = db / Math.cos(alphaM1) + specs.dp;
            }
        }

        private void calM1() {
            M1 = (specs.Z > 0) ? Mi1 : (Mi1 - 2 * specs.dp);
        }

        private void calMs() {
            Ms = M1 - span.M;
        }

        /**
         * 下偏差
         */
        private void calX2() {
            x2 = specs.Wx / specs.Mn / 2 / Math.sin(specs.alphaN);
        }

        private void calX2InvA() {
            x2InvA = Math.tan(alphaT)
                    - alphaT
                    + 2 * (specs.Xn + x2) * Math.tan(specs.alphaN) / Math.abs(specs.Z)
                    + specs.dp / specs.Mn / specs.Z / Math.cos(specs.alphaN)
                    - Math.PI / 2 / specs.Z;
        }

        private void calAlphaM2() {
            alphaM2 = NewtonCalAlpha(x2InvA);
        }

        private void calMi2() {
            if ((specs.Z & 1) == 1) {
                //cos是偶函数
                Mi2 = db / Math.cos(alphaM2) * Math.cos(Math.PI / 2 / Math.abs(specs.Z)) + specs.dp;
            } else {
                Mi2 = db / Math.cos(alphaM2) + specs.dp;
            }
        }

        private void calM2() {
            M2 = (specs.Z > 0) ? Mi2 : (Mi2 - 2 * specs.dp);
        }

        private void calMx() {
            Mx = M2 - span.M;
        }

        BaseTangent calculateDeviation(){
            calX1();
            calX1InvA();
            calAlphaM1();
            calMi1();
            calM1();
            calMs();
            calX2();
            calX2InvA();
            calAlphaM2();
            calMi2();
            calM2();
            calMx();
            return this;
        }

    }

    /**
     * 跨棒距
     * 需要先计算公法线基础部分和跨棒距基础部分，才能计算偏差部分
     * @author SuperNote
     */
    private strictfp class Span {
        /**
         * 跨棒距测量点直径
         */
        double dkm;
        /**
         * 跨棒距初算
         */
        private double Mi;
        /**
         * 跨棒距
         */
        double M;

        private void calDkm() {
            dkm = db / Math.cos(baseTangent.alphaM);
        }

        private void calMi() {
            if ((specs.Z & 1) == 1) {
                Mi = db / Math.cos(baseTangent.alphaM) * Math.cos(Math.PI / 2 / Math.abs(specs.Z)) + specs.dp;
            } else {
                Mi = db / Math.cos(baseTangent.alphaM) + specs.dp;
            }
        }

        private void calM() {
            M = (specs.Z > 0) ? Mi : (Mi - 2 * specs.dp);
        }

        Span calculate(){
            calDkm();
            calMi();
            calM();
            return this;
        }

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
         * 上偏差
         */
        private void calAlphaM1(){
            if ((specs.Z & 1) == 1) {
                alphaM1 = Math.acos(db * Math.cos(Math.PI / 2 / Math.abs(specs.Z))/(Mi+specs.Ms-specs.dp));//Excel
                // 实际公式与标称公式不符
            } else {
                alphaM1 =Math.acos(db /(Mi+specs.Ms-specs.dp));
            }
        }

        private void calX1(){
            x1=(Math.tan(alphaM1)-alphaM1-baseTangent.dWkInvA)*Math.abs(specs.Z)/2/Math.tan(specs.alphaN);
        }

        private void calWs(){
            Ws=2*x1*specs.Mn*Math.sin(specs.alphaN);
        }

        /**
         * 下偏差
         */
        private void calAlphaM2(){
            if ((specs.Z & 1) == 1) {
                alphaM2 = Math.acos(db * Math.cos(Math.PI / 2 / Math.abs(specs.Z))/(Mi+specs.Mx-specs.dp));//Excel
                // 实际公式与标称公式不符
            } else {
                alphaM2 =Math.acos(db /(Mi+specs.Mx-specs.dp));
            }
        }

        private void calX2(){
            x2=(Math.tan(alphaM2)-alphaM2-baseTangent.dWkInvA)*Math.abs(specs.Z)/2/Math.tan(specs.alphaN);
        }

        private void calWx(){
            Wx=2*x2*specs.Mn*Math.sin(specs.alphaN);
        }
        Span calculateDeviation(){
            calAlphaM1();
            calX1();
            calWs();
            calAlphaM2();
            calX2();
            calWx();
            return this;
        }

    }

    /**
     * 任一圆齿厚计算，非惰性
     */
    public strictfp class AnyCircle{
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
        public AnyCircle(double da) {
            this.da1 = da;
        }

        private void calAlphaT(){
            alphaT1 =Math.acos(db/ da1);
        }

        private void calS(){
            s=(Math.PI/2+2*specs.Xn*Math.tan(specs.alphaN))*specs.Mn/Math.cos(specs.beta);
        }

        private void calSa(){
            sa1=s* da1 /d;
            if (specs.Z>0){
                sa1-=da1 *(Math.tan(alphaT1)- alphaT1 -Math.tan(alphaT)+ alphaT);
            }else{
                sa1+=da1 *(Math.tan(alphaT1)- alphaT1 -Math.tan(alphaT)+ alphaT);
            }
        }

        private void calBeta(){
            beta1=Math.atan(Math.tan(specs.beta)*Math.cos(alphaT)/Math.cos(alphaT1));
        }

        private void calSn(){
            sn1=sa1*Math.cos(beta1);
        }

        AnyCircle calculate(){
            calAlphaT();
            calS();
            calSa();
            calBeta();
            calSn();
            return this;
        }

        public double getDa1() {
            return da1;
        }

        public double getAlphaT1() {
            return alphaT1;
        }

        public double getS() {
            return s;
        }

        public double getSa1() {
            return sa1;
        }

        public double getBeta1() {
            return beta1;
        }

        public double getSn1() {
            return sn1;
        }
    }
}
