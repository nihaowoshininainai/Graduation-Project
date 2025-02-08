import request from "."

export const getIndexNovel = (current:number,size:number,condition?:string,sort?:string)=>{
    return request.get("index",{
        params:{
            current:current,
            size:size,
            condition:condition,
            sort:sort
        }
    })
}