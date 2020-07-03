<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <div class="demo-input-size">
            <span>商家:</span>
            <el-input placeholder="请输入商铺名" v-model="listQuery.storeName" class="inputSerach" clearable>
              <i slot="prefix" class="el-input__icon el-icon-search"></i>
            </el-input>
            <span>分类:</span>
            <el-select v-model="listQuery.storeClassId" placeholder="请选择" clearable>
              <el-option v-for="item in shopClassList" :key="item.id" :label="item.className" :value="item.id">
              </el-option>
            </el-select>
            <span>状态:</span>
            <el-select v-model="listQuery.state" placeholder="请选择" clearable>
              <el-option v-for="item in stateList" :key="item.value" :label="item.name" :value="item.value">
              </el-option>
            </el-select>
            <span>添加时间:</span>
            <el-date-picker v-model="listQuery.createTime" type="datetimerange" range-separator="至" start-placeholder="开始日期"
              end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss" clearable>
            </el-date-picker>
            <el-button type="primary" icon="el-icon-search" @click="getList" v-if="hasPerm('shop:list')">搜索</el-button>
          </div>
        </el-form-item>
      </el-form>
      <el-form>
        <el-form-item>
          <!-- <el-button type="primary" icon="plus" @click="showCreate" v-if="hasPerm('shop:add')">添加 -->
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
      <el-table-column align="center" prop="storeName" label="店铺名称" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="name" label="联系人" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="telephone" label="联系方式" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="className" label="所属分类" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="area" label="所在地" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="stateName" label="状态" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="创店时间" width="170">
        <template slot-scope="scope">
          <span>{{scope.row.createTime}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="审核" width="200" v-if="hasPerm('shop:audit')">
        <template slot-scope="scope" class="switchClass">
          <el-tag type="success" @click="changeValue(2,scope.$index)" v-show="scope.row.state==1||scope.row.state==2">通过</el-tag>
          <el-tag type="danger" @click="changeValue(3,scope.$index)" v-show="scope.row.state==1||scope.row.state==3">拒绝</el-tag>
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" width="200">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow" :total="totalCount" :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="tempArticle" label-position="left" label-width="100px" style='width: 300px; margin-left:50px;'>
        <el-form-item label="店铺名称">
          <el-input type="text" v-model="tempArticle.storeName" value="" placeholder="请输入店铺名称">
          </el-input>
        </el-form-item>
        <el-form-item label="联系人">
          <el-input type="text" v-model="tempArticle.name" placeholder="请输入联系人">
          </el-input>
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input type="text" v-model="tempArticle.telephone" placeholder="请输入联系方式">
          </el-input>
        </el-form-item>
        <el-form-item label="所属分类">
          <el-select v-model="tempArticle.storeClassId" placeholder="请选择">
            <el-option v-for="item in shopClassList" :key="item.id" :label="item.className" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所在地">
          <el-cascader v-model="tempArticle.area" :options="areaList" :props="{ expandTrigger: 'hover' }"></el-cascader>
          </el-input>
        </el-form-item>
        <el-form-item label="地址详情">
          <el-input type="text" v-model="tempArticle.areaInfo" placeholder="请输入地址详情">
          </el-input>
        </el-form-item>
        <el-form-item label="营业执照">
          <el-upload class="avatar-uploader" action="" :show-file-list="false" :auto-upload="false" :before-upload="beforeAvatarUpload"
            :on-change="upload">
            <img v-if="tempArticle.imgUrl" :src="tempArticle.imgUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="店铺logo">
          <el-upload class="avatar-uploader" action="" :show-file-list="false" :auto-upload="false" :before-upload="beforeAvatarUpload"
            :on-change="upload1">
            <img v-if="tempArticle.logoUrl" :src="tempArticle.logoUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createArticle">创 建</el-button>
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
          pageRow: 50, //每页条数
          storeClassId: '',
          storeName: '',
          createTime: [],
          startTime: '',
          endTime: '',
          state: ''
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '创建'
        },
        tempArticle: {
          id: "",
          storeName: "",
          storeClassId: "",
          areaInfo: "",
          areaIdOne: "",
          areaIdTwo: "",
          areaIdThree: "",
          imgUrl: "",
          logoUrl: '',
          name: '',
          telephone: '',
          area: []
        },
        shopClassList: [],
        areaList: [],
        stateList: [{ //1 待审核 2.审核通过   3拒绝
          name: '待审核',
          value: 1
        }, { //1 待审核 2.审核通过   3拒绝
          name: '审核通过',
          value: 2
        }, { //1 待审核 2.审核通过   3拒绝
          name: '拒绝通过',
          value: 3
        }, ]
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        if (this.listQuery.createTime != null && this.listQuery.createTime.length > 0) {
          this.listQuery.startTime = this.listQuery.createTime[0]
          this.listQuery.endTime = this.listQuery.createTime[1]
          this.listQuery.createTime = this.listQuery.createTime.join(",")
        }
        //查询列表
        if (!this.hasPerm('shop:list')) {
          return
        }
        this.listLoading = true;
        this.api({
          url: "/shop/listShop",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.list.forEach((item) => {
            if (item.state == 1) {
              item.auditFlag = false
            } else {
              item.auditFlag = true
            }
          })
          this.areaList = data.areaList
          this.shopClassList = data.shopClassList
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
        this.tempArticle.content = "";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        //显示修改对话框
        //显示修改对话框
        this.tempArticle.id = this.list[$index].id
        this.tempArticle.name = this.list[$index].name
        this.tempArticle.storeName = this.list[$index].storeName
        this.tempArticle.telephone = this.list[$index].telephone
        this.tempArticle.storeClassId = this.list[$index].storeClassId
        this.tempArticle.areaInfo = this.list[$index].areaInfo
        this.tempArticle.imgUrl = this.list[$index].imgUrl
        this.tempArticle.logoUrl = this.list[$index].logoUrl
        this.tempArticle.area = []
        if (!this.tempArticle.name) {
          this.tempArticle.name = ''
        }
        if (!this.tempArticle.telephone) {
          this.tempArticle.telephone = ''
        }
        this.tempArticle.area.push(this.list[$index].areaIdOne)
        this.tempArticle.area.push(this.list[$index].areaIdTwo)
        this.tempArticle.area.push(this.list[$index].areaIdThree)
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createArticle() {
        //保存新文章
        this.api({
          url: "/article/addArticle",
          method: "post",
          data: this.tempArticle
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },
      updateArticle() {
        const skuStore = /^[0-9]+$/
        const price = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/
        if (this.tempArticle.storeName == '') {
          this.$message.error("请输入商铺名");
          return
        }
        if (this.tempArticle.name == '') {
          this.$message.error("请输入联系人");
          return
        }
        if (this.tempArticle.telephone == '') {
          this.$message.error("请输入联系方式");
          return
        }
        if (!(/^1[3456789]\d{9}$/.test(this.tempArticle.telephone))) {
          this.$message.error("联系方式有误");
          return
        }
        if (this.tempArticle.storeClassId == '') {
          this.$message.error("请选择分类");
          return
        }
        if (this.tempArticle.area.length == 0) {
          this.$message.error("请选择所在地");
          return
        }
        if (this.tempArticle.areaInfo == '') {
          this.$message.error("请输入地址详情");
          return
        }
        if (this.tempArticle.imgUrl == '') {
          this.$message.error("请上传营业执照");
          return
        }
        if (this.tempArticle.logoUrl == '') {
          this.$message.error("请上传logo图");
          return
        }
        this.tempArticle.areaIdOne = this.tempArticle.area[0]
        this.tempArticle.areaIdTwo = this.tempArticle.area[1]
        this.tempArticle.areaIdThree = this.tempArticle.area[2]
        this.tempArticle.area = ""
        //修改文章
        this.api({
          url: "/shop/updateShop",
          method: "post",
          data: this.tempArticle
        }).then(() => {
          this.$message({
            message: "修改成功",
            type: 'success',
            duration: 1 * 1000,
          })
          this.getList();
          this.dialogFormVisible = false
        })
      },
      changeValue(value, index) {
        if (this.list[index].state == 2 || this.list[index].state == 3) {
          return
        }
        let name = ""
        if (value == 2) {
          name = "审核通过"
        } else if (value == 3) {
          name = "拒绝通过"
        }
        this.$confirm('确定' + name + '?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          this.list[index].stateName = name
          this.list[index].state = value
          this.listLoading = true;
          this.api({
            url: "/shop/auditShop",
            method: "post",
            params: this.list[index]
          }).then(() => {
            this.listLoading = false;
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
          this.tempArticle.imgUrl = data.join(",")
        })
      },
      upload1(e) {
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
          this.tempArticle.logoUrl = data.join(",")
        })
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;
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

  .el-switch {
    display: -webkit-inline-box;
    display: -ms-inline-flexbox;
    display: inline-flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    position: relative;
    font-size: 14px;
    line-height: 20px;
    height: 20px;
    vertical-align: middle;
    margin-bottom: 8px;
  }

  .inputSerach {
    width: 10%;
  }
</style>
