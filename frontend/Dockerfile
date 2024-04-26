# Use a base image with Node.js installed
FROM node:14-slim

# Set the working directory in the container
WORKDIR /app

# Copy the package.json and package-lock.json files to the working directory
COPY package*.json ./

# Install the project dependencies
RUN npm install

# Copy the project source code to the working directory
COPY . .

# Build the frontend application
RUN npm run build

# Use a lightweight base image for serving the frontend
FROM nginx:alpine

# Copy the built frontend files from the previous stage
COPY --from=0 /app/build /usr/share/nginx/html

# Expose the port on which the frontend will run
EXPOSE 80

# Start the nginx server
CMD ["nginx", "-g", "daemon off;"]