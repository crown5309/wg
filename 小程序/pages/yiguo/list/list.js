
var app = getApp();
var netUtil = require("../../../utils/api.js"); //require引入
const utils = require('../../utils/util.js');
var sectionData = [];
var ifLoadMore = true;
var classifyId = null;
var page = 1;//默认第一页
Page({

  data: {
    scrollH: 0,
    imgWidth: 0,
    loadingCount: 0,
    images: [],
    col1: [],
    col2: [],
    goodsWelfareItems:[],
    page:0,
    isMore:true,
    classId:""
  },

  onLoad: function (options) {
    this.setData({
      classId: options.id
    }) 
    ifLoadMore = true;
    console.log('classifyId:' + classifyId);
    wx.getSystemInfo({
      success: (res) => {
        let ww = res.windowWidth;
        let wh = res.windowHeight;
        let imgWidth = ww * 0.48;
        let scrollH = wh;

        this.setData({
          scrollH: scrollH,
          imgWidth: imgWidth
        });

        //加载首组图片
        // this.loadImages();
        this.brandShow();
      }
    })
  },

  onImageLoad: function (e) {
    let imageId = e.currentTarget.id;
    let oImgW = e.detail.width;         //图片原始宽度
    let oImgH = e.detail.height;        //图片原始高度
    let imgWidth = this.data.imgWidth;  //图片设置的宽度
    let scale = imgWidth / oImgW;        //比例计算
    let imgHeight = oImgH * scale;      //自适应高度

    let images = this.data.brandGoods;
    let imageObj = null;

    for (let i = 0; i < images.length; i++) {
      let img = images[i];
      if (img.id + "" === imageId) {
        imageObj = img;
        break;
      }
    }

    imageObj.height = imgHeight;

    let loadingCount = this.data.loadingCount - 1;
    let col1 = this.data.col1;
    let col2 = this.data.col2;

    //判断当前图片添加到左列还是右列
    if (col1.length <= col2.length) {
      col1.push(imageObj);
    } else {
      col2.push(imageObj);
    }

    let data = {
      loadingCount: loadingCount,
      col1: col1,
      col2: col2
    };

    //当前这组图片已加载完毕，则清空图片临时加载区域的内容
    if (!loadingCount) {
      data.images = [];
    }

    this.setData(data);
  },

  brandShow: function (success) {
    console.log(page)
    //接口获取数据
    let that = this
    var page = that.data.page + 1
    that.setData({
      page: page
    });
    var arr = []
    var params = {
      appId: app.globalData.appId,
      state: 2,
      pageNo: page,
      pageSize: 6,
      classId:that.data.classId
    }
    if (that.data.isMore) {
      netUtil.requestLoading(app.globalData.baseUrl + "/front/getAllGoodsByState", params, '加载中', function (res) {
        //res就是我们请求接口返回的数据
        console.log(res)
        if (res.code == '100') {
          var arr = that.data.goodsWelfareItems
          if (res.info.length == 0) {
            that.setData({
              isMore: false
            })
          }
          for (var i = 0; i < res.info.length; i++) {
            arr.push(res.info[i])
          }
          that.setData({
            goodsWelfareItems: arr
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
  },
  //加载更多
  onReachBottom: function () {
    this.brandShow()
  },
  catchTapCategory: function (e) {
    var that = this;
    var goodsId = e.currentTarget.dataset.id;
    console.log('goodsId:' + goodsId);
    //新增商品用户点击数量
    // that.goodsClickShow(goodsId);
    //跳转商品详情
    wx.navigateTo({ url: '../detail/detail?goodsId=' + goodsId })
  },
  goodsClickShow(goodsId) {
    console.log('增加商品用户点击数量');
    var that = this;
    ajax.request({
      method: 'GET',
      url: 'goods/addGoodsClickRate?key=' + utils.key + '&goodsId=' + goodsId,
      success: data => {
        console.log("用户点击统计返回结果：" + data.message)
      }
    })
  }
})
