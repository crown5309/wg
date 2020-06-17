const wxapi = require('utils/wxapi.js')
App({
  globalData: {
    userInfo:{},
    appId: wx.getAccountInfoSync().miniProgram.appId,
    sysParam: { appId: wx.getAccountInfoSync().miniProgram.appId},
    appsecret:"48224cbb639b0c13f37904763998dc59",
    userPermission:[],
    baseUrl:"http://127.0.0.1:8080",
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