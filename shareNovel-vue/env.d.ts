/// <reference types="vite/client" />
interface ImportMetaEnv {
    readonly VITE_PAGE_SIZE: number
    readonly VITE_LOCALSTORAGE_USER: string
    readonly VITE_MESSAGE_RELOGIN: string
    readonly VITE_LOCALSTORAGE_NOVEL: string
    readonly VITE_LOCALSTORAGE_NOVEL_VIEW: string
  }
  
  interface ImportMeta {
    readonly env: ImportMetaEnv
  }