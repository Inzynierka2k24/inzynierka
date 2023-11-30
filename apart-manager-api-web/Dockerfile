### STAGE 1: Build ###
FROM node:latest as build
WORKDIR /usr/src/app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build

### STAGE 2: Run ###
FROM nginx:latest
COPY --from=build /usr/src/app/dist/apart-manager-api-web /usr/share/nginx/html

EXPOSE 80
