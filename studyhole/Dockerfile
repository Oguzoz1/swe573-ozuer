FROM node:20.11.1 as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --prod

FROM nginx:alpine
COPY --from=build /app/dist/studyhole /usr/share/nginx/html
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]
