<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <div class="demo-input-size">
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
    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="orderId" label="订单号" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="storeName" label="商家" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="stateName" label="状态" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="count" label="购买数量" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="discountPay" label="优惠价格(元)" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="practicePay" label="实付(元)" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="createTime" label="添加时间" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="管理" width="200" v-if="hasPerm('goods:update')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow" :total="totalCount" :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      111
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
        textMap: {
          create: '订单详情'
        },
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
        dialogImageUrl: '',
        dialogVisible: false,
        //0 取消订单1 待支付 2.待发货 3.待收货 4.交易失败 5.交易完成 6.下单成功 7下单失败 10退款中 11退款完成  12退货中
        optionsState: [{
            value: '0 ',
            label: '已取消'
          }, {
            value: '1',
            label: '待支付',
            disabled: true
          }, {
            value: '2',
            label: '待发货'
          }, {
            value: '3',
            label: '待收货'
          }, {
            value: '4',
            label: '交易失败'
          },
          {
            value: '5',
            label: '交易完成'
          },
          {
            value: '10',
            label: '退款中'
          },
          {
            value: '11',
            label: '退款完成'
          },
          {
            value: '12',
            label: '退货中'
          },
        ],
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
        if (this.listQuery.createTime != null && this.listQuery.createTime.length > 0) {
          this.listQuery.startTime = this.listQuery.createTime[0]
          this.listQuery.endTime = this.listQuery.createTime[1]
          this.listQuery.createTime = this.listQuery.createTime.join(",")
        }
        //查询列表
        if (!this.hasPerm('order:list')) {
          return
        }
        this.listLoading = true;
        this.api({
          url: "/order/getOrderInfoList",
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
            item.state1 = item.state
            if (item.state == 0 || item.state == 4) {
              item.flag = true
            } else {
              item.flag = false
            }
            if (item.state == 0) {
              item.auditFlag = false
            } else {
              item.auditFlag = true
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

      showUpdate($index) {
        //显示修改对话框
        this.dialogFormVisible = true

      },
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
