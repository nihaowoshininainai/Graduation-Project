/// <reference types="vite/client" />
interface ImportMeta {
    readonly env: ImportMetaEnv
}

interface ImportMetaEnv {
    readonly VITE_NOVEL_WEB_URL: string
}
