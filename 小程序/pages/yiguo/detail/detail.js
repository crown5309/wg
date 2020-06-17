// pages/detail/detail.js
var app = getApp();
var netUtil = require("../../../utils/api.js"); //require引入
var imgUrls = [];
var detailImg = [];
var goodsId = null;
var goods = null;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isLike: false,
    showDialog: false,
    shareDialog:false,
    goods: null,
    indicatorDots: true, //是否显示面板指示点
    autoplay: true, //是否自动切换
    interval: 3000, //自动切换时间间隔,3s
    duration: 1000, //  滑动动画时长1s,
    param:"",//car buy 加入购物车，购买
    // canvas 
    _width: 0, //手机屏宽
    _heigth: 0,//手机屏高
    swiperHeight: 300,//主图图片高度
    canvasType: false,//canvas是否显示
    loadImagePath: '',//下载的图片
    imageUrl: 'https://cos.myfaka.com/car/service/1.jpg', //主图网络路径
    codeUrl: 'https://cos.myfaka.com/car/share/code.jpg',//二维码网络路径
    localImageUrl: '', //绘制的商品图片本地路径
    localCodeUrl: '', //绘制的二维码图片本地路径
  },
  //预览图片
  previewImage: function (e) {
    var current = e.target.dataset.src;
    wx.previewImage({
      current: current, // 当前显示图片的http链接  
      urls: this.data.imgUrls // 需要预览的图片http链接列表  
    })
  },
  // 收藏
  addLike() {
    this.setData({
      isLike: !this.data.isLike
    });
    ajax.request({
      method: 'GET',
      url: 'collection/addShopCollection?key=' + utils.key + '&goodsId=' + goodsId,
      success: data => {
        console.log("收藏返回结果：" + data.message)
        wx.showToast({
          title: data.message,
          icon: 'success',
          duration: 2000
        });
      }
    })
  },
  // 跳到购物车
  toCar() {
    wx.switchTab({ url: '../cart1/cart1' })
  },
  // 立即购买
  immeBuy() {
    this.setData({
      showDialog: !this.data.showDialog,
      param: "buy"
    });
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    goodsId = options.goodsId;
    console.log('goodsId:' + goodsId);
    //加载商品详情
    that.goodsInfoShow();
  },
  goodsInfoShow: function (success) {
    var that = this;
    var params={
      goodsId: goodsId
    }
    netUtil.requestLoading(app.globalData.baseUrl + "/front/getGoodsById", params, '加载中', function (res) {
      //res就是我们请求接口返回的数据
      console.log(res)
      if (res.code == '100') {
        var arr = res.info
        arr.count=1
        arr.totalMoney=arr.price
        that.setData({
          goods: arr
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
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  /**
   * sku 弹出
   */
  toggleDialog: function () {
    this.setData({
      showDialog: !this.data.showDialog,
      param:"car"
    });
  },
  shareDialog: function () {
    this.setData({
      shareDialog: !this.data.shareDialog,
      param: "car"
    });
  },
  /**
   * sku 关闭
   */
  closeDialog: function () {
    console.info("关闭");
    this.setData({
      showDialog: false
    });
  },
  /* 减数 */
  delCount: function (e) {
    console.log("刚刚您点击了减1");
    var count = this.data.goods.count;
    // 商品总数量-1
    if (count > 1) {
      this.data.goods.count--;
    }
    // 将数值与状态写回  
    this.setData({
      goods: this.data.goods
    });
    this.priceCount();
  },
  /* 加数 */
  addCount: function (e) {
    console.log("刚刚您点击了加1");
    var count = this.data.goods.count;
    // 商品总数量-1  
    if (count < 10) {
      this.data.goods.count++;
    }
    // 将数值与状态写回  
    this.setData({
      goods: this.data.goods
    });
    this.priceCount();
  },
  //价格计算
  priceCount: function (e) {
    this.data.goods.totalMoney = this.data.goods.price * this.data.goods.count;
    this.setData({
      goods: this.data.goods
    })
  },
  /**
   * 加入购物车
   */
  addCar: function (e) {
    if(this.data.param=='buy'){//购买
        //下单
      var that = this;
      var params = {
        goodsId: goodsId,
        count: that.data.goods.count
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
     
    }else{//加入购物车
      var goods = this.data.goods;
      goods.isSelect = false;
      var count = this.data.goods.count;

      var title = this.data.goods.goodsName;
      if (title.length > 13) {
        goods.goodsName = title.substring(0, 13) + '...';
      }

      // 获取购物车的缓存数组（没有数据，则赋予一个空数组）  
      var arr = wx.getStorageSync('cart') || [];
      console.log("arr,{}", arr);
      if (arr.length > 0) {
        // 遍历购物车数组  
        for (var j in arr) {
          // 判断购物车内的item的id，和事件传递过来的id，是否相等  
          if (arr[j].goodsId == goodsId) {
            // 相等的话，给count+1（即再次添加入购物车，数量+1）  
            arr[j].count = arr[j].count + 1;
            // 最后，把购物车数据，存放入缓存（此处不用再给购物车数组push元素进去，因为这个是购物车有的，直接更新当前数组即可）  
            try {
              wx.setStorageSync('cart', arr)
            } catch (e) {
              console.log(e)
            }
            //关闭窗口
            wx.showToast({
              title: '加入购物车成功！',
              icon: 'success',
              duration: 2000
            });
            this.closeDialog();
            // 返回（在if内使用return，跳出循环节约运算，节约性能） 
            return;
          }
        }
        // 遍历完购物车后，没有对应的item项，把goodslist的当前项放入购物车数组  
        arr.push(goods);
      } else {
        arr.push(goods);
      }
      // 最后，把购物车数据，存放入缓存  
      try {
        wx.setStorageSync('cart', arr)
        // 返回（在if内使用return，跳出循环节约运算，节约性能） 
        //关闭窗口
        wx.showToast({
          title: '加入购物车成功！',
          icon: 'success',
          duration: 2000
        });
        this.closeDialog();
        return;
      } catch (e) {
        console.log(e)
      }
    }
  },
  /*按生成图片按钮时*/
  creatQrcodePictures: function () {
    wx.showLoading({
      title: '正在绘制图片',
    })
    /*获取手机宽高*/
    let that = this
    let imgHeigth = this.data.swiperHeight
    let imgUrl = this.data.imageUrl
    let qrcodeUrl = this.data.codeUrl
    wx.getSystemInfo({
      success(res) {
        that.setData({
          _width: res.windowWidth,
          _heigth: res.windowHeight,
          canvasType: true,
        })
        // 获取图片信息生成canvas
        that.getImginfo([imgUrl, qrcodeUrl], 0);
      }
    })
  },
  // 获取图片信息
  getImginfo: function (urlArr, _type) {
    debugger
    let that = this;
    wx.getImageInfo({
      src: urlArr[_type],
      success: function (res) {
        //res.path是网络图片的本地地址
        if (_type === 0) { //商品图片
          that.setData({
            localImageUrl: res.path,
          })
        //  that.getImginfo(urlArr, 1)
          that.setData({ //二维码
            localCodeUrl: res.path,
          })
          // 创建canvas图片
          that.createNewImg();
        } else {
          that.setData({ //二维码
            localCodeUrl: res.path,
          })
          // 创建canvas图片
          that.createNewImg();
        }
      },
      fail: function (res) {
        //失败回调
        console.log('Fail：', _type, res)
      }
    });
  },
  //绘制canvas
  createNewImg: function () {
    let _width = this.data._width,
      _heigth = this.data._heigth; //屏幕宽与高

    let imgHeigth = this.data.swiperHeight, //原图片高度
      scale = (_width - 40) / _width, //缩小比例
      that = this;
    let imgH = imgHeigth * scale; //绘制时图片显示高度  
    let ctx = wx.createCanvasContext('mycanvas');
    // 绘制背景
    ctx.setFillStyle("#fff");
    ctx.fillRect(0, 0, _width - 40, imgH + 160);
    //绘制图片
    ctx.drawImage(this.data.localImageUrl, 10, 10, _width - 60, imgH);

    // 绘制标题
    ctx.setFontSize(18);
    ctx.setFillStyle('#333');

    let txtWidth = _width - 60 + 30 - 100 - 50; //文字的宽度

    //商品名称
    ctx.fillText('汽车服务：白金蜡', 10, imgH + 40, txtWidth);
    // 绘制价格单位 '￥'
    ctx.setFontSize(14);
    ctx.setFillStyle('#d2aa68');
    ctx.fillText('￥', 10, imgH + 65, txtWidth);
    // 绘制价格
    ctx.setFontSize(18);
    ctx.fillText('90元/次', 26, imgH + 65, txtWidth);
    // 绘制小程序名称
    ctx.setFontSize(20);
    ctx.setFillStyle('red');
    ctx.fillText('武鸣爱车', 10, imgH + 105, txtWidth);
    // 绘制提示信息
    ctx.setFontSize(14);
    ctx.setFillStyle('#999');
    ctx.fillText('微信小程序 • 长按识别', 10, imgH + 125, txtWidth);

    // 绘制二维码
    ctx.drawImage(this.data.localCodeUrl, _width - 80 + 80 - 150, imgH + 20, 100, 100);
    // 显示绘制
    ctx.draw();

    //将生成好的图片保存到本地，需要延迟一会，绘制期间耗时
    setTimeout(function () {
      wx.canvasToTempFilePath({
        canvasId: 'mycanvas',
        success: function (res) {
          var tempFilePath = res.tempFilePath;
          that.setData({
            loadImagePath: tempFilePath,
          });
        },
        fail: function (res) {
          console.log(res);
        }
      });
    }, 500);
    //关闭提示
    wx.hideLoading();
  },
  //点击保存到相册
  saveImg: function () {
    //调用wxapi.js里集成的接口
    app.globalData.wxapi.saveImgToLocal(this.data.loadImagePath);
  },
  // 关闭弹窗
  onClickOverlay: function () {
    this.setData({
      canvasType: false
    });
  },
})