package bean;

import java.util.List;

public class SecondCategoryBean {


    /**
     * result : [{"id":"1001004001","name":"高跟鞋"},{"id":"1001004002","name":"帆布鞋"},{"id":"1001004003","name":"豆豆鞋"},{"id":"1001004004","name":"板鞋"},{"id":"1001004005","name":"凉鞋"}]
     * message : 查询成功
     * status : 0000
     */

    private String message;
    private String status;
    private List<ResultBean> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 1001004001
         * name : 高跟鞋
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
