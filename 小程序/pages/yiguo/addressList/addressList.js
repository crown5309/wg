const app = getApp();
var netUtil = require("../../../utils/api.js"); //require引入
Page({

  /**页面的初始数据 */
  data: {

    addressList: [],
    source:"",
    orderIds:""
  },

  /*生命周期函数--监听页面加载*/

  onLoad: function (options) {
let that=this
    var source = options.source
    netUtil.requestLoading(app.globalData.baseUrl + "/front/getAddressList", "", '数据加载中', function (res) {
      if (res.code == '100') {
        that.setData({
          addressList:res.info,
          source: source,
          orderIds: options.orderIds
        })
      } else {
        app.Tips({ title: res.msg, icon: 'success' });
      }
    }, function () {
      wx.showToast({
        title: '失败',
      })
    })

  },

  change:function(e){
    var id = e.currentTarget.dataset.id
    if(this.data.source=="pay"){
      wx.redirectTo({
        url: '../pay/pay?addressId=' + id + "&orderIds="+this.data.orderIds,
      })
    }
    console.log("11" + id)
  },

  /**生命周期函数--监听页面显示 */
  onShow: function () {

    this.onLoad();

  },

  addAddress: function () {

    wx.navigateTo({ url: '../address/address' });

  },

  /* 删除item */

  delAddress: function (e) {
    var id = e.currentTarget.dataset.id
    wx.navigateTo({ url: '../address/address?id='+id });
  }
})