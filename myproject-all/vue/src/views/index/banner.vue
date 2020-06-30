<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPerm('banner:add')" @click="showCreate">添加
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit highlight-current-row>
      <el-table-column align="center" label="显示顺序" prop="showOrder"></el-table-column>
      <el-table-column align="center" label="图片">
        <template slot-scope="scope">
          <img :src="scope.row.imgUrl" width="80px" height="80px" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" v-if="hasPerm('banner:update')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>
          <el-button type="danger" icon="delete" v-if="hasPerm('banner:delete')" @click="removeUser(scope.$index)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow" :total="totalCount" :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="goodsClass" label-position="left" label-width="80px" style='width: 300px; margin-left:80px;'>
        <el-form-item label="显示顺序">
          <el-input type="number" v-model="goodsClass.showOrder">
          </el-input>
        </el-form-item>
        <el-form-item label="图片">
          <el-upload class="avatar-uploader" action="" :show-file-list="false" :before-upload="beforeAvatarUpload" :on-change="upload">
            <img v-if="goodsClass.imgUrl" :src="goodsClass.imgUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createBanner()">创 建</el-button>
        <el-button type="primary" v-else @click="updateArticle">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {
    mapGetters
  } from 'vuex'

  export default {
    data() {
      return {
        totalCount: 0, //分页组件--数据总条数
        list: [], //表格的数据
        listLoading: false, //数据加载等待动画
        listQuery: {
          pageNum: 1, //页码
          pageRow: 50, //每页条数
        },
        roles: [], //角色列表
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建用户'
        },
        goodsClass: {
          imgUrl: '',
          showOrder: '',
          id: '',
        },
        deId: {
          id: ""
        }
      }
    },
    created() {
      this.getList();

    },
    computed: {
      ...mapGetters([
        'userId'
      ])
    },
    methods: {
      getList() {
        //查询列表
        this.listLoading = true;
        this.api({
          url: "/index/listBanner",
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
      handleFilter() {
        //查询事件
        this.listQuery.pageNum = 1
        this.getList()
      },
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
      showCreate() {
        //显示新增对话框
        this.goodsClass.id = "";
        this.goodsClass.imgUrl = "";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        let user = this.list[$index];
        this.goodsClass.imgUrl = user.imgUrl;
        this.goodsClass.id = user.id
        this.goodsClass.showOrder = user.showOrder;
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createBanner() {
        //添加新用户
        this.api({
          url: "/index/addBanner",
          method: "post",
          params: this.goodsClass
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },
      updateArticle() {
        //修改用户信息
        let _vue = this;
        this.api({
          url: "/index/updateBanner",
          method: "post",
          params: this.goodsClass
        }).then(() => {
          let msg = "修改成功";
          this.dialogFormVisible = false
          this.$message({
            message: msg,
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              _vue.getList();
            }
          })
        })
      },
      removeUser($index) {
        let _vue = this;
        this.$confirm('确定删除?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let user = _vue.list[$index];
          this.deId.id = user.id
          _vue.api({
            url: "/index/deleteBanner",
            method: "post",
            params: this.deId
          }).then(() => {
            _vue.getList()
          }).catch(() => {
            _vue.$message.error("删除失败")
          })
        })
      },
      upload(e) {
        console.log(e)
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
          this.goodsClass.imgUrl = data.join(",")
        })
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;
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
