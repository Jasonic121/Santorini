/**
 * Configuration for the Santorini game frontend
 * Contains environment-specific settings like backend URL
 */

const config = {
  /**
   * Backend URL that changes based on environment
   * - Development: local server
   * - Production: Render hosted service
   */
  backendUrl: process.env.NODE_ENV === 'production' 
    ? 'https://santorini-backend.onrender.com' 
    : 'http://localhost:8080'
};

export default config; 