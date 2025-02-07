import request from "."

export const getUname = (uid: number) => {
  return request.get("showUname", {
    params: {
      id: uid
    }
  })
}