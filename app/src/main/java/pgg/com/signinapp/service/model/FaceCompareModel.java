package pgg.com.signinapp.service.model;

import pgg.com.signinapp.data.httpData.HttpData;
import pgg.com.signinapp.service.domain.FaceCompareInfo;
import rx.Observer;

/**
 * Created by PDD on 2018/3/28.
 */

public class FaceCompareModel {

    public void getCompareFaceInfo(final OnLoadDataListener listener, String face_token,String image_base64_2){
        HttpData.getInstance().getCompareFace(new Observer<FaceCompareInfo>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onRequestFail(e);
            }

            @Override
            public void onNext(FaceCompareInfo info) {
                listener.onSuccess(info);
            }
        },face_token,image_base64_2);
    }
}
