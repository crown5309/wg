<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <div class="demo-input-size">
            <span>商品名:</span>
            <el-input placeholder="请输入商品名" v-model="listQuery.goodsName" class="inputSerach" clearable>
              <i slot="prefix" class="el-input__icon el-icon-search"></i>
            </el-input>
            <span>商品分类:</span>
            <el-cascader clearable v-model="listQuery.classId" :options="optionsClass" :props="{ expandTrigger: 'hover' }"></el-cascader>
            <span>状态:</span>
            <el-select v-model="listQuery.state" placeholder="请选择" clearable>
              <el-option v-for="item in optionsState" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
            <span>添加时间:</span>
            <el-date-picker v-model="listQuery.createTime" type="datetimerange" range-separator="至" start-placeholder="开始日期"
              end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
            <el-button type="primary" icon="el-icon-search" @click="getList" v-if="hasPerm('goods:list')">搜索</el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate" v-if="hasPerm('goods:add')">添加
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="goodsName" label="名称" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="price" label="价格(元)" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="storeName" label="商家" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="stateName" label="状态" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="skuStore" label="库存" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="count" label="销量" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="createTime" label="添加时间" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="下架/上架" width="200" v-if="hasPerm('goods:update')">
        <template slot-scope="scope">
          <el-switch style="display: block" active-value="2" inactive-value="3" v-model="scope.row.state" active-color="#13ce66"
            inactive-color="#ff4949" active-text="上架" inactive-text="下架" @change="changeValue(scope.row.state,scope.$index)"
            :disabled="scope.row.flag">
          </el-switch>

        </template>
      </el-table-column>
      <el-table-column align="center" label="审核" width="200" v-if="hasPerm('goods:update')">
        <template slot-scope="scope">
          <el-switch style="display: block" active-value="2" inactive-value="4" v-model="scope.row.state" active-color="#13ce66"
            inactive-color="#ff4949" active-text="通过" inactive-text="拒绝" @change="changeValue(scope.row.state,scope.$index)"
            :disabled="scope.row.auditFlag">
          </el-switch>

        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" width="200" v-if="hasPerm('goods:update')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>

        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow" :total="totalCount" :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="goods" label-position="left" label-width="80px" style='width: 300px; margin-left:80px;'>
        <el-form-item label="商品名称">
          <el-input type="text" v-model="goods.goodsName" placeholder="请输入商品名">
          </el-input>
        </el-form-item>
        <el-form-item label="价格(元)">
          <el-input type="number" v-model="goods.price" placeholder="请输入价格">
          </el-input>
        </el-form-item>
        <el-form-item label="库存">
          <el-input type="number" v-model="goods.skuStore" placeholder="请输入库存">
          </el-input>
        </el-form-item>
        <el-form-item label="所属分类">
          <el-cascader  @change="getClassIndex" clearable v-model="goods.classId" :options="optionsClass" :props="{ expandTrigger: 'hover' }"></el-cascader>
          </el-input>
        </el-form-item>
        <el-form-item label="banner图" class="imgCss">
          <span v-for="(item, i) in imgsback" :key='i'>
            <el-image class="img" :src="item" :preview-src-list="imgsback">
            </el-image>
            <span @click="delimgback(i)"><i class="el-icon-delete"></i></span>
          </span>
          <el-upload :auto-upload="false" class="avatar-uploader" action="" :show-file-list="false" :on-change="upload"
            :before-upload="beforeAvatarUpload">
            <i class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="详情图" class="imgCss">
          <span v-for="(item, i) in imgsback1" :key='i'>
            <el-image class="img" :src="item" :preview-src-list="imgsback1">
            </el-image>
            <span @click="delimgback1(i)"><i class="el-icon-delete"></i></span>
          </span>
          <el-upload :auto-upload="false" class="avatar-uploader" action="" :show-file-list="false" :on-change="upload1"
            :before-upload="beforeAvatarUpload">
            <i class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createGoods()">创 建</el-button>
        <el-button type="primary" v-else @click="updateArticle">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        totalCount: 0, //分页组件--数据总条数
        list: [], //表格的数据
        listLoading: false, //数据加载等待动画
        listQuery: {
          pageNum: 1, //页码
          pageRow: 10, //每页条数
          name: '',
          goodsName: "",
          classId: [],
          state: '',
          createTime: [],
          startTime: '',
          endTime: ''
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新增商品'
        },
        goods: {
          id: "",
          goodsName: "",
          price: "",
          skuStore: "",
          bannerUrl: "",
          detailUrl: "",
          classId1: "",
          classId2: "",
          classId3: '',
          multiIndex: [],
          classId:[]
        },
        dialogImageUrl: '',
        dialogVisible: false,
        //0 待审核 1审核通过  2 上架 3下架 4拒绝
        optionsState: [{
          value: '0 ',
          label: '待审核'
        }, {
          value: '1',
          label: '审核通过',
          disabled: true
        }, {
          value: '2',
          label: '上架'
        }, {
          value: '3',
          label: '下架'
        }, {
          value: '4',
          label: '拒绝通过'
        }],
        optionsClass: [],
        imgsback: [],
        imgsback1: []
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        if (this.listQuery.classId.length > 0) {
          this.listQuery.classId = this.listQuery.classId[2]
        }
        if (this.listQuery.createTime != null && this.listQuery.createTime.length > 0) {
          this.listQuery.startTime = this.listQuery.createTime[0]
          this.listQuery.endTime = this.listQuery.createTime[1]
          this.listQuery.createTime = this.listQuery.createTime.join(",")
        }
        //查询列表
        if (!this.hasPerm('goods:list')) {
          return
        }
        this.listLoading = true;
        this.api({
          url: "/goods/listgoods",
          method: "get",
          params: this.listQuery
        }).then(data => {
          if (this.listQuery.createTime != null && this.listQuery.createTime.length > 0) {
            this.listQuery.createTime = this.listQuery.createTime.split(",")
          }
          this.listLoading = false;
          this.list = data.list;
          this.optionsClass = data.goodsClassList
          this.list.forEach((item) => {
            if (item.state == 0 || item.state == 4) {
              item.flag = true
            } else {
              item.flag = false
            }
            if(item.state==0){
              item.auditFlag=false
            }else{
              item.auditFlag=true
            }
          })
          this.totalCount = data.totalCount;
        })
      },
      handleSizeChange(val) {
        //改变每页数量
        this.listQuery.pageRow = val
        this.handleFilter();
      },
      handleCurrentChange(val) {
        //改变页码
        this.listQuery.pageNum = val
        this.getList();
      },
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
      showCreate() {

        //显示新增对话框
        this.goods.goodsName = "";
        this.goods.price = "";
        this.goods.skuStore = "";
        this.imgsback =[];
        this.imgsback1 =[];
        this.goods.classId =[];
        this.dialogStatus = "create"
        this.dialogFormVisible = true
        this.dialogImageUrl = ""
      },
      showUpdate($index) {
        //显示修改对话框
        this.goods.classId=[]
        this.goods.id = this.list[$index].id;
        this.goods.goodsName = this.list[$index].goodsName;
        this.goods.price = this.list[$index].price;
        this.goods.skuStore = this.list[$index].skuStore;
        this.goods.classId.push(this.list[$index].classId1)
        this.goods.classId.push(this.list[$index].classId2)
        this.goods.classId.push(this.list[$index].classId3)
        this.imgsback=this.list[$index].bannerUrl.split(",")
        this.imgsback1=this.list[$index].detailUrl.split(",")
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      
      },
      verification(){
        const skuStore= /^[0-9]+$/
       const price= /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/
        if(this.goods.goodsName==''){
          this.$message.error("请输入商品名称");
          return false
        }
        if(!price.test(this.goods.price)){
          this.$message.error("请输入合法的金额数字，最多两位小数");
          return false
        }
        if(!skuStore.test(this.goods.skuStore)){
          this.$message.error("库存应为整数");
          return false
        }
        if(this.goods.classId.length==0){
          this.$message.error("请选择分类");
          return false
        }
        if(this.imgsback==0){
          this.$message.error("请上传banner图");
          return false
        }
        if(this.imgsback1==0){
          this.$message.error("请上传详情图");
          return
        }
        this.goods.classId1=this.goods.classId[0]
        this.goods.classId2=this.goods.classId[1]
        this.goods.classId3=this.goods.classId[2]
        this.goods.bannerUrl=this.imgsback.join(",")
        this.goods.detailUrl=this.imgsback1.join(",")
        return true
      },
      createGoods() {
        if(this.verification()){//校验
          this.api({
            url: "/goods/addgoodsClass",
            method: "post",
            data: this.goods
          }).then(() => {
            this.getList();
            this.dialogFormVisible = false
          })
        }
      },
      updateArticle() {
        //修改文章
        this.api({
          url: "/goods/updategoodsClass",
          method: "post",
          data: this.goodsClass
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;
      },
      changeValue(value, index) {
        let name = ""
        if (value == 2) { //上架
          name = "上架"
        } else if (value == 3) {
          name = "下架"
        }
        this.$confirm('确定' + name + '?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          this.list[index].stateName = name
          this.list[index].state = value
          this.api({
            url: "/goods/updateGoods",
            method: "post",
            params: this.list[index]
          }).then(() => {
            this.$message({
              message: this.list[index].stateName + "成功",
              type: 'success',
              duration: 1 * 1000,
            })
          })
        })
      },
      upload(e) {
        let formData1 = new FormData();
        formData1.append("file", e.raw)
        this.api({
          url: "/uploadimg",
          method: "post",
          data: formData1,
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }).then((data) => {
          data.forEach((item) => {
            this.imgsback.push(item)
          })
        })
      },
      upload1(e) {
        let formData1 = new FormData();
        formData1.append("file", e.raw)
        this.api({
          url: "/uploadimg",
          method: "post",
          data: formData1,
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }).then((data) => {
          data.forEach((item) => {
            this.imgsback1.push(item)
          })
        })
      },
      //删除图片的方法
      delimgback(i) {
        this.imgsback.splice(i, 1)
      },
      //删除图片的方法
      delimgback1(i) {
        this.imgsback1.splice(i, 1)
      },
      getClassIndex(){
        let classArray=this.goods.classId
        let classIndex=[]
        for(let i=0;i<this.optionsClass.length;i++){
          if(this.optionsClass[i].value==classArray[0]){
            classIndex.push(i);
            for(let j=0;j<this.optionsClass[i].children.length;j++){
              if(this.optionsClass[i].children[j].value==classArray[1]){
                classIndex.push(j);
                for(let k=0;k<this.optionsClass[i].children[j].children.length;k++){
                   if(this.optionsClass[i].children[j].children[k].value==classArray[2]){
                     classIndex.push(k);
                   }
                }
              }
            }
          }
        }
        this.goods.multiIndex=classIndex.join(",")
      }
    }
  }
</script>
<style>
  .imgCss .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader,
  .avatar-uploader div {
    display: inline-block;

  }

  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 80px;
    height: 80px;
    line-height: 80px;
    text-align: center;

  }

  .img {
    font-size: 28px;
    color: #8c939d;
    width: 80px;
    height: 80px;
    line-height: 80px;
    text-align: center;
  }

  .avatar {
    width: 80px;
    height: 80px;
  }

  .inputSerach {
    width: 10%;
  }
</style>
