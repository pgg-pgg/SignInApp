package pgg.com.signinapp.service.domain;


import java.util.List;

/**
 * Created by PDD on 2018/3/27.
 */

public class FaceInfo {


    /**
     * faces : [{"face_rectangle":{"height":296,"left":53,"top":295,"width":296},"face_token":"3774c45c7df502b0b3cd0cb98d869c7c"}]
     * image_id : WCDZ6ZDEgcv6g+GfXdrKNw==
     * request_id : 1522144116,34fad992-3c19-4945-bf27-6f8e048a7695
     * time_used : 320
     */

    private String image_id;
    private String request_id;
    private int time_used;
    private List<FacesBean> faces;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public List<FacesBean> getFaces() {
        return faces;
    }

    public void setFaces(List<FacesBean> faces) {
        this.faces = faces;
    }

    public static class FacesBean {
        /**
         * face_rectangle : {"height":296,"left":53,"top":295,"width":296}
         * face_token : 3774c45c7df502b0b3cd0cb98d869c7c
         */

        private FaceRectangleBean face_rectangle;
        private String face_token;

        public FaceRectangleBean getFace_rectangle() {
            return face_rectangle;
        }

        public void setFace_rectangle(FaceRectangleBean face_rectangle) {
            this.face_rectangle = face_rectangle;
        }

        public String getFace_token() {
            return face_token;
        }

        public void setFace_token(String face_token) {
            this.face_token = face_token;
        }

        public static class FaceRectangleBean {
            /**
             * height : 296
             * left : 53
             * top : 295
             * width : 296
             */

            private int height;
            private int left;
            private int top;
            private int width;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }
        }
    }
}
