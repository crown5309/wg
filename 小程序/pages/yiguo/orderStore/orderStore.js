var netUtil = require("../../../utils/api.js"); //require引入
var app = getApp();
Page({
  data: {
    nodataType: 7,
    orderList: [],    //订单列表数据，接口获取
    currentPage: 1,
    isNoMoreData: false,
    orderState: null,
    winHeight: 900,
    currentTab: 0,     //当前显示tab的下标
    navTab: ['全部','待付款', '待发货', '已完成', '退款'],
    state:['',1,2,5,10],
    loading: true,
    origin:"JDCOM",
    pageNo:0,
    showModalStatus: false,
    casArray: [],
    casIndex: 0,
    casArrayList:[],
    logisticsNo:"",
    logisticsType:'',
    orderId:""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var currentTab = this.data.currentTab
    this.initData(currentTab);    //获取数据的方法
    this.getwuLiu()
  },
  getwuLiu:function(){
    let that=this
    netUtil.requestLoading(app.globalData.baseUrl + "/area/listWuLiu", {}, '加载中', function (res) {
      //res就是我们请求接口返回的数据
      if (res.code == '100') {
        var array=res.info
        var casArray1=[]
        that.setData({
          casArray:[]
        }) 
        for(var i=0;i<array.length;i++){
          casArray1.push(array[i].label)
        }
        that.setData({
          casArray: casArray1,
          casArrayList: array,
          logisticsType: array[0].value
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
  },
  initData(currentPage) {

    //写你自己的接口
    var that = this;
    var pageNo = that.data.pageNo+1;
    var params = {
      state: that.data.state[currentPage],
      pageNo: pageNo,
      pageSize:5,
      type:'store'
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/front/getOrderInfoByState", params, '加载中', function (res) {
      //res就是我们请求接口返回的数据
      if (res.code == '100') {
        var arr = that.data.orderList
        if (arr.length == 0 || pageNo==1){
          arr = res.info
        }else{
          if (res.info.length == 0) {
            that.setData({
              isNoMoreData:true
            })
          }else{
            for (var i = 0; i < res.info.length; i++) {
              arr.push(res.info[i])
            }
          }
         
         
        }
       
        that.setData({
          orderList: arr,
          pageNo: pageNo
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
  },
  switchNav(e) {  //点击 这个方法会触发bindChange()方法
    let isSelect = e.target.dataset.current;
    this.setData({
      currentTab: isSelect,
      pageNo:0,
      orderList:[] 

    })
  },

  bindChange(e) {    //切换swiper
    let isSelect = e.detail.current;
    this.initData(isSelect)
  },
  toDetail(e) {
    wx.navigateTo({
      url: '../orderDetail/orderDetail?orderIds=' + this.data.orderList[e.currentTarget.dataset.id].orderId + "&addressId=" + this.data.orderList[e.currentTarget.dataset.id].addressId
    })
  },
  bindCasPickerChange: function (e) {
    this.setData({
      casIndex: e.detail.value,
      logisticsType: this.data.casArrayList[e.detail.value].value
    })

  },
  //输入出发地点
  inputVillage(e) {
    this.setData({
      logisticsNo: e.detail.value,
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
    console.log(this.data.isNoMoreData)
    if (!this.data.isNoMoreData) {
      this.setData({
        loading: true
      })
      this.initData(this.data.currentPage);
    }
  },
  powerDrawer: function (e) {
    var currentStatu = e.currentTarget.dataset.statu;
    this.setData({
      orderId: e.currentTarget.dataset.id,
      logisticsNo:""
    })
    this.util(currentStatu)
    //请求接口
  },
  powerDrawer1: function (e) {
    let that=this
    var params={
      logisticsNo: that.data.logisticsNo,
      logisticsType: that.data.logisticsType,
      orderId: that.data.orderId,
    }
    if (that.data.logisticsNo==''){
      wx.showToast({
        title: '请输入快递单号',
        icon: "none",
        duration: 1000,
        mask: true,
      })
      return
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/area/updateOrderByOrderId", params, '加载中', function (res) {
      //res就是我们请求接口返回的数据
      if (res.code == '100') {
        wx.showToast({
          title: '发货成功',
          icon: "none",
          duration: 1000,
          mask: true,
        })
        var currentStatu = e.currentTarget.dataset.statu;
        that.util(currentStatu)
        that.setData({ pageNo:0})
        that.initData(that.data.currentTab)
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
    //请求接口
  },
  util: function (currentStatu) {
    /* 动画部分 */
    // 第1步：创建动画实例  
    var animation = wx.createAnimation({
      duration: 200, //动画时长 
      timingFunction: "linear", //线性 
      delay: 0 //0则不延迟 
    });

    // 第2步：这个动画实例赋给当前的动画实例 
    this.animation = animation;

    // 第3步：执行第一组动画 
    animation.opacity(0).rotateX(-100).step();

    // 第4步：导出动画对象赋给数据对象储存 
    this.setData({
      animationData: animation.export()
    })

    // 第5步：设置定时器到指定时候后，执行第二组动画 
    setTimeout(function () {
      // 执行第二组动画 
      animation.opacity(1).rotateX(0).step();
      // 给数据对象储存的第一组动画，更替为执行完第二组动画的动画对象 
      this.setData({
        animationData: animation
      })

      //关闭 
      if (currentStatu == "close") {
        this.setData(
          {
            showModalStatus: false
          }
        );
      }
    }.bind(this), 200)

    // 显示 
    if (currentStatu == "open") {
      this.setData(
        {
          showModalStatus: true
        }
      );
    } }
})