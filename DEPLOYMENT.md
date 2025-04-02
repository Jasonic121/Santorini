# Deployment Guide for Santorini Game

This guide provides instructions for deploying the Santorini game with:
- Frontend: GitHub Pages
- Backend: Render

## Frontend Deployment (GitHub Pages)

1. Install the GitHub Pages package in your frontend directory:
   ```bash
   cd frontend
   npm install --save-dev gh-pages
   ```

2. Update the `package.json` file in the frontend directory:
   ```json
   {
     "name": "frontend",
     "version": "0.1.0",
     "private": true,
     "homepage": "https://[YOUR_GITHUB_USERNAME].github.io/santorini-game",
     "dependencies": {
       // existing dependencies...
     },
     "scripts": {
       "start": "react-scripts start",
       "build": "react-scripts build",
       "test": "react-scripts test",
       "eject": "react-scripts eject",
       "predeploy": "npm run build",
       "deploy": "gh-pages -d build"
     },
     // rest of the file...
   }
   ```
   Replace `[YOUR_GITHUB_USERNAME]` with your actual GitHub username.

3. Create a file called `src/config.js` in your frontend project to store the backend URL:
   ```javascript
   // config.js
   const config = {
     backendUrl: process.env.NODE_ENV === 'production' 
       ? 'https://[YOUR_RENDER_SERVICE_NAME].onrender.com' 
       : 'http://localhost:8080'
   };

   export default config;
   ```
   Replace `[YOUR_RENDER_SERVICE_NAME]` with your actual Render service name.

4. Update your API calls in the frontend to use this configuration:
   ```javascript
   import config from '../config';

   // Example API call
   fetch(`${config.backendUrl}/api/endpoint`)
     .then(response => response.json())
     .then(data => console.log(data));
   ```

5. Deploy to GitHub Pages:
   ```bash
   npm run deploy
   ```

6. Configure GitHub repository:
   - Go to your GitHub repository
   - Navigate to Settings > Pages
   - Set Source to "GitHub Actions"
   - Your site will be published at `https://[YOUR_GITHUB_USERNAME].github.io/santorini-game`

## Backend Deployment (Render)

1. Create a `Procfile` in the root of your backend directory:
   ```
   web: java -jar target/santorini-backend-1.0.0.jar
   ```

2. Create a new Web Service on Render:
   - Sign up for a Render account at https://render.com
   - Click "New" and select "Web Service"
   - Connect your GitHub repository
   - Configure the service:
     - Name: `santorini-backend` (or your preferred name)
     - Root Directory: `backend`
     - Runtime: `Java`
     - Build Command: `mvn clean package`
     - Start Command: `java -jar target/santorini-backend-1.0.0.jar`
   - Click "Create Web Service"

3. Enable CORS in your backend application to allow requests from your GitHub Pages domain.
   
   In your Java backend, add the following configuration:
   ```java
   // Example for Spring Boot
   @Configuration
   public class WebConfig implements WebMvcConfigurer {
       @Override
       public void addCorsMappings(CorsRegistry registry) {
           registry.addMapping("/**")
               .allowedOrigins("https://[YOUR_GITHUB_USERNAME].github.io")
               .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
               .allowCredentials(true);
       }
   }
   ```

4. Once deployed, Render will provide you with a URL for your backend service. Use this URL in your frontend's `config.js` file.

## Testing Your Deployment

1. Navigate to your GitHub Pages URL: `https://[YOUR_GITHUB_USERNAME].github.io/santorini-game`
2. Verify that the frontend can communicate with the backend
3. Test the game functionality to ensure everything works as expected

## Troubleshooting

- **CORS Errors**: If you encounter CORS errors, verify your backend CORS configuration is correct and includes your GitHub Pages domain.
- **API Connection Issues**: Check that your `config.js` file is correctly set up with the right Render service URL.
- **Deployment Failures**: Check the deployment logs on both GitHub and Render for specific error messages.

## Notes

- GitHub Pages hosts static content only, so your frontend must be built to work with a separate backend API.
- Render's free tier may have limitations on availability and performance.
- Consider implementing environment variables for more secure configuration management. 