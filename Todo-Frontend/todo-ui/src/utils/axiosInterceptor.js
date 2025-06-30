
import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8081/api', // ✅ Ensure correct backend URL
  headers: { 'Content-Type': 'application/json' }, // ✅ Default headers
});

// ✅ Attach Authorization token before each request
axiosInstance.interceptors.request.use(
  (config) => {
    // Get the token from localStorage or sessionStorage
    const token = localStorage.getItem('accessToken') || sessionStorage.getItem('accessToken');
    
    if (token) {
      // If token exists, add it to the Authorization header
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);


// ✅ Handle authentication & authorization errors
axiosInstance.interceptors.response.use(
  (response) => response, 
  (error) => {
    if (error.response) {
      const status = error.response.status;

      if (status === 401) {
        alert("🔒 Session expired. Please log in again.");
        localStorage.clear();
        window.location.href = "/login"; // Redirect to login page
      } else if (status === 403) {
        alert("🚫 You are not authorized to perform this action.");
      } else {
        alert(`❌ Error: ${error.response.statusText} (${status})`);
      }
    } else {
      console.error("🛑 Network error or server unreachable:", error);
      alert("⚠️ Unable to connect to the server. Please try again later.");
    }
    
    return Promise.reject(error);
  }
);

export default axiosInstance;