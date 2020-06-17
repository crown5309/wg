const app = getApp();
var netUtil = require("../../../utils/api.js"); //require引入
Page({

  /**
   * 页面的初始数据
   */
  data: {
    parameter: {
      'navbar': '1',
      'return': '1',
      'title': '添加地址'
    },
    region: ['省', '市', '区'],
    cartId: '',//购物车id
    pinkId: 0,//拼团id
    couponId: 0,//优惠券id
    id: 0,//地址id
    userAddress: { is_default: false },//地址详情
  },
  /**
   * 授权回调
   * 
  */
  onLoadFun: function () {
    this.getUserAddress();
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var param={
      addressId:options.id
    }
    if (options.id!=null){
      netUtil.requestLoading(app.globalData.baseUrl + "/front/getAddressById", param, '数据加载中', function (res) {
        if (res.code == '100') {
          var region = [res.info.province, res.info.city, res.info.country];
          that.setData({
            userAddress: res.info,
            region: region,
            id: options.id
          });
        } else {
          app.Tips({ title: res.msg, icon: 'success' });
        }
      }, function () {
        wx.showToast({
          title: '失败',
        })
      })
    }
  },
  bindRegionChange: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      region: e.detail.value
    })
  },
  getUserAddress: function () {
    if (!this.data.id) return false;
    var that = this;
    getAddressDetail(this.data.id).then(res => {
      var region = [res.data.province, res.data.city, res.data.district];
      that.setData({
        userAddress: res.data,
        region: region,
      });
    });
  },
  getWxAddress: function () {
    var that = this;
    wx.authorize({
      scope: 'scope.address',
      success: function (res) {
        wx.chooseAddress({
          success: function (res) {
            var addressP = {};
            addressP.province = res.provinceName;
            addressP.city = res.cityName;
            addressP.district = res.countyName;
            editAddress({
              address: addressP,
              is_default: 1,
              real_name: res.userName,
              post_code: res.postalCode,
              phone: res.telNumber,
              detail: res.detailInfo,
              id: 0
            }).then(res => {
              setTimeout(function () {
                if (that.data.cartId) {
                  var cartId = that.data.cartId;
                  var pinkId = that.data.pinkId;
                  var couponId = that.data.couponId;
                  that.setData({ cartId: '', pinkId: '', couponId: '' })
                  wx.navigateTo({
                    url: '/pages/order_confirm/index?cartId=' + cartId + '&addressId=' + (that.data.id ? that.data.id : res.data.id) + '&pinkId=' + pinkId + '&couponId=' + couponId
                  });
                } else {
                  wx.navigateBack({ delta: 1 });
                }
              }, 1000);
              return app.Tips({ title: "添加成功", icon: 'success' });
            }).catch(err => {
              return app.Tips({ title: err });
            });
          },
          fail: function (res) {
            if (res.errMsg == 'chooseAddress:cancel') return app.Tips({ title: '取消选择' });
          },
        })
      },
      fail: function (res) {
        wx.showModal({
          title: '您已拒绝导入微信地址权限',
          content: '是否进入权限管理，调整授权？',
          success(res) {
            if (res.confirm) {
              wx.openSetting({
                success: function (res) {
                  console.log(res.authSetting)
                }
              });
            } else if (res.cancel) {
              return app.Tips({ title: '已取消！' });
            }
          }
        })
      },
    })
  },
  /**
   * 提交用户添加地址
   * 
  */
  formSubmit: function (e) {
    var that = this, value = e.detail.value;
    if (!value.name) return app.Tips({ title: '请填写姓名' });
    if (!value.telephone) return app.Tips({ title: '请填写联系电话' });
    if (!/^1(3|4|5|7|8|9|6)\d{9}$/i.test(value.telephone)) return app.Tips({ title: '手机号不正确' });
    if (that.data.region[0] == '省') return app.Tips({ title: '请选择所在地区' });
    if (!value.detail) return app.Tips({ title: '请填写详细地址' });
    value.id = that.data.id;
    value.province = that.data.region[0]
    value.city = that.data.region[1]
    value.country = that.data.region[2]
    value.is_default = that.data.userAddress.is_default ? 1 : 0;
    netUtil.requestLoading(app.globalData.baseUrl + "/front/addAddress", value, '提交中', function (res) {
        if(res.code=='100'){
          if (that.data.id)
            wx.showToast({
              title: '修改成功',
              icon: "success",
              duration: 1000,
              mask: true,
            })
          else
            wx.showToast({
              title: '添加成功',
              icon: "success",
              duration: 1000,
              mask: true,
            })
            var time=setTimeout(function(){
              wx.navigateBack({
                delta: 1
              })
              clearTimeout(time)
            },1001)
           
          // wx.redirectTo({
          //   url: '../addressList/addressList'
          // })
        }else{
          app.Tips({ title: res.msg, icon: 'success' });
        }
    }, function () {
      wx.showToast({
        title: '失败',
      })
    })
      
   
  },
  Del:function(){
    var param={
      addressId:this.data.id
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/front/deleteAddressById", param, '数据加载中', function (res) {
      if (res.code == '100') {
        wx.showToast({
          title: '删除成功',
          icon: "success",
          duration: 1000,
          mask: true,
        })
        var time = setTimeout(function () {
          wx.navigateBack({
            delta: 1
          })
          clearTimeout(time)
        }, 1001)
      } else {
        app.Tips({ title: res.msg, icon: 'success' });
      }
    }, function () {
      wx.showToast({
        title: '失败',
      })
    })
  },
  ChangeIsDefault: function (e) {
    this.setData({ 'userAddress.is_default': !this.data.userAddress.is_default });
  },

})