package pgg.com.signinapp.service.model;

import pgg.com.signinapp.data.httpData.HttpData;
import pgg.com.signinapp.service.domain.FaceCompareInfo;
import pgg.com.signinapp.service.domain.Location;
import pgg.com.signinapp.service.domain.Results;
import rx.Observer;

/**
 * Created by PDD on 2018/3/28.
 */

public class FaceCompareModel {

    public void getCompareFaceInfo(final OnLoadDataListener listener, String face_token, String image_base64_2, final Location location){
        HttpData.getInstance().getCompareFace(new Observer<Results<Location>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onRequestFail(e);
            }

            @Override
            public void onNext(Results<Location> locationResults) {
                listener.onSuccess(locationResults);
            }
        }, face_token, image_base64_2,location);
    }
}
