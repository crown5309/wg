var netUtil = require("../../../utils/api.js"); //require引入
var app = getApp();
Page({
  data: {
    orderList: [],    //订单列表数据，接口获取
    address:{},
    logisticsList:[],
    logisticsLast:{},
    show:false,
    kuaidi:'',
    num:''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let that = this
    var param = {
      orderIds: options.orderIds,
      addressId: options.addressId,
      type:"logisticsType"
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/front/getOrderInfo", param, '数据加载中', function (res) {
      if (res.code == '100') {
        var result = res.info.OrderInfoList
        var array=[]
        var logisticsLast={}
        var show=false
        var num=''
        if (result[0].logisticsList.code=='100'){
          for (var i = 0; i < result[0].logisticsList.info.data.length; i++) {
            num = result[0].logisticsList.info.nu
            show = true;
            if (i == 0) {
              logisticsLast = result[0].logisticsList.info.data[i]
            } else {
              array.push(result[0].logisticsList.info.data[i])
            }
          }
        }
      
        that.setData({
          orderList: result,
          address: res.address,
          logisticsList: array,
          logisticsLast: logisticsLast,
          show: show,
          kuaidi: result[0].kuaidi,
          num: num
        })
      } else {
        app.Tips({ title: res.msg, icon: 'error' });
      }
    }, function () {
      wx.showToast({
        title: '失败',
      })
    })
    
  }
})