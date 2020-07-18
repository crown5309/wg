var netUtil = require("../../../utils/api.js"); //require引入
const app = getApp();
Page({
  data: {
    painting: {},
    shareImage: '',
    navbar: ['首页'],
    navbarArray:[],
    currentTab: 0,
    indicatorDots: true, //设置是否显示面板指示点
    autoplay: true, //设置是否自动切换
    interval: 3000, //设置自动切换时间间隔,3s
    duration: 1000, //  设置滑动动画时长1s,
    menus:[],
    showBanner:true,
    classId:'',
    imgUrls: [],
    page:0,
    // 实时热销榜
    goodsHotItems: [
      {
        goodId: 0,
        name: '天然植物唇膏2.4g',
        url: 'bill',
        imageurl: 'https://a3.vimage1.com/upload/merchandise/pdc/272/707/8870197248301707272/2/6922446703595-5_360x456_90.jpg',
        newprice: "32.00",
        oldprice: "59.00",
      },
      {
        goodId: 1,
        name: '流光水彩唇膏升级版 3.5g',
        url: 'bill',
        imageurl: 'https://a3.vimage1.com/upload/merchandise/pdcvis/2017/08/03/8/4fb444e7-3417-4f4a-b5a1-7f1d884c610f_218x274_70.jpg',
        newprice: "89.00",
        oldprice: "99.00",
      },
      {
        goodId: 2,
        name: '卡姿兰蜗牛氧气泡泡面膜',
        url: 'bill',
        imageurl: 'https:////a3.vimage1.com/upload/merchandise/pdcvis/2017/08/24/167/28c3726f-dfdd-4a59-89ac-b79ea8e24f40_218x274_70.jpg',
        newprice: "139.00",
        oldprice: "159.00",
      },
      {
        goodId: 3,
        name: '特效润肤露',
        url: 'bill',
        imageurl: 'http://mz.djmall.xmisp.cn/files/product/20161201/14805828016.jpg',
        newprice: "30.00",
        oldprice: "80.00",
      },
      {
        goodId: 4,
        name: '御泥坊 | 美白嫩肤泥浆...',
        url: 'bill',
        imageurl: 'https://a4.vimage1.com/upload/merchandise/pdcvis/2017/11/03/98/f34a6c251abf45e5ba60a645f13c7757-5.jpg',
        newprice: "79.00",
        oldprice: "80.00",
      }, {
        goodId: 5,
        name: '日本资生堂洗颜',
        url: 'bill',
        imageurl: 'http://mz.djmall.xmisp.cn/files/product/20161201/148058328876.jpg',
        newprice: "30.00",
        oldprice: "80.00",
      }
      , {
        goodId: 6,
        name: '玉兰油 | 水感透白光塑钻...',
        url: 'bill',
        imageurl: 'https://a3.vimage1.com/upload/merchandise/pdcvis/2017/05/19/2/e5de903ab5ba4a6492f3574469fdfca9-5.jpg',
        newprice: "145.00",
        oldprice: "324.00",
      }
    ],
    // 福利专场
    goodsWelfareItems: [ ]
  },

  // 导航切换监听
  navbarTap: function (e) {
    
    console.log(e.currentTarget.dataset.idx);
    if(e.currentTarget.dataset.idx>=1){
      this.setData({
        currentTab: e.currentTarget.dataset.idx,
        menus:this.data.navbarArray[e.currentTarget.dataset.idx-1].classsesList,
        showBanner:false,
        classId: this.data.navbarArray[e.currentTarget.dataset.idx - 1].classId
      })
    }else{
      this.setData({
        currentTab: e.currentTarget.dataset.idx,
        menus:[],
        showBanner:true,
        page:0
      })
    }
    this.setData({ 
      goodsWelfareItems:[],
      page:0
      })
    this.getGoodsList()
  },
  //加载更多
  onReachBottom: function () {
    this.getGoodsList()
  },
  eventGetImage(event) {
    console.log(event)
    wx.hideLoading()
    const { tempFilePath, errMsg } = event.detail
    if (errMsg === 'canvasdrawer:ok') {
      this.setData({
        shareImage: tempFilePath
      })
    }
  },
  onShow:function(){
   
    let that=this
    that.setData({navbar:['首页']})
    var params={
      appId:app.globalData.appId,
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/index/listIndexAll", params, '加载中', function (res) {
      //res就是我们请求接口返回的数据
      if (res.code == '100') {
        
        that.setData({
          imgUrls:res.info.bannerList,
          navbarArray:res.info.classList,
        })
        var array=that.data.navbar
       for(var i=0;i<that.data.navbarArray.length;i++){
        array.push(that.data.navbarArray[i].name);
       }
       that.setData({
        navbar:array,
      })
        that.getGoodsList()
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
  ToSearchResult:function(e){
    var id=e.currentTarget.dataset.id
    console.log(id)
     wx.navigateTo({
       url: '../list/list?id='+id
     })
  },
  getGoodsList:function(){
    let that = this
    var page=this.data.page+1
    var params = {
      appId: app.globalData.appId,
      classId:this.data.classId,
      pageNo: page,
      pageSize:6
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/index/listIndexGoods", params, '加载中', function (res) {
      //res就是我们请求接口返回的数据
      if (res.code == '100') {
        var array = that.data.goodsWelfareItems
    
        for (var i = 0; i < res.info.length;i++){
          array.push(res.info[i])
        }
        that.setData({
          goodsWelfareItems: array,
          page: page
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
  },
  wxSerchFocus:function(){
    wx.navigateTo({
      url: '../list/list?id=&foucs=true'
    })
  },
  toGo: function (e) {
    var id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: '../detail/detail?goodsId=' + id
    })
  }
 
})
