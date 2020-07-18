var netUtil = require("../../../utils/api.js"); //require引入
var app = getApp();
Page({
  data: {
    nodataType: 7,
    orderList: [],    //订单列表数据，接口获取
    currentPage: 1,
    isNoMoreData: false,
    orderState: null,
    winHeight: 900,
    currentTab: 0,     //当前显示tab的下标
    navTab: ['全部','待付款', '待收货', '已完成', '退款'],
    state:['',1,3,5,10],
    loading: true,
    origin:"JDCOM",
    pageNo:0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      currentTab: options.typeId
    })
    var currentTab = this.data.currentTab
    this.initData(currentTab);    //获取数据的方法
  },
  initData(currentPage) {

    //写你自己的接口
    var that = this;
    var pageNo = that.data.pageNo+1;
    var params = {
      state: that.data.state[currentPage],
      pageNo: pageNo,
      pageSize:5
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/front/getOrderInfoByState", params, '加载中', function (res) {
      //res就是我们请求接口返回的数据
      if (res.code == '100') {
        var arr = that.data.orderList
        if (arr.length == 0 || pageNo==1){
          arr = res.info
        }else{
          if (res.info.length == 0) {
            that.setData({
              isNoMoreData:true
            })
          }else{
            for (var i = 0; i < res.info.length; i++) {
              arr.push(res.info[i])
            }
          }
         
         
        }
       
        that.setData({
          orderList: arr,
          pageNo: pageNo
        });
      } else {
        wx.showToast({
          title: res.msg
        })
      }
    }, function () {
      wx.showToast({
        title: '加载失败',
      })
    })
  },
  switchNav(e) {  //点击 这个方法会触发bindChange()方法
    let isSelect = e.target.dataset.current;
    this.setData({
      currentTab: isSelect,
      pageNo:0,
      orderList:[] 

    })
  },

  bindChange(e) {    //切换swiper
    let isSelect = e.detail.current;
    this.initData(isSelect)
  },
  toDetail(e) {
    wx.navigateTo({
      url: '../orderDetail/orderDetail?orderIds=' + this.data.orderList[e.currentTarget.dataset.id].orderId + "&addressId=" + this.data.orderList[e.currentTarget.dataset.id].addressId
    })
  },

  /**
 * 生命周期函数--监听页面初次渲染完成
 */
  onReady: function () {   //获取设备高度
    let _this = this;
    wx.getSystemInfo({
      success: function (res) {
        // console.log(res.windowWidth);
        // console.log(res.windowHeight);
        _this.setData({
          winHeight: res.windowHeight
        })
      },
    })

  },
  /**
  * 页面上拉触底事件的处理函数
  */
  onReachBottom: function () {    //上拉加载分页
    console.log(this.data.isNoMoreData)
    if (!this.data.isNoMoreData) {
      this.setData({
        loading: true
      })
      this.initData(this.data.currentPage);
    }
  },
  powerDrawer: function (e) {
    var that=this
    var order = e.currentTarget.dataset.id;
    var params={
      orderId: order,
      state:5
    }
    wx.showModal({
      title: '提示',
      content: '确认收货',
      cancelText: "取消",
      confirmText: "通过",
      success(res) {
        if (res.confirm) {
          netUtil.requestLoading(app.globalData.baseUrl + "/front/updateOrderState", params, '加载中', function (res) {
            //res就是我们请求接口返回的数据
            console.log(res)
            if (res.code == '100') {
              wx.showToast({
                title: '成功',
              })
              that.setData({ pageNo:0})
              that.initData(that.data.currentTab)
            } else {
              wx.showToast({
                title: res.msg
              })
            }
          }, function () {
            wx.showToast({
              title: '加载失败',
            })
          })
        } 
      }
    })
  },
})