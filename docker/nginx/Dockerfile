FROM node:22.16.0-alpine3.21 AS build-user
WORKDIR /app
COPY shareNovel-vue/ .
RUN npm install -g pnpm && pnpm install --no-frozen-lockfile && pnpm build

FROM node:22.16.0-alpine3.21 AS build-admin
WORKDIR /app
COPY shareNovelManager-vue/ .
RUN npm install -g pnpm && pnpm install --no-frozen-lockfile && pnpm build

FROM nginx:alpine
COPY docker/nginx/default.conf /etc/nginx/conf.d/default.conf
COPY --from=build-user /app/dist /usr/share/nginx/html
COPY --from=build-admin /app/dist /usr/share/nginx/admin
EXPOSE 80 90
