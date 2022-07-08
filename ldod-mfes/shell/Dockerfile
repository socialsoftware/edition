FROM node:18.4-alpine
WORKDIR /shell
COPY package.json ./
RUN yarn install
EXPOSE 9000
COPY . .