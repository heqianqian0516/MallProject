package api;


/**
 * @author Peng
 */
public class Apis {
    /**
     * 登录 http://172.17.8.100/small/user/v1/login
     */
    public static final String URL_LOGIN_POST = "user/v1/login";
    /**
     * 注册 http://172.17.8.100/small/user/v1/register
     */
    public static final String URL_REGISTER_POST = "user/v1/register";
    /**
     * 修改昵称 http://172.17.8.100/small/user/verify/v1/modifyUserNick
     */
    public static final String URL_UPDATE_NAME_POST="user/verify/v1/modifyUserNick";
    /**
     *  修改用户密码 http://172.17.8.100/small/user/verify/v1/modifyUserPwd
     */
    public static final String URL_UPDATE_PASSWORD_POST="user/verify/v1/modifyUserPwd";
    /**
     *  用户上传头像 http://172.17.8.100/small/user/verify/v1/modifyHeadPic
     */
    public static final String URL_UPDATE_HEADERIMAGE_POST="user/verify/v1/modifyHeadPic";
    /**
     * 根据用户ID查询用户信息 http://172.17.8.100/small/user/verify/v1/getUserById
     */
    public static final String URL_QUERYBYID_GET="user/verify/v1/getUserById";
    /**
     * 收货地址列表 http://172.17.8.100/small/user/verify/v1/receiveAddressList
     */
    public static final String URL_RECEIVE_ADDRESS_GET="user/verify/v1/receiveAddressList";
    /**
     * 新增收货地址 http://172.17.8.100/small/user/verify/v1/addReceiveAddress
     */
    public static final String URL_ADD_RECEIVE_ADDRESS_POST="user/verify/v1/addReceiveAddress";
    /**
     * 设置默认收货地址 http://172.17.8.100/small/user/verify/v1/setDefaultReceiveAddress
     */
    public static final String URL_SET_DEFAULT_RECEIVE_ADDRESS_POST="user/verify/v1/setDefaultReceiveAddress";
    /**
     * 修改收货信息 http://172.17.8.100/small/user/verify/v1/changeReceiveAddress
     */
    public static final String URL_CHANGE_RECEIVE_ADDRESS_PUT="user/verify/v1/changeReceiveAddress";
    /**
     * 查询用户钱包 http://172.17.8.100/small/user/verify/v1/findUserWallet
     */
    public static final String URL_FIND_USER_WALLET_GET="user/verify/v1/findUserWallet";
    /**
     * 创建订单 http://172.17.8.100/small/order/verify/v1/createOrder
     */
    public static final String URL_CREATE_ORDER_POST="user/verify/v1/createOrder";
    /**
     * 支付 http://172.17.8.100/small/order/verify/v1/pay
     */
    public static final String URL_PAY_POST="user/verify/v1/pay";
    /**
     * 根据订单状态查询订单信息 http://172.17.8.100/small/order/verify/v1/findOrderListByStatus
     */
    public static final String URL_FIND_ORDER_LIST_BYSTATUS_GET="user/verify/v1/findUserWallet";
    /**
     * 删除订单 http://172.17.8.100/small/order/verify/v1/deleteOrder
     */
    public static final String URL_DELETE_ORDER_DELETE="user/verify/v1/deleteOrder";
    /**
     * 收货 http://172.17.8.100/small/order/verify/v1/confirmReceipt
     */
    public static final String URL_CONFIRM_RECEIPT_PUT="user/verify/v1/confirmReceipt";
    /**
     * 查询购物车 http://172.17.8.100/small/order/verify/v1/findShoppingCart
     */
    public static final String URL_FIND_SHOPPING_CART_GET="user/verify/v1/findShoppingCart";
    /**
     * 同步购物车数据 http://172.17.8.100/small/order/verify/v1/syncShoppingCart
     */
    public static final String URL_SYNC_SHOPPING_CART_PUT="user/verify/v1/syncShoppingCart";
    /**
     * 查询订单明细数据 http://172.17.8.100/small/order/verify/v1/findOrderInfo
     */
    public static final String URL_FIND_ORDER_INFO_GET="user/verify/v1/findOrderInfo";
    /**
     * 圈子列表 http://172.17.8.100/small/circle/v1/findCircleList
     */
    public static final String URL_FIND_CIRCLE_LIST_GET="circle/v1/findCircleList?page=%d&count=%d";
    /**
     * 发布圈子 http://172.17.8.100/small/circle/verify/v1/releaseCircle
     */
    public static final String URL_RELEASE_CIRCLE_POST="user/verify/v1/releaseCircle";
    /**
     * 删除我发表过的圈子 http://172.17.8.100/small/circle/verify/v1/deleteCircle
     */
    public static final String URL_DELETE_CIRCLE_DELETE="user/verify/v1/deleteCircle";
    /**
     * 我的圈子 http://172.17.8.100/small/circle/verify/v1/findMyCircleById
     */
    public static final String URL_FIND_MYCIRCLE_BYID_GET="circle/verify/v1/findMyCircleById";
    /**
     * 圈子点赞 http://172.17.8.100/small/circle/verify/v1/addCircleGreat
     */
    public static final String URL_ADD_CIRCLE_GREAT_POST="user/verify/v1/addCircleGreat";
    /**
     * 取消点赞 http://172.17.8.100/small/circle/verify/v1/cancelCircleGreat
     */
    public static final String URL_CANCLE_CIRCLE_GREAT_DELETE="user/verify/v1/cancelCircleGreat";
    /**
     * banner展示列表 http://172.17.8.100/small/commodity/v1/bannerShow
     */
    public static final String URL_BANNER_SHOW_GET="commodity/v1/bannerShow";
    /**
     * 首页商品信息列表 http://172.17.8.100/small/commodity/v1/commodityList
     */
    public static final String URL_COMMODITY_LIST_GET="commodity/v1/commodityList";
    /**
     * 根据商品列表归属标签查询商品信息 http://172.17.8.100/small/commodity/v1/findCommodityListByLabel
     */
    public static final String URL_FIND_COMMODITY_LIST_BYLABEL_GET="commodity/v1/findCommodityListByLabel";
    /**
     *  商品详情 http://172.17.8.100/small/commodity/v1/findCommodityDetailsById
     */
    public static final String URL_FIND_COMMODITY_DETAILS_BYID_GET="commodity/v1/findCommodityDetailsById";
    /**
     * 根据关键词查询商品信息 http://172.17.8.100/small/commodity/v1/findCommodityByKeyword
     */
    public static final String URL_FIND_COMMODITY_BYKEYWORD_GET="commodity/v1/findCommodityByKeyword";
    /**
     * 根据二级类目查询商品信息 http://172.17.8.100/small/commodity/v1/findCommodityByCategory
     * http://172.17.8.100/small/commodity/v1/findFirstCategory
     */
    public static final String URL_FIND_COMMODITY_BYCATEGORY_GET="commodity/v1/findCommodityByCategory";
    /**
     * 我的足迹 http://172.17.8.100/small/commodity/verify/v1/browseList
     */
    public static final String URL_BROWSE_LIST_GET="commodity/verify/v1/browseList";
    /**
     * 商品评论列表 http://172.17.8.100/small/commodity/v1/CommodityCommentList
     */
    public static final String URL_COMMODITY_COMMENT_LIST_GET="commodity/v1/CommodityCommentList";
    /**
     * 商品评论 http://172.17.8.100/small/commodity/verify/v1/addCommodityComment
     */
    public static final String URL_ADD_COMMODITY_COMMENT_LIST_POST="commodity/verify/v1/addCommodityComment";
    /**
     * 查询一级商品类目 http://172.17.8.100/small/commodity/v1/findFirstCategory
     */
    public static final String URL_FIND_FIRST_CATEGORY_GET="commodity/v1/findFirstCategory";
    /**
     * 查询二级商品类目 http://172.17.8.100/small/commodity/v1/findSecondCategory
     */
    public static final String URL_FIND_SECOND_CATEGORY_GET="commodity/v1/findSecondCategory?firstCategoryId=";


}
