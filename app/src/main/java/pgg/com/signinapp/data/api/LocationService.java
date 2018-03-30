package pgg.com.signinapp.data.api;

import pgg.com.signinapp.service.domain.Location;
import pgg.com.signinapp.service.domain.Results;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by PDD on 2018/3/25.
 */

public interface LocationService {

    @POST("Location/addLocation")
    Observable<Results<Location>> addLocation(@Body Location location);


}
