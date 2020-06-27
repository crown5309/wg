// pages/productReleased/productReleased.js
var netUtil = require("../../../utils/api.js"); //require引入
const app = getApp();
Page({
  /**
   * 页面的初始数据
   */
  data: {
    index: 0,
    indexTwo: 0,
    id1:"",
    id2:"",
    id3:"",
    array: [],
    multiArray: [['请选择'], ['请选择'], ['请选择']],
    multiIndex: [0, 0, 0],
    title: "",
    info: "",
    point: "",
    price: "",
    type: [{
      name: "实物",
      id: 0
    }, {
      name: "虚拟",
      id: 1
    }],
    state: [{
      name: "下架",
      id: 0
    }, {
      name: "上架",
      id: 1,
     
    }],
    bannerUrl: [],
    detailUrl: [],
    productID: 0,
    category: [{ title: "ggg", categoryID: 1 }] ,
    categoryInd: -1, //类别
    typeInd: 0, //类型
    stateInd: 1, //状态
    banner: [], //轮播图片
    bannerNew: [],
    bannerAll: [],
    detail: [], //详情图片
    detailNew: [],
    detailAll: [],
    checkUp: true, //判断从编辑页面进来是否需要上传图片
    chooseViewShowDetail: true,
    chooseViewShowBanner: true,
    params: {
      productID: 0,
      contentFile: "",
      bannerFile: "",
      check: false,
    },
    dis: false,
  },
  /**
   * 生命周期函数--监听页面加载
   */
  clickData: function (options) {
    let that = this;
    netUtil.requestLoading(app.globalData.baseUrl + "/front/getAllGoodsClass", app.globalData.sysParam, '数据加载中', function (res) {
      //res就是我们请求接口返回的数据
      if (res.code == '100') {
        var array = res.info;
        that.setData({
          array: array,
        })
        //初始化三级数据
        that.data.multiArray[0] = []
        that.data.multiArray[1] = []
        that.data.multiArray[2] = []
        let id1 = that.data.array[0].screenId
        that.setData({
          id1: id1
        })
        let id2 = that.data.array[0].childrenScreenArray[0].screenId
        that.setData({
          id2: id2
        })
        let id3 = that.data.array[0].childrenScreenArray[0].childrenScreenArray[0].screenId
        that.setData({
          id3: id3
        })
        console.log(that.data.id1)
        for (var i = 0; i < that.data.array.length; i++) {
          that.data.multiArray[0].push(that.data.array[i].screenName)
          if(i==0){
            for (var j = 0; j < that.data.array[0].childrenScreenArray.length; j++) {
              that.data.multiArray[1].push(that.data.array[0].childrenScreenArray[j].screenName)
              if(j==0){
                for (var k = 0; k < that.data.array[0].childrenScreenArray[0].childrenScreenArray.length; k++) {
                  that.data.multiArray[2].push(that.data.array[0].childrenScreenArray[0].childrenScreenArray[k].screenName)
                }
              }
             
            }
          }
       
        }
        console.log(that.data.multiArray)
        that.setData({
          multiArray: that.data.multiArray
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
  /**
   * 获取标题
   */
  titleBlur(e) {
    this.setData({
      title: e.detail.value
    })
  },
  /**
   * 获取商品价格
   */
  priceBlur(e) {
    this.setData({
      price: e.detail.value
    })
  },
  
    /**
   * 获取商品库存
   */
  skuStoreBlur(e) {
    this.setData({
      skuStore: e.detail.value
    })
  },
  /** 
   * 商品价格
   */
  price(e) {
    this.setData({
      price: e.detail.value
    })
  },
  /**
   * 商品类型
   */
  type(e) {
    this.setData({
      typeInd: e.detail.value
    })
  },
  /**
   * 商品状态
   */
  state(e) {
    this.setData({
      stateInd: e.detail.value
    })
  },
  /**
   * 商品类别
   */
  category(e) {
    this.setData({
      categoryInd: e.detail.value
    })
  },
  /**
   * 获取商品类别
   */
  getCategory() {
    let params = {}
    app.getCategoryList(params).then(res => {
      var good = []
      var g_type = res.data.categories[0].children //从接口中获取商品类别
      for (var i = 1; i < g_type.length; i++) { //从下标为1开始循环，不显示全部
        good[i - 1] = g_type[i] //将循环出来的数组，循环放入good中
      }
      this.setData({
        category: good
      })
      if (this.data.productID != 0) { //防止先调用详情方法而不显示类别
        if (res.state === 1) {
          this.getProductDetail();
        }
      }
    })
  },

  /**获取商品详情 */
  getProductDetail() {
    let params = {
      userID: app.globalData.userID,
      productID: this.data.productID
    }
    app.getReleaseProductDetail(params).then(res => {
      let product = res.data.productDetail[0]
      if (product.state) {
        this.setData({
          stateInd: 1
        })
      } else {
        this.setData({
          stateInd: 0
        })
      }

      let categoryInd = -1;
      for (var i = 0; i < this.data.category.length; i++) {
        if (this.data.category[i].categoryID === product.categoryID) {
          categoryInd = i
          break;
        } else {
          categoryInd: -1;
        }
      }

      if (product.bannerImages.length >= 2) {
        this.setData({
          chooseViewShowBanner: false
        })
      } else {
        this.setData({
          chooseViewShowBanner: true
        })
      }

      if (product.detailImages.length >= 3) {
        this.setData({
          chooseViewShowDetail: false
        })
      } else {
        this.setData({
          chooseViewShowDetail: true
        })
      }
      this.setData({
        title: product.title,
        info: product.info,
        point: product.point,
        typeInd: product.productType,
        price: product.currentPrice,
        banner: product.bannerImages,
        detail: product.detailImages,
        categoryInd: categoryInd
      })
    })
  },

  /**发布提交 */
  formSubmit(e) {
    let that = this
    var priceTF = /^\d+(\.\d{1,2})?$/
    var skuTF =/^([1-9][0-9]*)$/ 
    if (e.detail.value.goodsName === "") {
      wx.showToast({
        title: '请输入商品名称',
        icon: "none",
        duration: 1000,
        mask: true,
      })
    } else if (e.detail.value.goodsName.length > 60) {
      wx.showToast({
        title: '商品名称不得大于60字',
        icon: "none",
        duration: 1000,
        mask: true,
      })
    } else if (e.detail.value.price.length === "") {
      wx.showToast({
        title: '请输入商品价格',
        icon: "none",
        duration: 1000,
        mask: true,
      })
    } else if (!priceTF.test(e.detail.value.price)) {
      wx.showToast({
        title: '商品价格精确到两位',
        icon: "none",
        duration: 1000,
        mask: true,
      })
    } else if (e.detail.value.skuStore.length === "") {
      wx.showToast({
        title: '请输入商品库存',
        icon: "none",
        duration: 1000,
        mask: true,
      })
    } else if (!skuTF.test(e.detail.value.skuStore)) {
      wx.showToast({
        title: '商品库存应为整数',
        icon: "none",
        duration: 1000,
        mask: true,
      })
    } else if (that.data.id1 === "") {
      wx.showToast({
        title: '请选择分类',
        icon: "none",
        duration: 1000,
        mask: true,
      })
    } else if (that.data.bannerUrl.length === 0) {
      wx.showToast({
        title: '请选择轮播图片',
        icon: "none",
        duration: 1000,
        mask: true,
      })
    } else if (that.data.detailUrl.length === 0) {
      wx.showToast({
        title: '请选择详情图片',
        icon: "none",
        duration: 1000,
        mask: true,
      })
    } else {
      console.log(that.data.bannerUrl)
      let params = {
        goodsName: e.detail.value.goodsName,
        price: e.detail.value.price,
        skuStore: e.detail.value.skuStore,
        bannerUrl: that.data.bannerUrl.join(","),
        detailUrl: that.data.detailUrl.join(","),
        multiIndex: that.data.multiIndex.join(","),
        classId1: that.data.id1,
        classId2: that.data.id2,
        classId3: that.data.id3,
        appId: app.globalData.appId
      }
      wx.showModal({
        title: '提示',
        content: '确定发布商品',
        success(res) {
          if (res.confirm) {
            if (that.data.productID != 0) {
              that.sureEdit(params); //编辑
            } else {
              that.sureRelease(params); //发布
            }
            that.setData({
              dis: true,
            })
          }
        }
      })
    }
  },

  /**确认发布 */
  sureRelease(params) {
    let that = this
    netUtil.requestLoading(app.globalData.baseUrl + "/front/addGoods", params, '添加中', function (res) {
      //res就是我们请求接口返回的数据
      if (res.code == '100') {
        wx.showToast({
          title: "添加成功"
        })
      } else {
        wx.showToast({
          title: res.msg
        })
      }
    }, function () {
      wx.showToast({
        title: '登录失败',
      })
    })
  },

  /**确认编辑 */
  sureEdit(params) {
    let that = this
    app.addProduct(params).then(res => {
      that.data.params.productID = res.data.productID;
      //判断编辑页面下是否只改变了文字数据，选择图片后checkUp为false
      if (that.data.checkUp && res.state === 1) {
        wx.showToast({
          title: '商品修改成功',
          icon: "none",
          duration: 2000,
          mask: true,
          success() {
            setTimeout(function () {
              wx.navigateBack({
                delta: 0,
              })
            }, 1000);
          }
        })
      }
      //判断编辑页面下是否改变了图片 改变了则uploadFile
      else {
        that.checkBanner();
        that.checkDetail();
        //如果没有添加直接删除图片的话
        if (that.data.bannerAll.length === 0 && that.data.detailAll.length === 0) {
          wx.showToast({
            title: '商品修改成功',
            icon: "none",
            duration: 2000,
            mask: true,
            success() {
              setTimeout(function () {
                wx.navigateBack({
                  delta: 0,
                })
              }, 1000);
            }
          })
        }
        //只改变bannerAll情况下,detailAll为空直接将bannerAll往数据库写入
        else if (that.data.detailAll.length === 0) {
          for (var i = 0; i < that.data.bannerAll.length; i++) {
            if (that.data.bannerAll.length === i + 1) {
              that.data.params.check = true
            }
            wx.uploadFile({
              url: app.globalData.baseUrl + '/wechat/release/addProductPhoto',
              filePath: that.data.bannerAll[i],
              name: 'banner',
              formData: {
                'parameters': JSON.stringify(that.data.params)
              },
              success: function (res) {
                if (JSON.parse(res.data).state === 1) {
                  wx.showToast({
                    title: '商品修改成功',
                    icon: "none",
                    duration: 2000,
                    mask: true,
                    success() {
                      setTimeout(function () {
                        wx.navigateBack({
                          delta: 0,
                        })
                      }, 1000);
                    }
                  })
                } else {
                  wx.showToast({
                    title: '商品修改失败',
                    icon: "none",
                    duration: 2000,
                    mask: true,
                    success() {
                      setTimeout(function () {
                        wx.navigateBack({
                          delta: 0,
                        })
                      }, 1000);
                    }
                  })
                }
              },
              fail(res) {
                if (JSON.parse(res.errMsg) === "request:fail socket time out timeout:6000") {
                  wx.showToast({
                    title: '请求超时，请稍后再试！',
                    icon: "none",
                    duration: 2000,
                    mask: true,
                    success() {
                      setTimeout(function () {
                        wx.navigateBack({
                          delta: 0,
                        })
                      }, 1000);
                    }
                  })
                }
              }
            })
          }
        }
        //只改变detailAll，不改变bannerAll的情况下，直接将detailAll写入数据库
        else if (that.data.bannerAll.length === 0) {
          for (var j = 0; j < that.data.detailAll.length; j++) {
            if (that.data.detailAll.length === j + 1) {
              that.data.params.check = true
            }
            wx.uploadFile({
              url: app.globalData.baseUrl + '/wechat/release/addProductPhoto',
              filePath: that.data.detailAll[j],
              name: 'detail',
              formData: {
                'parameters': JSON.stringify(that.data.params)
              },
              success: function (res) {
                if (JSON.parse(res.data).state === 1) {
                  wx.showToast({
                    title: '商品修改成功',
                    icon: "none",
                    duration: 2000,
                    mask: true,
                    success() {
                      setTimeout(function () {
                        wx.navigateBack({
                          delta: 0,
                        })
                      }, 1000);
                    }
                  })
                } else {
                  wx.showToast({
                    title: '商品修改失败',
                    icon: "none",
                    duration: 2000,
                    mask: true,
                    success() {
                      setTimeout(function () {
                        wx.navigateBack({
                          delta: 0,
                        })
                      }, 1000);
                    }
                  })
                }
              },
              fail: function (res) {
                if (JSON.parse(res.errMsg) === "request:fail socket time out timeout:6000") {
                  wx.showToast({
                    title: '请求超时，请稍后再试！',
                    icon: "none",
                    duration: 2000,
                    mask: true,
                    success() {
                      setTimeout(function () {
                        wx.navigateBack({
                          delta: 0,
                        })
                      }, 1000);
                    }
                  })
                }
              }
            })
          }
        }
        //如果detailAll和bannerAll都改变的情况下
        else {
          for (var i = 0; i < that.data.bannerAll.length; i++) {
            wx.uploadFile({
              url: app.globalData.baseUrl + '/wechat/release/addProductPhoto',
              filePath: that.data.bannerAll[i],
              name: 'banner',
              formData: {
                'parameters': JSON.stringify(that.data.params)
              },
            })
            if (that.data.bannerAll.length === i + 1) {
              for (var j = 0; j < that.data.detailAll.length; j++) {
                if (that.data.detailAll.length === j + 1) {
                  that.data.params.check = true
                }
                wx.uploadFile({
                  url: app.globalData.baseUrl + '/wechat/release/addProductPhoto',
                  filePath: that.data.detailAll[j],
                  name: 'detail',
                  formData: {
                    'parameters': JSON.stringify(that.data.params)
                  },
                  success: function (res) {
                    if (JSON.parse(res.data).state === 1) {
                      wx.showToast({
                        title: '商品修改成功',
                        icon: "none",
                        duration: 2000,
                        mask: true,
                        success() {
                          setTimeout(function () {
                            wx.navigateBack({
                              delta: 0,
                            })
                          }, 1000);
                        }
                      })
                    } else {
                      wx.showToast({
                        title: '商品修改失败',
                        icon: "none",
                        duration: 2000,
                        mask: true,
                        success() {
                          setTimeout(function () {
                            wx.navigateBack({
                              delta: 0,
                            })
                          }, 1000);
                        }
                      })
                    }
                  },
                  fail: function (res) {
                    if (JSON.parse(res.errMsg) === "request:fail socket time out timeout:6000") {
                      wx.showToast({
                        title: '请求超时，请稍后再试！',
                        icon: "none",
                        duration: 2000,
                        mask: true,
                        success() {
                          setTimeout(function () {
                            wx.navigateBack({
                              delta: 0,
                            })
                          }, 1000);
                        }
                      })
                    }
                  }
                })
              }
            }
          }
        }
      }
    })
  },

  /**判断轮播新旧数组是否有相同值 */
  checkBanner() {
    let banner = this.data.banner
    let bannerNew = this.data.bannerNew
    let bannerAll = this.data.bannerAll
    for (var i = 0; i < banner.length; i++) {
      for (var j = 0; j < bannerNew.length; j++) {
        if (banner[i] === bannerNew[j]) {
          bannerAll = bannerAll.concat(bannerNew[j])
          this.setData({
            bannerAll: bannerAll
          })
        } else {
          console.log("banner无相同")
        }
      }
    }
  },

  /**判断详情新旧数组是否有相同值 */
  checkDetail() {
    let detail = this.data.detail
    let detailNew = this.data.detailNew
    let detailAll = this.data.detailAll
    for (var i = 0; i < detail.length; i++) {
      for (var j = 0; j < detailNew.length; j++) {
        if (detail[i] === detailNew[j]) {
          detailAll = detailAll.concat(detail[i])
          this.setData({
            detailAll: detailAll
          })
        } else {
          console.log("detail无相同")
        }
      }
    }
  },
  /** 选择图片detail */
  chooseDetail: function () {
    var that = this;
    if (that.data.detail.length < 10) {
      wx.chooseImage({
        count: 10,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: function (photo) {
          //detail中包含的可能还有编辑页面下回显的图片，detailNew中包含的只有所选择的图片
          let detail = that.data.detail;
          detail = detail.concat(photo.tempFilePaths);
          let detailNew = that.data.detailNew
          detailNew = detailNew.concat(photo.tempFilePaths)
          that.setData({
            detail: detail,
            detailNew: detailNew,
            checkUp: false
          })
          that.chooseViewShowDetail();
          var tempFilePaths = photo.tempFilePaths;
          for (var i = 0; i < tempFilePaths.length;i++){
            wx.uploadFile({
              url: app.globalData.baseUrl + "/uploadimg",      //此处换上你的接口地址
              filePath: tempFilePaths[0],
              name: 'file',
              header: {
                "Content-Type": "multipart/form-data",
                'accept': 'application/json',

              },
              formData: {
                'user': 'test'  //其他额外的formdata，可不写
              },
              success: function (res) {
                let detailUrl = that.data.detailUrl
                detailUrl.push(JSON.parse(res.data).info)
                that.setData({
                  detailUrl: detailUrl
                })
              },
              fail: function (res) {
                console.log('fail');

              },
            })
          }
       
        }
      })
    } else {
      wx.showToast({
        title: '限制选择3个文件',
        icon: 'none',
        duration: 1000
      })
    }
  },

  /** 删除图片detail */
  deleteImvDetail: function (e) {
    var that = this;
    var detail = that.data.detail;
    var detailUrl = that.data.detailUrl;
    var itemIndex = e.currentTarget.dataset.id;
    if (that.data.productID != 0) {
      wx.showModal({
        title: '提示',
        content: '删除不可恢复，请谨慎操作',
        success(res) {
          if (res.confirm) {
            detail.splice(itemIndex, 1);
            detailUrl.splice(itemIndex, 1);
            that.setData({
              detail: detail,
              checkUp: false,
              detailUrl: detailUrl
            })
            that.chooseViewShowDetail();
            let params = {
              productID: that.data.productID,
              isBanner: false,
              index: itemIndex,
            }
            app.deleteProductImage(params).then(res => {
              if (res.data.fileContent !== "" && res.data.fileBanner !== "") {
                that.data.params.contentFile = res.data.fileContent
                that.data.params.bannerFile = res.data.fileBanner
              }
            })
          }
        }
      })
    } else {
      detail.splice(itemIndex, 1);
      that.setData({
        detail: detail,
        checkUp: false
      })
      that.chooseViewShowDetail();
    }
  },


  /** 是否隐藏图片选择detail */
  chooseViewShowDetail: function () {
    if (this.data.detail.length >= 3) {
      this.setData({
        chooseViewShowDetail: false
      })
    } else {
      this.setData({
        chooseViewShowDetail: true
      })
    }
  },

  /** 查看大图Detail */
  showImageDetail: function (e) {
    var detail = this.data.detail;
    var itemIndex = e.currentTarget.dataset.id;
    wx.previewImage({
      current: detail[itemIndex], // 当前显示图片的http链接
      urls: detail // 需要预览的图片http链接列表
    })
  },


  /** 选择图片Banner */
  chooseBanner: function () {
    var that = this;
    if (that.data.banner.length < 10) {
      wx.chooseImage({
        count: 10, //最多选择4张图片- that.data.imgArr.length,
        sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function (photo) {
          var banner = that.data.banner;
          banner = banner.concat(photo.tempFilePaths);
          var bannerNew = that.data.bannerNew;
          bannerNew = bannerNew.concat(photo.tempFilePaths);
          that.setData({
            banner: banner,
            bannerNew: bannerNew,
            checkUp: false
          })
          that.chooseViewShowBanner();
          var tempFilePaths = photo.tempFilePaths;
          for (var i = 0; i < tempFilePaths.length;i++){
            wx.uploadFile({
              url: app.globalData.baseUrl + "/uploadimg",      //此处换上你的接口地址
              filePath: tempFilePaths[i],
              name: 'file',
              header: {
                "Content-Type": "multipart/form-data",
                'accept': 'application/json;charset=utf-8',

              },
              formData: {
                'user': 'test'  //其他额外的formdata，可不写
              },
              success: function (res) {
                let bannerUrl = that.data.bannerUrl
                bannerUrl.push(JSON.parse(res.data).info)
                that.setData({
                  bannerUrl: bannerUrl
                })
              },
              fail: function (res) {
                console.log('fail');

              },
            })
          }
          
        }
      })

    } else {
      wx.showToast({
        title: '限制选择2个文件',
        icon: 'none',
        duration: 1000
      })
    }
  },

  /** 删除图片Banner */
  deleteImvBanner: function (e) {
    var that = this
    var banner = that.data.banner;
    var itemIndex = e.currentTarget.dataset.id;
    var bannerUrl = that.data.bannerUrl
    if (that.data.productID != 0) {
      wx.showModal({
        title: '提示',
        content: '删除不可恢复，请谨慎操作',
        success(res) {
          if (res.confirm) {
            banner.splice(itemIndex, 1);
            bannerUrl.splice(itemIndex, 1);
            that.setData({
              banner: banner,
              checkUp: false,
              bannerUrl: bannerUrl
            })
            that.chooseViewShowBanner();
            let params = {
              productID: that.data.productID,
              isBanner: true,
              index: itemIndex,
            }
            app.deleteProductImage(params).then(res => {
              if (res.data.fileContent !== "" && res.data.fileBanner !== "") {
                that.data.params.contentFile = res.data.fileContent
                that.data.params.bannerFile = res.data.fileBanner
              }
            })
          }
        }
      })
    } else {
      banner.splice(itemIndex, 1);
      that.setData({
        banner: banner,
        checkUp: false
      })
      that.chooseViewShowBanner();
    }
  },


  /** 是否隐藏图片选择Banner*/
  chooseViewShowBanner() {
    if (this.data.banner.length >= 2) {
      this.setData({
        chooseViewShowBanner: false
      })
    } else {
      this.setData({
        chooseViewShowBanner: true
      })
    }
  },

  /** 查看大图Banner */
  showImageBanner: function (e) {
    var banner = this.data.banner;
    var itemIndex = e.currentTarget.dataset.id;
    wx.previewImage({
      current: banner[itemIndex], // 当前显示图片的http链接
      urls: banner // 需要预览的图片http链接列表
    })
  },
  bindMultiPickerChange: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      multiIndex: e.detail.value
    })
  },
  bindMultiPickerColumnChange: function (e) {
    console.log(this.data.array)
   
    console.log('修改的列为', e.detail.column, '，值为', e.detail.value);
    this.multiArrayChange(e.detail.column, e.detail.value)

    console.log(this.data.multiArray)
    this.setData({
      multiArray: this.data.multiArray
    })
    // 知道修改的列以后，就可以通过修改multiIndex中对应元素的值，然后再修改multiArray
  },
  multiArrayChange: function (column,value){
    if (column == 0) {
      this.data.multiArray[1] = []
      for (var i = 0; i < this.data.array[value].childrenScreenArray.length; i++) {
        this.data.multiArray[1].push(this.data.array[value].childrenScreenArray[i].screenName)
      }
      this.setData({
        index: value
      })
      this.data.multiArray[2] = []
      for (var i = 0; i < this.data.array[value].childrenScreenArray[0].childrenScreenArray.length; i++) {
        this.data.multiArray[2].push(this.data.array[value].childrenScreenArray[0].childrenScreenArray[i].screenName)

      }
      this.setData({
        id1: this.data.array[value].screenId
      })
      this.setData({
        id2: this.data.array[value].childrenScreenArray[0].screenId
      })
      this.setData({
        id3: this.data.array[value].childrenScreenArray[0].childrenScreenArray[0].screenId
      })
      this.data.multiIndex[0] = value
      this.data.multiIndex[1]=0
      this.data.multiIndex[2] = 0
      this.setData({
        multiIndex: this.data.multiIndex
      })
    } else if (column==1){
      this.data.multiArray[2] = []
      this.setData({
        indexTwo: value
      })
      for (var i = 0; i < this.data.array[this.data.index].childrenScreenArray[value].chlidren.length; i++) {
        this.data.multiArray[2].push(this.data.array[this.data.index].chlidren[value].chlidren[i].screenName)

      }
      this.setData({
        id1: this.data.array[this.data.index].screenId
      })
      this.setData({
        id2: this.data.array[this.data.index].childrenScreenArray[value].screenId
      })
      this.setData({
        id3: this.data.array[this.data.index].childrenScreenArray[value].childrenScreenArray[0].screenId
      })
      this.data.multiIndex[0] = this.data.index
      this.data.multiIndex[1] = value
      this.data.multiIndex[2] = 0
      this.setData({
        multiIndex: this.data.multiIndex
      })
    } else if (column == 3){
      this.setData({
        id1: this.data.array[this.data.index].screenId
      })
      this.setData({
        id2: this.data.array[this.data.index].childrenScreenArray[this.data.indexTwo].screenId
      })
      this.setData({
        id3: this.data.array[this.data.index].childrenScreenArray[this.data.indexTwo].childrenScreenArray[value].screenId
      })
      this.data.multiIndex[0] = this.data.index
      this.data.multiIndex[1] = this.data.indexTwo
      this.data.multiIndex[2] = value
      this.setData({
        multiIndex: this.data.multiIndex
      })
    }
    console.log(this.data.id1 + "," + this.data.id2 + "," + this.data.id3)
  }
})
