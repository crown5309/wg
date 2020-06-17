const app = getApp();
var netUtil = require("../../../utils/api.js"); //require引入
var openid = wx.getStorageSync("openid");
    Page({
      data: {
        params:{},
        userInfo:app.globalData.userInfo,
        mine_list: [
          {
            "pic_url": "/images/icons/iocn_home_01.png",
            "title": "商品添加",
            "router":"../addGoods/addGoods"
          },
          {
            "pic_url": "/images/icons/iocn_home_02.png",
            "title": "商品管理",
            "router": "../addGoods/addGoods"
          },
          {
            "pic_url": "/images/icons/iocn_home_03.png",
            "title": "商品分类",
            "router": "../detail/detail"
          },
          {
            "pic_url": "/images/icons/iocn_home_04.png",
            "title": "订单管理",
            "router": "../goods/goods"
          },
          {
            "pic_url": "/images/icons/iocn_home_04.png",
            "title": "店铺管理",
            "router": "../addGoods/addGoods"
          },
          {
            "pic_url": "/images/icons/iocn_home_04.png",
            "title": "商品审核",
            "router": "../addGoods/addGoods"
          }
        ],
        hasUserInfo: openid == ""
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
          success: function (res) {
            //请求自己后台获取用户openid
            wx.request({
              url: 'https://api.weixin.qq.com/sns/jscode2session',
              data: {
                appid: app.globalData.appId,
                secret: app.globalData.appsecret,
                js_code: res.code,
                grant_type:'authorization_code'
              },
              success: function (response) {
                app.globalData.userInfo.openId = response.data.openid;
                app.globalData.userInfo.appId = app.globalData.appId;
                console.log('请求获取openid:' + openid);
                //可以把openid存到本地，方便以后调用
                wx.setStorageSync('openid', openid);
                //网络请求
                that.data.params = app.globalData.userInfo;
                netUtil.requestLoading(app.globalData.baseUrl+"/login/frontAuth", that.data.params, '登录中', function (res) {
                  //res就是我们请求接口返回的数据
                  console.log(res)
                    if(res.code=='100'){
                      app.globalData.userInfo = res.info.userPermission
                         that.setData({
                           userInfo: res.info.userPermission,
                           hasUserInfo: false
                       })
                    }else{
                      wx.showToast({
                        title: res.msg
                      })
                    }
                }, function () {
                  wx.showToast({
                    title: '登录失败',
                  })
                })
              }
            })
          }
        })
      },
  gotoOrder:function(){
    wx.navigateTo({
      url: '../order/order'
    })
  },
   gotoaddressList: function (){
     wx.navigateTo({
       url: '../addressList/addressList'
     })
  },
  // 事件处理函数
  bindViewTap: function () {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  onShow: function () {
    var that = this;
    console.log("openid:", openid);
  },
    onLaunch: function () {
      that.doAuthorization();
    },
})
