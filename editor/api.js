const baseUrl="http://localhost:2333"
async function getKeys() {
     return   fetch(`${baseUrl}/petpet/keys`)
}
async function getAlias(){
    return   fetch(`${baseUrl}/petpet/alias`)
}
async function getKey(key){
   return  fetch(`${baseUrl}/petpet/key/${key}`)
}

 async function postBuildData(data) {
    return  await fetch(`${baseUrl}/editor/generate`, {
            method: "POST",
            body: data,
        })


 }
export {getKey,getKeys,getAlias,postBuildData}