FROM nginx:latest

COPY default.conf /etc/nginx/conf.d/
COPY dist/ /usr/share/nginx/html/
EXPOSE 80
COPY mana/dist /usr/share/nginx/mana/html
EXPOSE 90
