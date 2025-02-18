import type { NovelLabel } from "@/baseType";
import request from ".";

export const uploadNovelLabels = (novelLabels: NovelLabel[]) => {
    return request.post("addLabel", novelLabels);
}