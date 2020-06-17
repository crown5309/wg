var netUtil = require("../../../utils/api.js"); //require引入
const app = getApp();
Page({
  data: {
    currentTab: 0,  //对应样式变化 
    scrollTop: 0,  //用作跳转后右侧视图回到顶部
    screenArray: [], //左侧导航栏内容
    screenId: "11",  //后台查询需要的字段
    childrenArray: {}, //右侧内容
  },

  onShow: function (options) {
    let that = this;
      netUtil.requestLoading(app.globalData.baseUrl + "/front/getAllGoodsClass", app.globalData.sysParam, '数据加载中', function (res) {
      //res就是我们请求接口返回的数据
      console.log(res)
      if (res.code == '100') {
        var screenArray = res.info;
         var screenId = screenArray[0].id;
        var childrenScreenArray = screenArray[0];
        console.log(childrenScreenArray)
         that.setData({
          screenArray: screenArray,
           screenId: screenId,
           childrenArray: childrenScreenArray,
           scrollTop:0,
           currentTab:0
        })
      } else {
        wx.showToast({
          title: res.msg
        })
      }
    }, function () {
      wx.showToast({
        title: '数据加载失败',
      })
    })
  },
  ToSearchResult:function(e){
    var id=e.currentTarget.dataset.id
    console.log(id)
     wx.navigateTo({
       url: '../list/list?id='+id
     })
  },
  navbarTap: function (e) {
    var that = this;
    this.setData({
      currentTab: e.currentTarget.id,   //按钮CSS变化
      screenId: e.currentTarget.dataset.screenid,
      scrollTop: 0,   //切换导航后，控制右侧滚动视图回到顶部
    })
    //刷新右侧内容的数据
    var childrenScreenArray = that.data.screenArray[e.currentTarget.id];
    that.setData({
      childrenArray: childrenScreenArray
    })
  },
})
