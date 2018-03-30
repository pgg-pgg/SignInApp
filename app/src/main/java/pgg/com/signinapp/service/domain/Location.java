package pgg.com.signinapp.service.domain;

/**
 * Created by PDD on 2018/3/29.
 */

public class Location {

    private String id;
    private String longitude;//经度
    private String latitude;//维度

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
