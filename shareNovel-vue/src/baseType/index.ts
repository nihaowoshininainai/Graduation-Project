export type Novel = {
    id?:number,
    title:string,
    content:string,
    createTime?:string,
    uid?:number,
    views?:number,
    state?:number
}

export type Label = {
    id:number,
    name:string
}

export type User = {
    uid?:number,
    createTime?:string,
    pwd?:string,
    uname:string,
    banned?:number
}

export type Result = {
    state:string,
    message:string,
    data:any
}

export type NovelLabel = {
    id?:number,
    nid:number,
    lid:number
}