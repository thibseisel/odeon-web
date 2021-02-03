FROM node:14-alpine

WORKDIR /app

COPY ./package*.json ./
RUN npm install --no-progress
COPY . .

CMD ["npm", "run", "docker-start"]
