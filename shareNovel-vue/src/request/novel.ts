import request from "."

export const getIndexNovel = (current: number, size: number, condition?: string, sort?: string, labelId?: number) => {
    if (labelId && labelId !== -1)
    {
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