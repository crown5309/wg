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
    address:null,
    orderIds:"",
    addressId:"",
    count:0,
    imgList:[],
    showDialog: false,
    shareDialog: false,
  },
  // 跳到购物车
  bindSubmitOrder(e) {
    let that = this
    if (that.data.address==null){
      wx.showToast({
        title: '请选择地址',
      })
    }
    var param = {
      orderIds: that.data.orderIds,
      addressId: that.data.address.id
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/front/pay", param, '数据加载中', function (res) {
      if (res.code == '100') {
        var res=res.info
        wx.requestPayment({
          timeStamp: res.timeStamp,
          nonceStr: res.nonceStr,
          package: res.package,
          signType: res.signType,
          paySign: res.paySign,
          success:function(){
            wx.navigateTo({ url: '../payCompelete/payCompelete?resultType=success' });
          },
          fail:function(){
            wx.navigateTo({ url: '../payCompelete/payCompelete?resultType=error' });
          },

        })
      } else {
        wx.navigateTo({ url: '../payCompelete/payCompelete?resultType=error' });
      }
    }, function () {
      wx.showToast({
        title: '失败',
      })
    })
    
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
      netUtil.requestLoading(app.globalData.baseUrl + "/front/getAddressById", param, '数据加载中', function (res) {
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
              address: address,
              addressShow: show,
              orderIds: options.orderIds,
              addressId: options.addressId
            })
          }
        }
        that.setData({
          orderinfo: res.info,
          count: res.info.count,
          orderIds: options.orderIds,
          addressShow: show,
          address: address,
        })
        if (that.data.orderinfo.imgList.length>1){
          that.setData({
            oneGoodsShow: false
          })
        }
        console.log(that.data.orderinfo.imgList)
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

  },
  shareDialog: function () {
    this.setData({
      shareDialog: !this.data.shareDialog,
    });
  },
})