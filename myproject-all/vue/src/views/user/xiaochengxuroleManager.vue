<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>

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
      <el-table-column align="center" label="菜单名" prop="permissionName"></el-table-column>
      <el-table-column align="center" label="菜单路径" prop="permissionCode"></el-table-column>
      <el-table-column align="center" label="菜单图标">
        <template slot-scope="scope">
            <img :src="scope.row.imgUrl" width="80px" height="80px" />
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column align="center" label="显示顺序" prop="showOrder"></el-table-column>
      <el-table-column align="center" label="管理" width="220" v-if="hasPerm('role:update') ||hasPerm('role:delete') ">
        <template slot-scope="scope">
          <div v-if="scope.row.roleName!='管理员'">
            <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)" v-if="hasPerm('role:update')">修改
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="tempRole" label-position="left" label-width="80px" style='width: 300px; margin-left:80px;'>
        <el-form-item label="显示顺序">
          <el-input type="number" v-model="tempRole.showOrder" placeholder="请输入显示顺序">
          </el-input>
        </el-form-item>
        <el-form-item label="菜单图标" class="imgCss">
          <el-upload class="avatar-uploader" action="" :show-file-list="false"
            :auto-upload="false" :before-upload="beforeAvatarUpload" :on-change="upload">
            <img v-if="tempRole.imgUrl" :src="tempRole.imgUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary"  @click="updateRole" :disabled="disabled">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        list: [], //表格的数据
        allPermission: [],
        listLoading: false, //数据加载等待动画
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建角色'
        },
        tempRole: {
          showOrder: '',
          imgUrl: '',
          permissions: [],
        },
        adminName: '管理员',
        disabled:false
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        //查询列表
        this.listLoading = true;
        this.api({
          url: "/user/listPermission",
          method: "get",
          params: {
            type: 'weixin'
          }
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
        })
      },
      getIndex($index) {
        //表格序号
        return $index + 1
      },
      showCreate() {
        //显示新增对话框
        this.tempRole.roleName = '';
        this.tempRole.roleId = '';
        this.tempRole.permissions = [];
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        this.disabled=false
        let role = this.list[$index];
        this.tempRole.id = role.id;
        this.tempRole.imgUrl = role.imgUrl;
        this.tempRole.showOrder =role.showOrder;
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      updateRole() {
         this.disabled=true
         const price = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/
       if (!price.test(this.tempRole.showOrder)) {
         this.$message.error("请输入显示顺序，最多两位小数");
         return
       }
       if (this.tempRole.imgUrl=="") {
         this.$message.error("请上传图片");
         return
       }

        //修改角色
        this.api({
          url: "/user/updateWeiRole",
          method: "post",
          params: this.tempRole
        }).then(() => {
          this.$message({
            message: "修改成功",
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              this.getList();
              this.dialogFormVisible = false
              this.listLoading = false;
            }
          })
        })
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;
      },
      upload(e) {
        let formData1 = new FormData();
        formData1.append("file", e.raw)
        this.listLoading = true;
        this.api({
          url: "/uploadimg",
          method: "post",
          data: formData1,
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }).then((data) => {
          this.listLoading = false;
          this.tempRole.imgUrl=data.join(",")
        })
      },
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
