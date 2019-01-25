# Voogla

RxJava 操作符：


待解决：

- 请求嵌套问题
- 延迟过滤
- 入库箱码产品码逻辑需改进
- iView.showSuccess(response) 如果有多个接口, T 对应不上

项目说明：

-----

- SharedP ：SharedPreferences 形式存值。GoodNo
- SimpleCache : ACache 形式存值。存 putString、UserInfo、ArrayList<QrCodeListData>
- SparseArrayUtil：文件形式存值。 List<QrCodeListData>

RecyclerView 封装：

- 单击
- 长按
- 头部和尾部
- 单选
- 多选
- 刷新和加载


问题：

- 入库
1. 规格为 5，不足 5 盒可以箱码绑定吗？