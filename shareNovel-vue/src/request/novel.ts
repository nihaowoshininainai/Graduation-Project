import type { Novel, Result, User } from "@/baseType"
import request from "."
import router from "@/router"

//获取小说
export const getIndexNovel = (current: number, size: number, condition?: string, sort?: string, labelId?: number) => {
    if (labelId && labelId !== -1) {
        return request.get('classify', {
            params: {
                current,
                size,
                condition,
                sort,
                labelId
            }
        })
    }
    return request.get("index", {
        params: {
            current,
            size,
            condition,
            sort
        }
    })
}

//获取用户小说
export const getUserNovel = (uid: number) => {
    return request.get("showNovels", {
        params: {
            uid: uid
        }
    })
}

//获取用户收藏小说
export const getUserCollectNovel = (uid: number) => {
    return request.get("showCollections", {
        params: {
            uid: uid
        }
    })
}

type UserToNovel = {
    user: User,
    novel: Novel
}

//上传小说
export const uploadUserNovel = async (data: UserToNovel) => {
    const res = await request.post("addNovel", data)
    const result: Result = res.data
    if (result.state === '0')
        if (result.message === import.meta.env.VITE_MESSAGE_RELOGIN)
            ElMessage.error({
                message: result.message,
                duration: 1000,
                onClose: () => {
                    document.cookie += ";max-age=0"
                    localStorage.removeItem(import.meta.env.VITE_LOCALSTORAGE_USER)
                    localStorage.setItem(import.meta.env.VITE_LOCALSTORAGE_NOVEL, JSON.stringify(data.novel))
                    router.go(0)
                }
            })

        else
            ElMessage.error(result.message)

    else
        ElMessage.success(result.message)

    return result
}

//获取小说
export const getNovelDetail = (novelId: number) => {
    return request.get("showNovel", {
        params: {
            nid: novelId
        }
    })
}

//举报小说
export const reportNovel = (novel: Novel) => {
    if(document.cookie===""){
        ElMessage.error("请先登录")
        return
    }
    return request.post("reportNovel", novel).then(res => {
        const result: Result = res.data
        if (result.state === '0')
            ElMessage.error(result.message)
        else
            ElMessage.success(result.message)
    }
    )

}
