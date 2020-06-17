// pages/detail/detail.js
var app = getApp();
var netUtil = require("../../../utils/api.js"); //require引入
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isLike: false,
    showDialog: false,
    indicatorDots: true, //是否显示面板指示点
    autoplay: true, //是否自动切换
    interval: 3000, //自动切换时间间隔,3s
    duration: 1000, //  滑动动画时长1s,
    param: "",//car buy 加入购物车，购买
    goods:{},
    storeList:[],
    orderinfo:{},
    oneGoodsShow:true,
    addressShow:true,
    address:{},
    orderIds:"",
    addressId:""
  },
  // 跳到购物车
  bindSubmitOrder(e) {
    let that = this
    var param = {
      orderIds: that.data.orderIds,
      addressId: that.data.address.id
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/front/pay", param, '数据加载中', function (res) {
      if (res.code == '100') {
      } else {
        app.Tips({ title: res.msg, icon: 'error' });
      }
    }, function () {
      wx.showToast({
        title: '失败',
      })
    })
    wx.navigateTo({ url: '../payCompelete/payCompelete?resultType=success' });
  },
  bindaddress(e) {
    wx.navigateTo({ url: '../addressList/addressList?source=pay&orderIds=' + this.data.orderIds });
  },
  // 立即购买
  immeBuy() {
    this.setData({
      showDialog: !this.data.showDialog,
      param: "buy"
    });
  },
  onShow:function(){
    var param={
      addressId: this.data.addressId
    }
    if (this.data.addressId!="")
    netUtil.requestLoading(app.globalData.baseUrl + "/front/addressId", param, '数据加载中', function (res) {
      if (res.code == '100') {
        that.setData({
          address: res.info,
        })
      } else {
        app.Tips({ title: res.msg, icon: 'error' });
      }
    }, function () {
      wx.showToast({
        title: '失败',
      })
    })
  },
  onLoad: function (options) {
    let that = this
    var param={
      orderIds: options.orderIds,
      addressId: options.addressId
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/front/getOrderInfo", param, '数据加载中', function (res) {
      if (res.code == '100') {
        var result = res.info.OrderInfoList
        var address = res.address
       var show=true;
        if (address == null || address=='null'){
          show = true
        }else{
          show = false
        }
        if (result.length==1){
          if (result[0].goodsList.length==1){
            that.setData({
              goods: result[0].goodsList[0],
              orderinfo: res.info,
              address: address,
              addressShow: show,
              orderIds: options.orderIds,
              addressId: options.addressId
            })
          }
        }
       for(var i=0;i<result.length;i++){
          
       }
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