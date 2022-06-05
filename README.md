# petpet

一个生成摸摸头GIF的 Mirai 插件，灵感/数据来自 [nonebot-plugin-petpet](https://github.com/noneplugin/nonebot-plugin-petpet)。

java 编写，**未使用任何第三方库** ：轻量，高效。

## 使用方法

1. 下载 [最新版本](https://github.com/Dituon/petpet/releases/)

2. 将插件放入 `Mirai/plugins/`

3. 下载 [图片素材](https://github.com/Dituon/petpet/tree/main/data/xmmt.dituon.petpet)

4. 将图片素材放入 `Mirai/data/xmmt.dituon.petpet`

5. 使用 **戳一戳** 有 `30%` 的概率触发; 或发送 `pet @xxx`

> `pet @xxx` 后跟 `key` 可以返回指定图片 例如 `pet @xxx kiss`
>> 启用 `keyCommand` 后 上述指令可简写为 `kiss @xxx`

> 启用 `respondImage` 后 可通过发送的图片生成Petpet `pet [图片] kiss`

## 配置文件

首次运行 Petpet 插件时，会生成 `Mirai/config/xmmt.dituon.petpet/Petpet.yml` 文件

```
content: 
  version: 2.4 #配置文件版本
  command: pet #触发 petpet 的指令
  probability: 30 #使用 戳一戳 的触发概率
  antialias: false #抗锯齿
  disabled: [] #禁用列表
  keyCommand: false #以 key 作为指令头
  respondImage: false #使用发送的图片生成 petpet
```

修改后重启 Mirai 以重新加载

## 权限管理

> 群主或管理员使用 `pet on` `pet off` 以 启用/禁用 插件

> 可在配置文件中禁用指定key, 被禁用的key不会随机触发, 但仍可以通过指令使用

## 图片预览

**图片按key排序(见`data/xmmt.dituon.petpet/`)**

<details>
<summary>展开/收起</summary>

| key | 预览 |
| --- | --- |
| kiss | ![image](img/0.gif) |
| rub | ![image](img/1.gif) |
| throw | ![image](img/2.gif) |
| petpet | ![image](img/3.gif) |
| play | ![image](img/4.gif) |
| roll | ![image](img/5.gif) |
| bite | ![image](img/6.gif) |
| twist | ![image](img/7.gif) |
| pound | ![image](img/8.gif) |
| thump | ![image](img/9.gif) |
| knock | ![image](img/10.gif) |
| suck | ![image](img/11.gif) |
| hammer | ![image](img/12.gif) |
| tightly | ![image](img/13.gif) |

</details>

## 自定义

### data.json

`./data/xmmt.dituon.petpet/` 下的目录名为 `key` ，插件启动时会遍历 `./data/xmmt.dituon.petpet/$key/data.json`

`data.json` 标准如下 (以 `thump/data.json` 为例)

```
{
  "type": "GIF", // 图片类型(enum)
  "avatar": "SINGLE", // 头像数(enum)
  "pos": [ // 坐标
    [65, 128, 77, 72], [67, 128, 73, 72], [54, 139, 94, 61], [57, 135, 86, 65]
  ],
  "text": [], // 文字
  "round": true, // 值为true时 会将头像裁切为椭圆形
  "rotate": false, // 值为true时 GIF头像会旋转
  "avatarOnTop": false // 值为true时 头像图层在顶端
}
```

#### 类型枚举

**`type`**

- `GIF`  动图
- `IMG`  静态图片

**`avatar`**

- `SINGLE`  单头像
- `DOUBLE`  双头像

#### 坐标

坐标的基本组成单位是 4长度 `int[]` 数组

其中，前两项为 **左上角顶点坐标**， 后两项为 **宽度和高度**

例: 
`[65, 128, 77, 72]` 即 头像的左上角顶点坐标是 `(65,128)`, 宽度为 `77`, 高度为 `72`

如果是 `GIF` 类型，坐标应为二维数组，`GIF` 的每一帧视为单个图像文件
```
"type": "GIF", // GIF 类型
"avatar": "SINGLE", // 单头像
"pos": [ // pos的元素对应GIF的4帧
    [65, 128, 77, 72], [67, 128, 73, 72], [54, 139, 94, 61], [57, 135, 86, 65]
  ],
```

如果是 `DOUBLE` 类型，应有2个数组
```
  "type": "GIF", // GIF 类型
  "avatar": "DOUBLE", // 双头像
  "pos": [ // 两个子数组对应两个头像
    [ // 元素对应GIF的6帧
      [102, 95, 70, 80], [108, 60, 50, 100], [97, 18, 65, 95],
      [65, 5, 75, 75], [95, 57, 100, 55], [109, 107, 65, 75]
    ],
    [ // 元素对应GIF的6帧
      [39, 91, 75, 75], [49, 101, 75, 75], [67, 98, 75, 75],
      [55, 86, 75, 75], [61, 109, 75, 75], [65, 101, 75, 75]
    ]
  ]
```
发送者头像对应第1个数组, 接收者对应第2个数组, **发送者头像图层在接收者头像之上**

相信你已经明白了坐标的格式规范, 下面还有两个例子

```
  "type": "IMG", // IMG 类型
  "avatar": "SINGLE", // 单头像
  "pos": [0, 0, 200, 200]
```
```
  "type": "IMG", // IMG 类型
  "avatar": "DOUBLE", // 双头像
  "pos": [[0, 0, 200, 200] , [150, 160, 200, 200]]
```

#### 文字

如果你想在图片上添加文字，可以编辑 `text`

```
"text": [ // 这是一个数组, 可以添加很多文字
  {
    "text": "Petpet!", // 文字内容
    "color": "#66ccff", // 颜色, 默认为#191919
    "pos": [100, 100], // 坐标, 默认为 [2,14]
    "size": 24 // 字号, 默认为12
  },
  {
    "text": "发送者: $from, 接收者: $to", // 支持变量
    "color": [0,0,0,255], // 颜色可以使用RGB或RGBA的格式
    "pos": [20, 150], // 坐标
    "font": "宋体" // 字体, 默认为黑体
  }
  ]
```

**`变量`**

- `$from` : 发送者, 会被替换为发送者群名片，如果没有群名片就替换为昵称
- `$to` : 接收者, 被戳或At的对象, 发送图片构造时为"你"
- `$group` : 群名称

**需要更多变量请提交 Issue**

## 常见问题

> 戳一戳无法触发?
>> 检查 Mirai 登录协议, 仅 `ANDORID_PHONE` 可以收到 戳一戳 消息

> `NoClassDefFoundError`?
>> `Mirai 2.11.0` 提供了新的 `JavaAutoSaveConfig` 方法, 请更新Mirai版本至 `2.11.0` (不是`2.11.0-M1`), 或使用本插件 `2.0` 及以下版本

> `Exception in coroutine <unnamed>`?
>> 图片素材应位于 `Mirai/data/xmmt.dituon.petpet` 目录下, 请检查路径

> `YamlDecodingException`?
>> 配置文件中不能包含注释，最简单的解决方法是删除配置文件让插件自动生成

> 文字构造乱码?
>> `Linux` 系统 可能缺少中文字体, 使用 `fc-list` 列出已安装的字体; `Windows` 系统 可能是文件编码问题, 更改 `data.json` 编码 或加入`-Dfile.encoding=utf-8` 启动项

## 分享你的作品

如果你想分享自定义的 Petpet, **欢迎Pr**

## 后话

对此插件进行二次开发比你想象的简单很多，我认为这是初学者入门 Mirai 开发的不二选择。

如果此插件和您预期的一样正常工作，请给我一个 `star`

欢迎提交任何请求

交流群: `534814022`
