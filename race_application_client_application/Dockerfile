FROM node:18.12.0 as builder
WORKDIR /app
COPY race_application_client_application/package*.json ./
RUN npm ci
COPY race_application_client_application/ ./
RUN npm run build -- --configuration production

FROM nginx:alpine
COPY --from=builder /app/dist/race_application_client_application /usr/share/nginx/html
COPY race_application_client_application/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
