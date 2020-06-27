
var xsplb = [
  { SPLB_ID: 'all', SPLB_MC: '全部商品', SPLB_YS: true },
  { SPLB_ID: '0001', SPLB_MC: '商品类别一', SPLB_YS: false },
  { SPLB_ID: '0002', SPLB_MC: '商品类别二', SPLB_YS: false },
  { SPLB_ID: '0003', SPLB_MC: '商品类别三', SPLB_YS: false },
]

var xbanner = [
  { b_selected: true, b_type: '综合', icon1: true, b_src1: '', icon2: true, b_src2: '' },
  { b_selected: false, b_type: '销量', icon1: true, b_src1: '', icon2: true, b_src2: '' },
  { b_selected: false, b_type: '新品', icon1: true, b_src1: '', icon2: true, b_src2: '' },
  { b_selected: false, b_type: '价格', icon1: false, b_src1: '../../../image/jg_up.png', icon2: false, b_src2: '../../../image/jg_down.png', jgsx: false },
]

Page({
  data: {
    spxx:'',
    splb: xsplb,
    banner: xbanner,
    pxgz:'zh',
    open: false,
    left:false,
    right:false,
    jgmin:'',jgmax:'',
    istoright: true,
    imgWidth: 0,
    loadingCount: 0,
    images: [],
    col1: [],
    col2: [],
  },
  onLoad:function(option){
    wx.getSystemInfo({
      success: (res) => {
        let ww = res.windowWidth;
        let imgWidth = ww * 0.48;
        this.setData({
          imgWidth: imgWidth
        });
        //加载首组图片
        this.onReachBottom();
      }
    })
  },
  onReady: function () {
    //获得dialog组件
    this.dialog = this.selectComponent("#dialog");
  },
  onPullDownRefresh: function () {
    this.setData({
      col1: [],
      col2: []
    })
    this.onLoad();
    wx.stopPullDownRefresh();
  },
  tap_cover:function(){
    btnCover('', this);
  },
  // 点击左上角小图标事件
  tap_ch: function (e) {
    var lr = 'left';
    btnCover(lr,this);
  },
  // 商品类别
  tap_splb:function(e){
    btnCover('',this);
    btnSplb(e,this);
  },
  // 商品信息
  spxx_input:function(e){this.setData({spxx:e.detail.value})},
  // 搜索
  tap_search:function(e){
    console.log(this.data.spxx);
  },
  // 排序规则
  tap_banner: function (e) {
    btnBanner(e, this);
  },
  // 筛选
  tap_sx:function(e){
    var lr = 'right';
    btnCover(lr,this);
  },
  // 重置
  tap_cz:function(e){
    this.setData({
      jgmin:'',jgmax:''
    })
  },
  // 价格区间
  min_input: function (e) { this.setData({ jgmin: e.detail.value }) },
  max_input: function (e) { this.setData({ jgmax: e.detail.value }) },
  tap_confirm:function(e){
    console.log(this.data.jgmin);
    console.log(this.data.jgmax);
    btnCover('', this);
  },
  //加入购物车
  tap_jrgwc:function(e){
    var lr = 'buy';
    this.setData({
      buy_imageurl:e.currentTarget.dataset.url,
      buy_name: e.currentTarget.dataset.name,
      buy_price: e.currentTarget.dataset.price
    });
    btnCover(lr, this);
  },
  //关闭订单弹出框
  _closeEvent:function(){
    btnCover('', this);
  },
  onImageLoad: function (e) {
    let imageId = e.currentTarget.id;
    let oImgW = e.detail.width;         //图片原始宽度
    let oImgH = e.detail.height;        //图片原始高度
    let imgWidth = this.data.imgWidth;  //图片设置的宽度
    let scale = imgWidth / oImgW;        //比例计算
    let imgHeight = oImgH * scale;      //自适应高度
    let images = this.data.images;
    let imageObj = null;

    for (let i = 0; i < images.length; i++) {
      let img = images[i];
      if (img.id === imageId) {
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
  onReachBottom: function () {
    let images = [
      {
        goodId: 0,
        name: '泊尔崖蜜蜜光面膜（5片盒装）',
        url: 'bill',
        imageurl: 'https://a3.vimage1.com/upload/merchandise/pdcvis/2017/08/21/142/fb2960bf8e074d029c24315279289c19-5_218x274_70.jpg',
        newprice: "86",
        oldprice: "88",
        discount: "8.8",
        height: 0,
      },
      {
        goodId: 1,
        name: '透无瑕矿物养护两用粉饼#03',
        url: 'bill',
        imageurl: 'https://a.appsimg.com/upload/brand/upcb/2017/10/26/77/ias_150899004322921_604x290_80.jpg!75.webp',
        newprice: "147.00",
        height: 0,
      },
      {
        goodId: 2,
        name: '川水水光面膜（5片盒装）',
        url: 'bill',
        imageurl: 'https://a2.vimage1.com/upload/merchandise/pdcvis/2017/08/21/86/7891361fdab348a1bc91aeca31fc77b1-5_218x274_70.jpg',
        newprice: "86.00",
        height: 0,
      },
      {
        goodId: 3,
        name: '蜜三色渐变咬唇膏3.2g 03蜜橙动心恋',
        url: 'bill',
        imageurl: 'http://a3.vimage1.com/upload/merchandise/pdcvis/2017/08/21/176/c3b9453a4d7f46c6a8fe78705f77352b-5_218x274_70.jpg',
        newprice: "97.00",
        height: 0,
      },
      {
        goodId: 4,
        name: '时焕颜亮采套装',
        url: 'bill',
        imageurl: 'https://a2.vimage1.com/upload/merchandise/pdcvis/2017/08/21/93/69a6bc1c11eb4be184b7dffb43b8565b-5_218x274_70.jpg',
        newprice: "398.00",
        height: 0,
      }
    ];

    let baseId = "img-" + (+new Date());

    for (let i = 0; i < images.length; i++) {
      images[i].id = baseId + "-" + i;
    }

    this.setData({
      loadingCount: images.length,
      images: images
    });
  },
})
// 蒙版
var btnCover = function(lr,that){
  if (that.data.open) {
    that.setData({
      open: false,
      left:false,
      right:false
    });
    that.dialog.hideDialog();
  } else {
    that.setData({
      open: true
    });
    if(lr === 'left'){
      that.setData({
        left:true
      });
    }else if(lr === 'right'){
      that.setData({
        right: true
      });
    } else if (lr === 'buy'){
      that.dialog.showDialog();
    }
  }
}
// 分类颜色切换
var btnSplb = function(e,that){
  var xindex = e.currentTarget.dataset.index;
  var xsplb = that.data.splb;
  for (var i = 0; i < xsplb.length; i++) {
    xsplb[i].SPLB_YS = false;
  }
  that.setData({
    splb: xsplb
  })
  xsplb[xindex].SPLB_YS = true;
  // 设置导航栏标题
  wx.setNavigationBarTitle({
    title: that.data.splb[xindex].SPLB_MC,
  })
  that.setData({
    splb: xsplb
  })
}
// 排序规则切换
var btnBanner = function (e, that) {
  var xindex = e.currentTarget.dataset.index;
  var xbanner = that.data.banner;
  var xpxgz = '';
  var s = xbanner[xindex].jgsx;
  for (var i = 0; i < xbanner.length; i++) {
    xbanner[i].b_selected = false;
  }
  that.setData({
    banner: xbanner
  })
  xbanner[xindex].b_selected = true;
  if(xindex === 0){
    xpxgz = 'zh';
    xbanner[3].b_src1 = '../../../image/jg_up.png';
    xbanner[3].b_src2 = '../../../image/jg_down.png';
  }else if(xindex === 1){
    xpxgz = 'xl';
    xbanner[3].b_src1 = '../../../image/jg_up.png';
    xbanner[3].b_src2 = '../../../image/jg_down.png';
  }else if(xindex === 2){
    xpxgz = 'xp';
    xbanner[3].b_src1 = '../../../image/jg_up.png';
    xbanner[3].b_src2 = '../../../image/jg_down.png';
  }else if (xindex === 3 && !s){
    xpxgz = 'cddg';
    xbanner[xindex].jgsx = true;
    xbanner[xindex].b_src1 = '../../../image/jg_up1.png';
    xbanner[xindex].b_src2 = '../../../image/jg_down.png';
  } else if (xindex === 3 && s){
    xpxgz = 'cgdd';
    xbanner[xindex].jgsx = false;
    xbanner[xindex].b_src1 = '../../../image/jg_up.png';
    xbanner[xindex].b_src2 = '../../../image/jg_down1.png';
  }
  that.setData({
    banner: xbanner,
    pxgz:xpxgz
  })
}