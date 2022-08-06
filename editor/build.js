import {postBuildData} from "./api.js";

const parameterMap={
    "FORM":"fromAvatar",
    "TO":"toAvatar",
    "GROUP":"groupAvatar",
    "BOT":"botAvatar"
}
let backGroundImageData;
function parseTextarea(){
    console.log(buildData())
    return JSON.parse(buildData())
}
function getFrames(){
   const img_frame=document.querySelectorAll(".frame")
   return new Array(...img_frame).map(el=>{
        return {name:el.id,data:base64ToBlob(el.src)}
    })
}
function base64ToBlob(urlData) {
    let arr = urlData.split(',');
    let mime = arr[0].match(/:(.*?);/)[1] || 'image/png';
    // 去掉url的头，并转化为byte
    let bytes = window.atob(arr[1]);
    // 处理异常,将ascii码小于0的转换为大于0
    let ab = new ArrayBuffer(bytes.length);
    // 生成视图（直接针对内存）：8位无符号整数，长度1个字节
    let ia = new Uint8Array(ab);

    for (let i = 0; i < bytes.length; i++) {
        ia[i] = bytes.charCodeAt(i);
    }

    return new Blob([ab], {
        type: mime
    });
}
const box = document.getElementById('drop_area');
const file = document.getElementById('file');
//绑定文件上传事件,获取背景图片
file.addEventListener("change",function (){
       onBackGroundDrop(this.files)
})
box.addEventListener("drop", e => {
    const fileList = e.dataTransfer.files;
    onBackGroundDrop(fileList)
});
function onBackGroundDrop(fileList){
    if (fileList.length === 0) {
        return false;
    }
    if (fileList[0].type.indexOf('image') === -1) {
        return false;
    }
    if (fileList[0].type === 'image/gif') {
        return false;
    }
    const reader=new FileReader()
    reader.readAsArrayBuffer(fileList[0]);
    reader.onload = () => {

        backGroundImageData =  reader.result
    }
}

function toImage(arrayBuffer) {
    const img=new Image()
    img.src=URL.createObjectURL( new Blob([arrayBuffer]))
    img.onload=()=>{

        $(".result").html(img)
    }

}

const buildImage=document.getElementById("buildImage")
buildImage.addEventListener("click",async   (evt) =>{
        const upper_canvas=document.querySelector(".upper-canvas")
        const data = parseTextarea();
        const formData= new FormData()
        if (data.type==="IMG"){

            formData.append("f1",new Blob([backGroundImageData]))
        }else if (data.type==="GIF"){
            getFrames().forEach(value => {
                formData.append(value.name,value.data)
            })
        }else {
            return
        }
         textList.map(({text})=>{
            return text.text;
        }).forEach(text=>{
            formData.append("textList",text)
         });
        formData.append("data",JSON.stringify(data))

        const response= await  postBuildData(formData)
        if (response.ok){
            toImage(await response.arrayBuffer())
            return
        }
    const result=  await response.json()
    alert(result.msg)


    })






