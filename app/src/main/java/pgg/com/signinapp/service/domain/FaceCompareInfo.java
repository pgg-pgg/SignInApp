package pgg.com.signinapp.service.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PDD on 2018/3/28.
 */

public class FaceCompareInfo {


    /**
     * time_used : 473
     * confidence : 96.46
     * thresholds : {"1e-3":65.3,"1e-5":76.5,"1e-4":71.8}
     * request_id : 1469761507,07174361-027c-46e1-811f-ba0909760b18
     */

    private int time_used;
    private double confidence;
    private ThresholdsBean thresholds;
    private String request_id;

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public ThresholdsBean getThresholds() {
        return thresholds;
    }

    public void setThresholds(ThresholdsBean thresholds) {
        this.thresholds = thresholds;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public static class ThresholdsBean {
        /**
         * 1e-3 : 65.3
         * 1e-5 : 76.5
         * 1e-4 : 71.8
         */

        @SerializedName("1e-3")
        private double _$1e3;
        @SerializedName("1e-5")
        private double _$1e5;
        @SerializedName("1e-4")
        private double _$1e4;

        public double get_$1e3() {
            return _$1e3;
        }

        public void set_$1e3(double _$1e3) {
            this._$1e3 = _$1e3;
        }

        public double get_$1e5() {
            return _$1e5;
        }

        public void set_$1e5(double _$1e5) {
            this._$1e5 = _$1e5;
        }

        public double get_$1e4() {
            return _$1e4;
        }

        public void set_$1e4(double _$1e4) {
            this._$1e4 = _$1e4;
        }
    }
}
