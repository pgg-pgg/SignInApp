package pgg.com.signinapp.service.model;

import pgg.com.signinapp.data.httpData.HttpData;
import pgg.com.signinapp.service.domain.AddFaceInfo;
import pgg.com.signinapp.service.domain.FaceSetInfo;
import rx.Observer;

/**
 * Created by PDD on 2018/3/29.
 */

public class AddFaceModel {
    public void addFaceInfo(final OnLoadDataListener listener, String faceset_token,String face_tokens){
        HttpData.getInstance().addFaceToSet(new Observer<AddFaceInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onRequestFail(e);
            }

            @Override
            public void onNext(AddFaceInfo addFaceInfo) {
                listener.onSuccess(addFaceInfo);
            }
        }, faceset_token, face_tokens);
    }
}
