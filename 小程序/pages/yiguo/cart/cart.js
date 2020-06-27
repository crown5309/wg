var netUtil = require("../../../utils/api.js"); //require引入
const app = getApp();
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
  },
  onShow: function () {
    // 获取产品展示页保存的缓存数据（购物车的缓存数组，没有数据，则赋予一个空数组）  
    var arr = wx.getStorageSync('cart') || [];

    let that = this
    var params={
      pageNo:1,
      pageSize:1000
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/front/getMyCartList", params, '加载中', function (res) {
      //res就是我们请求接口返回的数据
      if (res.code == '100') {
        arr=res.info
         // 有数据的话，就遍历数据，计算总金额 和 总数量  
    if (arr.length > 0) {
      // 更新数据  
      that.setData({
        carts: arr,
        iscart: true,
        hidden: false,
        totalMoney:0
      });
      console.info("缓存数据：" + that.data.carts);
    } else {
      that.setData({
        iscart: false,
        hidden: true,
      });
    }
      } else {
        wx.showToast({
          title: res.msg
        })
      }
    }, function () {
      wx.showToast({
        title: '失败',
      })
    })
   
  },
  //勾选事件处理函数  
  switchSelect: function (e) {
    // 获取item项的id，和数组的下标值  
    var Allprice = 0, i = 0;
    let id = e.target.dataset.id,

      index = parseInt(e.target.dataset.index);
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
    })
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
    var ids=[]
    for(var i=0;i<this.data.carts.length;i++){
      if(this.data.carts[i].isSelect==true){
        ids.push(this.data.carts[i].id);
      }
    }
    var params = {
      cartIds: ids.join(","),
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/front/submitOrder", params, '加载中', function (res) {
      //res就是我们请求接口返回的数据
      var orderIds="";
      console.log(res)
      if (res.code == '100') {
        orderIds = res.info
        //支付页面
        wx.navigateTo({
          url: '../pay/pay?orderIds=' + orderIds +"&addressId="
        })
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
    wx.showToast({
      title: '去结算111111111111',
      icon: 'success',
      duration: 3000
    });
    this.setData({
      showDialog: !this.data.showDialog
    });
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
    var params=this.data.carts[index]
    netUtil.requestLoading(app.globalData.baseUrl + "/front/updateCartCountById", params, '加载中', function (res) {
      //res就是我们请求接口返回的数据
      if (res.code == '100') {
      
      } else {
        wx.showToast({
          title: res.msg
        })
      }
    }, function () {
      wx.showToast({
        title: '失败',
      })
    })
  },
  /* 加数 */
  addCount: function (e) {
    var index = e.target.dataset.index;
    console.log("刚刚您点击了加+");
    var count = this.data.carts[index].count;
    // 商品总数量+1  
    if (count < 5000) {
      this.data.carts[index].count++;
    }
    // 将数值与状态写回  
    this.setData({
      carts: this.data.carts
    });
    console.log("carts:" + this.data.carts);
    
    this.priceCount();
    var params=this.data.carts[index]
    netUtil.requestLoading(app.globalData.baseUrl + "/front/updateCartCountById", params, '加载中', function (res) {
      //res就是我们请求接口返回的数据
      if (res.code == '100') {
      
      } else {
        wx.showToast({
          title: res.msg
        })
      }
    }, function () {
      wx.showToast({
        title: '失败',
      })
    })
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
    var params={
      id:e.currentTarget.dataset.id
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/front/deleteMyCartById", params, '加载中', function (res) {
      //res就是我们请求接口返回的数据
      if (res.code == '100') {
        wx.showToast({
          title: '删除成功',
        })
      } else {
        wx.showToast({
          title: res.msg
        })
      }
    }, function () {
      wx.showToast({
        title: '失败',
      })
    })
  }
})