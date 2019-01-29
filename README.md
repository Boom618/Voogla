# Voogla

RxJava 操作符：


待解决：

- 请求嵌套问题
- 延迟过滤
- 入库箱码产品码逻辑需改进
- iView.showSuccess(response) 如果有多个接口, T 对应不上
- 文件读写改用 okIo

项目说明：

-----

- SharedP ：SharedPreferences 形式存值。GoodNo
- SimpleCache : ACache 形式存值。存 putString、UserInfo、ArrayList<QrCodeListData>
- SparseArrayUtil：文件形式存值。 List<QrCodeListData>
- 代码抽取 Ctrl + Alt + M （Refactor -> Extract -> Function）

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

RecyclerView 问题：

- RecyclerView 在 `android.support.constraint.ConstraintLayout` 约束布局中数据超过一屏展示不全

解决方案：

含有 `RecyclerView` 的控件放在外层改为`LinearLayout`或其他常用 Layout 。

总结：

- 参与接口设计
- 数据结构在设计的时候与界面操作尽量保持一致：比如
    - 获取详情结构尽量与保存提交的时候一致
