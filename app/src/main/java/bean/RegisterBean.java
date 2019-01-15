package bean;

public class RegisterBean {
    private String status;
    private String message;
    private final String SUCCESS_CODE="登录成功";
    /**
     * result : {"headPic":"http://172.17.8.100/images/small/head_pic/2018-11-21/20181121100733.jpg","nickName":"OP_8mY65","phone":"16619958760","sessionId":"154276714558512","sex":1,"userId":12}
     */

    private ResultBean result;

    public boolean isSuccess(){
        return message.equals(SUCCESS_CODE);
    };


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * headPic : http://172.17.8.100/images/small/head_pic/2018-11-21/20181121100733.jpg
         * nickName : OP_8mY65
         * phone : 16619958760
         * sessionId : 154276714558512
         * sex : 1
         * userId : 12
         */

        private String headPic;
        private String nickName;
        private String phone;
        private String sessionId;
        private int sex;
        private int userId;

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
