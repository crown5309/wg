<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate" v-if="hasPerm('goods:add')">查询
          </el-button>
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
      <el-table-column align="center" prop="storeName" label="商家" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="stateName" label="状态" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="createTime" label="添加时间" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="上架/下架" width="200" v-if="hasPerm('goods:update')">
        <template slot-scope="scope">
          <el-switch style="display: block" active-value="3" inactive-value="2" v-model="scope.row.state"  active-color="#13ce66" inactive-color="#ff4949"
            active-text="下架" inactive-text="上架" @change="changeValue(scope.row.state,scope.$index)">
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
      <el-form class="small-space" :model="goodsClass" label-position="left" label-width="80px" style='width: 300px; margin-left:80px;'>
        <el-form-item label="分类名称">
          <el-input type="text" v-model="goodsClass.className">
          </el-input>
        </el-form-item>
        <el-form-item label="显示顺序">
          <el-input type="number" v-model="goodsClass.showOrder">
          </el-input>
        </el-form-item>
        <el-form-item label="图片" v-show='goodsClass.classLevel!=2'>

          <el-upload class="avatar-uploader" action="http://81.68.73.72:9000/uploadimg" :show-file-list="false"
            :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
            <img v-if="goodsClass.classImgUrl" :src="goodsClass.classImgUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createGoodsClass()">创 建</el-button>
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
          parentId: "0",
          shangId: "",
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '创建分类'
        },
        goodsClass: {
          id: "",
          className: "",
          classImgUrl: "",
          imgUrl: "",
          showOrder: "",
          classLevel: 1,
          parentId: 0
        },
        dialogImageUrl: '',
        dialogVisible: false
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
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
          this.listLoading = false;
          this.list = data.list;
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
        this.goodsClass.className = "";
        this.goodsClass.showOrder = "";
        this.goodsClass.classImgUrl = "";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
        this.dialogImageUrl = ""
      },
      showUpdate($index) {
        //显示修改对话框
        this.goodsClass.id = this.list[$index].id;
        this.goodsClass.className = this.list[$index].className;
        this.goodsClass.showOrder = this.list[$index].showOrder;
        this.goodsClass.classImgUrl = this.list[$index].classImgUrl;
        this.dialogImageUrl = this.list[$index].classImgUrl
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      showXia($index) {
        this.goodsClass.classLevel++
        this.listQuery.parentId = this.list[$index].id
        if (this.goodsClass.classLevel == 2) {
          this.listQuery.shangId = this.list[$index].id
        }
        this.getList()
      },
      showSang() {
        this.goodsClass.classLevel = this.goodsClass.classLevel - 1;
        if (this.goodsClass.classLevel == 2) {
          this.listQuery.parentId = this.listQuery.shangId
        } else {
          this.listQuery.parentId = 0
        }
        this.getList()
      },
      createGoodsClass() {
        this.goodsClass.parentId = this.listQuery.parentId
        //保存新文章
        this.api({
          url: "/goods/addgoodsClass",
          method: "post",
          data: this.goodsClass
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
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
      handleAvatarSuccess(res, file) {
        if (res.code == '100') {
          this.goodsClass.classImgUrl = res.info.join(",")
        }
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;
      },
      changeValue(value,index){
        if(value==2){//上架
          this.list[index].stateName="上架"
        }else{
          this.list[index].stateName="下架"
        }
      }
    }
  }
</script>
<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }

  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
