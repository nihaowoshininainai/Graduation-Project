import request from "."

enum Condition{
    time,
    hot
}

enum Sort{
    asc,
    desc
}

export const getIndexNovel = (current:number,size:number,condition?:Condition,sort?:Sort)=>{
    return request.get("index",{
        params:{
            current:current,
            size:size,
            condition:condition,
            sort:sort
        }
    })
}