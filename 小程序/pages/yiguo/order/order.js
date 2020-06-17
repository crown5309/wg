Page({
  data: {
    nodataType: 7,
    orderList: [],    //订单列表数据，接口获取
    currentPage: 1,
    isNoMoreData: false,
    orderState: null,
    winHeight: 900,
    currentTab: 0,     //当前显示tab的下标
    navTab: ['全部', '待付款', '已完成', '退款'],
    loading: true,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.initData(1);    //获取数据的方法
  },
  initData(currentPage) {

    //写你自己的接口

  },
  switchNav(e) {  //点击 这个方法会触发bindChange()方法
    let isSelect = e.target.dataset.current;
    this.setData({
      currentTab: isSelect,

    })
  },

  bindChange(e) {    //切换swiper
    let isSelect = e.detail.current;

    if (isSelect != 0) {
      this.setData({
        orderState: isSelect
      })
    }
    else {
      this.setData({
        orderState: null
      })
    }
    this.setData({
      isNoMoreData: false,
      loading: true,
      currentTab: isSelect,
      orderList: []
    })
    this.initData(1)
  },
  toDetail(val) {
    console.log(val.detail)
    let obj = JSON.stringify(val.detail);
    wx.navigateTo({
      url: '../orderDetail/orderDetail?item=' + encodeURIComponent(obj)
    })
  },

  /**
 * 生命周期函数--监听页面初次渲染完成
 */
  onReady: function () {   //获取设备高度
    let _this = this;
    wx.getSystemInfo({
      success: function (res) {
        // console.log(res.windowWidth);
        // console.log(res.windowHeight);
        _this.setData({
          winHeight: res.windowHeight
        })
      },
    })

  },
  /**
  * 页面上拉触底事件的处理函数
  */
  onReachBottom: function () {    //上拉加载分页
    this.setData({
      loading: true
    })
    if (!this.data.isNoMoreData && this.data.orderList.length > 0) {
      this.initData(++this.data.currentPage);
    }
  },
})