const app = getApp();
var netUtil = require("../../../utils/api.js"); //require引入
Page({
  data: {
    userInfo: {},
    hasUserInfo: true,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    mine_list: [
      {
        "pic_url": "/images/icons/iocn_home_01.png",
        "title": "商品添加",
        "router": "../addGoods/addGoods"
      },
      {
        "pic_url": "/images/icons/iocn_home_02.png",
        "title": "商品审核",
        "router": "../goodsManager/goodsManager"
      },
      {
        "pic_url": "/images/icons/iocn_home_03.png",
        "title": "商品上架",
        "router": "../goodsUpDown/goodsUpDown"
      },
      {
        "pic_url": "/images/icons/iocn_home_04.png",
        "title": "订单管理",
        "router": "../goods/goods"
      },
      {
        "pic_url": "/images/icons/iocn_home_04.png",
        "title": "商家审核",
        "router": "../addGoods/addGoods"
      },
      {
        "pic_url": "/images/icons/iocn_home_04.png",
        "title": "商品审核",
        "router": "../addGoods/addGoods"
      }
    ],
    orderItems: [
      {
        typeId: 1,
        name: '待付款',
        url: 'bill',
        imageurl: '../../../images/person/personal_pay.png',
      },
      {
        typeId: 2,
        name: '待收货',
        url: 'bill',
        imageurl: '../../../images/person/personal_receipt.png',
      },
      {
        typeId: 3,
        name: '已完成',
        url: 'bill',
        imageurl: '../../../images/person/personal_comment.png'
      },
      {
        typeId: 4,
        name: '退换/售后',
        url: 'bill',
        imageurl: '../../../images/person/personal_service.png'
      }
    ],
  },
  //事件处理函数
  toOrder: function (e) {
    wx.navigateTo({
      url: '../order/order?typeId=' + e.currentTarget.dataset.typeid
    })
  },
  onLoad: function () {
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse) {
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true
          })
        }
      })
    }
  },
  getUserInfo: function (e) {
    this.doAuthorization(e)
  },
  doAuthorization: function (e) {
    var that = this;
    console.log("调用了 doAuthorization 授权");
    // console.log(e);
    if (e.detail.userInfo == null) { //为null  用户拒绝了授权
      //coding。。。。
      console.log("用户拒绝授权");
    } else {
      //授权
      app.globalData.userInfo = e.detail.userInfo
      this.getOpenid();

      // this.setData({
      //   userInfo: e.detail.userInfo,
      //   hasUserInfo: false
      // })
    }

  },
  // 获取用户openid
  getOpenid: function () {
    let that = this;
    //获取openid不需要授权
    wx.login({
      success: function (res1) {
        //请求自己后台获取用户openid
            //可以把openid存到本地，方便以后调用
            wx.setStorageSync('openid', app.globalData.userInfo.openId);
        wx.getSetting({
          success: res => {
            if (res.authSetting['scope.userInfo']) {
              // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
              wx.getUserInfo({
                success: res => {
                  // 可以将 res 发送给后台解码出 unionId
                  console.log(res.userInfo)
                  //网络请求
                  that.data.params = res.userInfo
                  that.data.params.appid= app.globalData.appId
                  that.data.params.code=res1.code
                  
                  netUtil.requestLoading(app.globalData.baseUrl + "/user/auth", that.data.params, '登录中', function (res) {
                    //res就是我们请求接口返回的数据
                    console.log(res)
                    if (res.code == '100') {
                      app.globalData.sessionId = res.sessionId
                      that.setData({
                        userInfo: res.info,
                        hasUserInfo: false
                      })
                    } else {
                      wx.showToast({
                        title: res.msg
                      })
                    }
                  }, function () {
                    wx.showToast({
                      title: '登录失败',
                    })
                  })
                  // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
                  // 所以此处加入 callback 以防止这种情况
                  if (that.userInfoReadyCallback) {
                    that.userInfoReadyCallback(res)
                  }
                }
              })
            }
          }
        })
               }
        })
      }

  ,
  myAddress:function(e){
    wx.navigateTo({ url: '../addressList/addressList' });
  }
})
