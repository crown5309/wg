<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <div class="demo-input-size">
            <span>订单号:</span>
            <el-input placeholder="请输入订单号" v-model="listQuery.orderId" class="inputSerach" clearable>
              <i slot="prefix" class="el-input__icon el-icon-search"></i>
            </el-input>
            <span>状态:</span>
            <el-select v-model="listQuery.state" placeholder="请选择" clearable>
              <el-option v-for="item in optionsState" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
            <span>添加时间:</span>
            <el-date-picker v-model="listQuery.createTime" type="datetimerange" range-separator="至" start-placeholder="开始日期"
              end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
            <el-button type="primary" icon="el-icon-search" @click="getList" v-if="hasPerm('order:list')">搜索</el-button>
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
      <el-table-column align="center" prop="orderId" label="订单号" ></el-table-column>
      <el-table-column align="center" prop="storeName" label="商家" ></el-table-column>
      <el-table-column align="center" prop="stateName" label="状态" ></el-table-column>
      <el-table-column align="center" prop="count" label="购买数量" ></el-table-column>
      <el-table-column align="center" prop="totalPay" label="总价格(元)" ></el-table-column>
      <el-table-column align="center" prop="discountPay" label="优惠价格(元)" ></el-table-column>
      <el-table-column align="center" prop="practicePay" label="实付(元)" ></el-table-column>
      <el-table-column align="center" prop="createTime" label="添加时间" ></el-table-column>
      <el-table-column align="center" label="管理" width="300" v-if="hasPerm('order:list')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)">查看</el-button>
          <el-button type="primary" icon="edit" @click="showUpdate1(scope.$index)" v-if="scope.row.state==2&&scope.row.showFaHuo">
            发货
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow" :total="totalCount" :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <div>
        <span>订单信息</span>
        <el-divider></el-divider>
        <div class="orderInfo">
          <span>订单号:</span> <span>{{good.orderId}}</span>
          <span>状态:</span> <span>{{good.stateName}}</span>
          <span>总价(元):</span> <span>{{good.totalPay}}</span>
          <span>优惠(元):</span> <span>{{good.discountPay}}</span>
          <span>实付(元):</span> <span>{{good.practicePay}}</span>
        </div>
      </div>
      <el-divider></el-divider>
      <div v-if="good.addressId">
        <span>收货人信息</span>
        <el-divider></el-divider>
        <div class="orderInfo">
          <span>姓名:</span>
          <span v-show="showAddress">{{good.name}}</span>
          <span v-show="!showAddress" class="spanClass">
            <el-input type="text" :value="good.name" v-model="good.name">

            </el-input>
          </span>
          <span>手机号:</span>
          <span v-show="showAddress">{{good.telephone}}</span>
          <span v-show="!showAddress" class="spanClass">
            <el-input type="text" :value="good.telephone" v-model="good.telephone" @keyup="check"></el-input>
          </span>
          <span>地址:</span>
          <span v-show="showAddress">{{good.province}}{{good.city}}{{good.country}}{{good.detail}}</span>
          <span v-show="!showAddress" class="optionsDetailClass">
            <el-cascader v-model="value" :options="options" :props="{ expandTrigger: 'hover' }" @change="handleChange"></el-cascader>
            <div class="orderInfo1">
              <span>详细地址:</span><span>
                <el-input type="text" :value="good.detail" v-model="good.detail"></el-input>
              </span>
              <el-button type="success" icon="el-icon-close" circle v-show="!showAddress" @click="changeAddressShow"></el-button>
              <el-button type="success" icon="el-icon-check" circle v-show="!showAddress" @click="changeAddress"></el-button>
            </div>
          </span>
          <el-button type="primary" icon="el-icon-edit" circle :disabled="good.logisticsNo" v-show="showAddress" @click="changeAddressShow"></el-button>
        </div>
      </div>
      <el-divider v-if="good.addressId"></el-divider>
      <div>
        <span>商品信息</span>
        <el-divider></el-divider>
        <el-table :data="goodList" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
          highlight-current-row class="tableClass">
          <el-table-column align="center" label="序号">
            <template slot-scope="scope">
              <span v-text="scope.$index"> </span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="goodsName" label="商品名"></el-table-column>
          <el-table-column align="center" prop="count" label="购买数量"></el-table-column>
          <el-table-column align="center" prop="price" label="价格(元)"></el-table-column>
        </el-table>
      </div>
      <el-divider></el-divider>
      <div v-if="good.logisticsNo">
        <span>物流信息-{{good.wuLiu}}(快递号：{{good.logisticsNo}})</span>
        <el-divider></el-divider>
        <el-timeline v-if="activities">
          <el-timeline-item v-for="(activity, index) in activities" :key="index" :timestamp="activity.time">
            {{activity.context}}
          </el-timeline-item>
        </el-timeline>
        <span  v-else> <el-button type="primary" icon="edit" @click="showUpdate2()">
            <span>重新发货</span>
          </el-button></span>
      </div>
    </el-dialog>
    <el-dialog title="发货" :visible.sync="dialogFormVisible1">
      <el-form class="small-space" :model="goodsClass" label-position="left" label-width="80px" style='width: 300px; margin-left:80px;'>
        <el-form-item label="选择物流">
          <el-select v-model="goodsClass.logisticsType" placeholder="请选择">
            <el-option v-for="item in logisticsList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="快递单号">
          <el-input type="text" v-model="goodsClass.logisticsNo" placeholder="请输入快递单号">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible1 = false">取 消</el-button>
        <el-button type="success" @click="submitWuLiu()">提交</el-button>
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
        dialogFormVisible1: false,
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
        reverse: true,
        activities: [],
        goodList: [],
        good: {},
        showAddress: true,
        options: [],
        value: [],
        goodsClass: {
          logisticsType:'',
          logisticsNo:'',
          orderId:''
        },
        logisticsList:[],
        index:''
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
        this.index=$index
        this.dialogFormVisible = true
        this.goodList = this.list[$index].goodsList
        this.good = this.list[$index]
        if (this.good.logisticsNo) {
          this.listLoading = true;
          this.api({
            url: "/order/getOrderLogistics",
            method: "get",
            params: {
              logisticsNo: this.good.logisticsNo,
              logisticsType: this.good.logisticsType
            }
          }).then(data => {
            this.listLoading = false;
            this.activities = data.data
          });
        }
      },
      showUpdate1($index) {
        //显示修改对话框
         this.index=$index
         this.good = this.list[$index]
        this.dialogFormVisible1 = true
        this.api({
          url: "/area/listWuLiu",
          method: "get",
        }).then(data => {
          this.listLoading = false;
          this.logisticsList = data
        });
      },
      showUpdate2(){
        this.showUpdate1(this.index)
      },
      changeAddressShow() {
        this.value = []
        this.value.push(this.good.province)
        this.value.push(this.good.city)
        this.value.push(this.good.country)
        this.showAddress = !this.showAddress
        this.listLoading = true;
        this.api({
          url: "/area/listArea",
          method: "get",
        }).then(data => {
          console.log(data)
          this.listLoading = false;
          this.options = data
        });
      },
      submitWuLiu($index){
        if(this.goodsClass.logisticsType==''){
          this.$message.error("请选择物流");
          return
        }
        if(this.goodsClass.logisticsNo==''){
          this.$message.error("请输入快递单号");
          return
        }
        this.goodsClass.orderId=this.good.orderId
        this.listLoading = true;
        this.api({
          url: "/area/updateOrder",
          method: "post",
          data:this.goodsClass
        }).then(data => {
          this.$message({
            message: "发货成功",
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              this.getList();
              this.dialogFormVisible1 = false
              this.listLoading = false;
            }
          })
          this.getList()
        });
      },
      changeAddress() {
        if (this.check()) {
          this.$message.error("手机号不正确");
          return
        }
        this.api({
          url: "/area/updateArea",
          method: "post",
          data: this.good
        }).then(data => {
          this.getList();
          this.showAddress = !this.showAddress
        });
      },
      handleChange() {
        this.good.province = this.value[0]
        this.good.city = this.value[1]
        this.good.country = this.value[2]
      },
      check() {
        let reg = /^1[3|4|5|7|8][0-9]{9}$/;
        //校验手机号规则
        //如果校验不通过会返回false，如果校验通过会返回true
        if (!reg.test(this.good.telephone)) {
          //修改状态值方便上面的视图层判断展示
          return true;
        }
      }
    }
  }
</script>
<style>
  .orderInfo {
    margin-top: 15px;
    margin-left: 15px;
  }

  .orderInfo1 {
    margin-top: 15px;
  }

  .orderInfo1 span {
    padding-right: 25px;
  }

  .orderInfo1 .el-input {
    width: 25%;
  }

  .orderInfo span {
    padding-right: 25px;
  }

  .spanClass .el-input {
    width: 15%;
  }

  .optionsDetailClass span {
    padding-right: 0px;
  }

  .optionsDetailClass {
    padding-right: 0px;
  }

  .optionsDetailClass .el-input {}

  .orderInfo .el-input__inner {
    height: 35px;
  }

  .tableClass {
    font-size: 13px;
  }

  .inputSerach {
    width: 10%;
  }
</style>
