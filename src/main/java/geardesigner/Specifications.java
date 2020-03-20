package geardesigner;

/**
 * @author SuperNote
 */
public class Specifications {
    /**
    角全部以弧度制存储
     */
    /**
     * 法向模数
     */
    int Mn;
    /**
     * 齿数（内齿为负）
     */
    int Z;
    /**
     *法向压力角
     */
    double alphaN;
    /**
     * 螺旋角
     */
    double beta;
    /**
     * 法向变位系数
     */
    double Xn;
    /**
     * 齿顶高系数
     */
    double ha;
    /**
     * 齿根高系数
     */
    double hf;
    /**
     * 顶隙系数
     */
    double Cf;
    /**
     * 量棒直径
     */
    double dp;
    /**
     * 公法线上偏差
     */
    double Ws;
    /**
     * 公法线下偏差
     */
    double Wx;
    /**
     * 跨棒距上偏差
     */
    double Ms;
    /**
     * 跨棒距下偏差
     */
    double Mx;

    /**
     * 检查参数设置是否合法
     * @return
     */
    public boolean isValued(){
        return true;//// TODO: 2020/2/9 检查参数设置是否合法
    }


    public static final class SpecificationsBuilder {
        private int Mn;
        private int Z;
        private double alphaN;
        private double beta;
        private double Xn;
        private double ha;
        private double hf;
        private double Cf;
        private double dp;
        private double Ws;
        private double Wx;
        private double Ms;
        private double Mx;

        private SpecificationsBuilder() {
        }

        public static SpecificationsBuilder aSpecifications() {
            return new SpecificationsBuilder();
        }

        public SpecificationsBuilder Mn(int Mn) {
            this.Mn = Mn;
            return this;
        }

        public SpecificationsBuilder Z(int Z) {
            this.Z = Z;
            return this;
        }

        public SpecificationsBuilder alphaN(double alphaN) {
            this.alphaN = alphaN;
            return this;
        }

        public SpecificationsBuilder beta(double beta) {
            this.beta = beta;
            return this;
        }

        public SpecificationsBuilder Xn(double Xn) {
            this.Xn = Xn;
            return this;
        }

        public SpecificationsBuilder ha(double ha) {
            this.ha = ha;
            return this;
        }

        public SpecificationsBuilder hf(double hf) {
            this.hf = hf;
            return this;
        }

        public SpecificationsBuilder Cf(double Cf) {
            this.Cf = Cf;
            return this;
        }

        public SpecificationsBuilder dp(double dp) {
            this.dp = dp;
            return this;
        }

        public SpecificationsBuilder Ws(double Ws) {
            this.Ws = Ws;
            return this;
        }

        public SpecificationsBuilder Wx(double Wx) {
            this.Wx = Wx;
            return this;
        }

        public SpecificationsBuilder Ms(double Ms) {
            this.Ms = Ms;
            return this;
        }

        public SpecificationsBuilder Mx(double Mx) {
            this.Mx = Mx;
            return this;
        }

        public Specifications build() {
            Specifications specifications = new Specifications();
            specifications.Mn = this.Mn;
            specifications.Z = this.Z;
            specifications.Xn = this.Xn;
            specifications.ha = this.ha;
            specifications.beta = this.beta;
            specifications.Mx = this.Mx;
            specifications.alphaN = this.alphaN;
            specifications.hf = this.hf;
            specifications.dp = this.dp;
            specifications.Ws = this.Ws;
            specifications.Ms = this.Ms;
            specifications.Wx = this.Wx;
            specifications.Cf = this.Cf;
            if (specifications.isValued()) {
                return specifications;
            }else {
                throw new IllegalArgumentException("填入的参数不正确");
            }
        }
    }
}
