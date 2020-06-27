const wxapi = require('utils/wxapi.js')
App({
  globalData: {
    userInfo:{},
    appId: wx.getAccountInfoSync().miniProgram.appId,
    sysParam: { appId: wx.getAccountInfoSync().miniProgram.appId},
    appsecret:"fb7207fded21b11f75701f9f3cba0944",
    userPermission:[],
    baseUrl:"http://192.168.0.107:8080",
    sessionId:"",
    wxapi: wxapi,
  },
  onLaunch: function () {
    console.log(wx.getAccountInfoSync().miniProgram)
  },
  Tips:function(param){
    wx.showToast(param)
  }
})