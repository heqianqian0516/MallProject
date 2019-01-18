package bean;

public class QueryBean {
    private int commodityId;
    private int amount;

    public int getCommodityId() {
        return commodityId;
    }

    public QueryBean(int commodityId, int amount) {
        this.commodityId = commodityId;
        this.amount = amount;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
