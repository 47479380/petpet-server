### 调用方式

#### 根据qq号下载头像生成petpet

```http request
POST http://localhost:2333/petpet/generate/qq
Content-Type: application/x-www-form-urlencoded

key=petpet&toAvatar=2752181597
###
```
#### 根据url下载头像生成petpet
```http request
POST http://localhost:2333/petpet/generate/url
Content-Type: application/x-www-form-urlencoded


key=petpet&toAvatar=https://q1.qlogo.cn/g?b=qq&s=100&nk=2752181597
###
```
#### 上传文件生成 petpet
```http request
POST http://localhost:2333/petpet/generate/post
Content-Type: multipart/form-data; boundary=WebAppBoundary

--WebAppBoundary
Content-Disposition: form-data; name="toAvatar"; filename="toAvatar.jpg"

< ./2752181597.jpg
--WebAppBoundary--
Content-Disposition: form-data; name="key"

petpet
--WebAppBoundary--
###
```
#### 参数说明
| 参数名 | 数据类型   | 默认值  | 说明 |
| ----- | --------- | --- | ------------ |
| `key` | `string` |   `没有`    |     需要生成的key     |
| `fromAvatar` | `string` 或 `file` |   `没有`    |     发送者头像     |
| `toAvatar`  | `string` 或 `file`  | `没有` |      接收者头像, 或构造的图片   |
| `groupAvatar`  | `string` 或 `file`  | `没有` |     群头像   |
| `botAvatar`  | `string` 或 `file`  | `没有` |     机器人头像   |
| `fromName`  | `string`  | `没有` |     发送者   |
| `toName`  | `string`  | `没有` |     接收者   |
| `groupName`  | `string`  | `没有` |     群名称   |
| `textList`  | `string`或`string[]`  | `没有` |      文本变量, 可用于生成meme图    |

#### 响应
如果响应码是`200`，说明生成成功，直接返回图片数据。
如果响应码不是`200`，说明生成失败，返回`json`数据


| 字段 | 数据类型   |说明 |
| ----- | --------- | ------------ |
| `code` | `Integer` | 响应码(没啥用) |
| `message` | `string` | 错误信息 |

#### 获取所有的key
```http request
GET http://localhost:2333/petpet/keys
Accept: application/json

###
```
#### 获取所有的alias
```http request
GET http://localhost:2333/petpet/alias
Accept: application/json

###
```
#### 获取某个key的data.json
get请求`http://localhost:2333/petpet/key/` + key的名称

例如获取petpet的data.json
```http request
GET http://localhost:2333/petpet/key/petpet
Accept: application/json

###
```
