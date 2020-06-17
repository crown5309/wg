var app = getApp();
var netUtil = require("../../../utils/api.js"); //require引入
Page({
  /**
   * 页面的初始数据
   */
  data: {
    carts: [], //数据 
    iscart: false,
    hidden: null,
    isAllSelect: false,
    totalMoney: 0,
    navTab: ['待审核', '已审核', '已拒绝 '],
    goodState:[0,1,4],
    state:0,
    currentTab:0,
    currentPage: 1,
    isNoMoreData: false,
    page:1,
    isMore:true,
    goodIds:[]
  },
  onShow: function () {
    //接口获取数据
    let that=this
    var page = that.data.page
    var arr=[]
    var params={
      appId:app.globalData.appId,
      state: that.data.goodState[that.data.currentTab],
      pageNo:page,
      pageSize:5
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/front/getAllGoodsByState",params, '加载中', function (res) {
      //res就是我们请求接口返回的数据
      console.log(res)
      if (res.code == '100') {
        arr= res.info
        that.setData({
          carts: arr,
        });
      } else {
        wx.showToast({
          title: res.msg
        })
      }
    }, function () {
      wx.showToast({
        title: '加载失败',
      })
    })
    // if (arr.length > 0) {
    //   // 更新数据  
    //   this.setData({
    //     carts: arr,
    //     iscart: true,
    //     hidden: false
    //   });
    //   console.info("缓存数据：" + this.data.carts);
    // } else {
    //   this.setData({
    //     iscart: false,
    //     hidden: true,
    //   });
    // }
  },
  //勾选事件处理函数  
  switchSelect: function (e) {
    // 获取item项的id，和数组的下标值 
    console.log(e) 
    var Allprice = 0, i = 0;
    let id = e.target.dataset.id
    var  index = parseInt(e.target.dataset.index);
    var goodIds = this.data.goodIds
    console.log("hhh" + this.data.carts[index].isSelect)
   
      var flag=true;
      for (var i = 0; i < goodIds.length;i++){
        if (this.data.carts[index].goodsId == goodIds[i]){
          flag=false
          goodIds.splice(i, 1)
        }
      }
      if (flag){
        goodIds.push(this.data.carts[index].goodsId)
      }
    this.data.carts[index].isSelect = !this.data.carts[index].isSelect;
    //价钱统计
   
    if (this.data.carts[index].isSelect) {
      this.data.totalMoney = this.data.totalMoney + (this.data.carts[index].price * this.data.carts[index].count);
    }
    else {
      this.data.totalMoney = this.data.totalMoney - (this.data.carts[index].price * this.data.carts[index].count);
      
    }
    //是否全选判断
    for (i = 0; i < this.data.carts.length; i++) {
      Allprice = Allprice + (this.data.carts[index].price * this.data.carts[index].count);
    }
    if (Allprice == this.data.totalMoney) {
      this.data.isAllSelect = true;
    }
    else {
      this.data.isAllSelect = false;
    }
    this.setData({
      carts: this.data.carts,
      totalMoney: this.data.totalMoney,
      isAllSelect: this.data.isAllSelect,
      goodIds: goodIds
    })
    console.log(goodIds)
  },
  //全选
  allSelect: function (e) {
    //处理全选逻辑
    let i = 0;
    if (!this.data.isAllSelect) {
      this.data.totalMoney = 0;
      for (i = 0; i < this.data.carts.length; i++) {
        this.data.carts[i].isSelect = true;
        this.data.totalMoney = this.data.totalMoney + (this.data.carts[i].price * this.data.carts[i].count);

      }
    }
    else {
      for (i = 0; i < this.data.carts.length; i++) {
        this.data.carts[i].isSelect = false;
      }
      this.data.totalMoney = 0;
    }
    this.setData({
      carts: this.data.carts,
      isAllSelect: !this.data.isAllSelect,
      totalMoney: this.data.totalMoney,
    })
  },
  // 去结算
  toBuy() {
    var that=this
    var goodIds = this.data.goodIds;
    var carts = this.data.carts
    if (goodIds.length==0){
      wx.showToast({
        title: '请选择商品',
      })
      return
    }
    wx.showModal({
      title: '提示',
      content: '审核商品',
      cancelText:"拒绝",
      confirmText:"通过",
      success(res) {
        if (res.confirm) {
          console.log(goodIds)
          var params={
            ids: goodIds.join(","),
            state:1
          }
          netUtil.requestLoading(app.globalData.baseUrl + "/front/updateGoodsByIds", params, '加载中', function (res) {
            //res就是我们请求接口返回的数据
            console.log(res)
            if (res.code == '100') {
              wx.showToast({
                title: '成功',
              })
              that.onShow()
            } else {
              wx.showToast({
                title: res.msg
              })
            }
          }, function () {
            wx.showToast({
              title: '加载失败',
            })
          })
        }else{
          var params = {
            ids: goodIds.join(","),
            state: 4
          }
          netUtil.requestLoading(app.globalData.baseUrl + "/front/updateGoodsByIds", params, '加载中', function (res) {
            //res就是我们请求接口返回的数据
            console.log(res)
            if (res.code == '100') {
              wx.showToast({
                title: '成功',
              })
              that.onShow()
            } else {
              wx.showToast({
                title: res.msg
              })
            }
          }, function () {
            wx.showToast({
              title: '加载失败',
            })
          })
        }
      }
    })
    
  },
  //数量变化处理
  handleQuantityChange(e) {
    var componentId = e.componentId;
    var quantity = e.quantity;
    this.data.carts[componentId].count.quantity = quantity;
    this.setData({
      carts: this.data.carts,
    });
  },
  /* 减数 */
  delCount: function (e) {
    var index = e.target.dataset.index;
    console.log("刚刚您点击了加一");
    var count = this.data.carts[index].count;
    // 商品总数量-1
    if (count > 1) {
      this.data.carts[index].count--;
    }
    // 将数值与状态写回  
    this.setData({
      carts: this.data.carts
    });
    console.log("carts:" + this.data.carts);
    this.priceCount();
  },
  /* 加数 */
  addCount: function (e) {
    var index = e.target.dataset.index;
    console.log("刚刚您点击了加+");
    var count = this.data.carts[index].count;
    // 商品总数量+1  
    if (count < 10) {
      this.data.carts[index].count++;
    }
    // 将数值与状态写回  
    this.setData({
      carts: this.data.carts
    });
    console.log("carts:" + this.data.carts);
    this.priceCount();
  },
  priceCount: function (e) {
    this.data.totalMoney = 0;
    for (var i = 0; i < this.data.carts.length; i++) {
      if (this.data.carts[i].isSelect == true) {
        this.data.totalMoney = this.data.totalMoney + (this.data.carts[i].price * this.data.carts[i].count);
      }

    }
    this.setData({
      totalMoney: this.data.totalMoney,
    })
  },
  /* 删除item */
  delGoods: function (e) {
    this.data.carts.splice(e.target.id.substring(3), 1);
    // 更新data数据对象  
    if (this.data.carts.length > 0) {
      this.setData({
        carts: this.data.carts
      })
      wx.setStorageSync('cart', this.data.carts);
      this.priceCount();
    } else {
      this.setData({
        cart: this.data.carts,
        iscart: false,
        hidden: true,
      })
      wx.setStorageSync('cart', []);
    }
  },
  switchNav(e) {  //点击 这个方法会触发bindChange()方法
    let isSelect = e.target.dataset.current;
    let that = this
    var arr = []
    var state = that.data.goodState[isSelect]
    that.setData({
      state: state
    })
    that.setData({
      goodIds: []
    })
    var params = {
      appId: app.globalData.appId,
      state: that.data.goodState[isSelect],
      pageNo: 1,
      pageSize: 5
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/front/getAllGoodsByState", params, '加载中', function (res) {
      //res就是我们请求接口返回的数据
      console.log(res)
      if (res.code == '100') {
        arr = res.info
        that.setData({
          carts: arr,
        });
      } else {
        wx.showToast({
          title: res.msg
        })
      }
    }, function () {
      wx.showToast({
        title: '加载失败',
      })
    })
    this.setData({
      currentTab: isSelect,
    })

  },
  //加载更多
  onReachBottom: function () {
    //接口获取数据
    let that = this
    var page = that.data.page+1
    that.setData({
      page: page
    });
    var arr = []
    var params = {
      appId: app.globalData.appId,
      state: that.data.state,
      pageNo: page,
      pageSize: 5
    }
    if(that.data.isMore){
      netUtil.requestLoading(app.globalData.baseUrl + "/front/getAllGoodsByState", params, '加载中', function (res) {
        //res就是我们请求接口返回的数据
        console.log(res)
        if (res.code == '100') {
          var arr = that.data.carts
          if (res.info.length==0){
            that.setData({
              isMore:false
            })
          }
          for (var i = 0; i < res.info.length; i++) {
            arr.push(res.info[i])
          }
          that.setData({
            carts: arr
          });
        } else {
          wx.showToast({
            title: res.msg
          })
        }
      }, function () {
        wx.showToast({
          title: '加载失败',
        })
      })
    }
  
  }
})