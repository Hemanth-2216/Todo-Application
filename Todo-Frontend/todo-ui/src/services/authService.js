
import axiosInstance from '../utils/axiosInterceptor';

export const authService = {
  // ✅ Login User
  login: async (username, password) => {
    try {
      const response = await axiosInstance.post('/auth/login', { username, password });

      if (response.data) {
        // Extract user data from the response
        const user = {
          accessToken: response.data.accessToken,
          refreshToken: response.data.refreshToken,
          userId: response.data.userId,
          username: response.data.username,
          role: response.data.role,
        };

        // Store the full user object in localStorage
        localStorage.setItem('user', JSON.stringify(user));

        return response.data; // Return the data for further usage (e.g., to navigate)
      }

      return null;
    } catch (error) {
      console.error("Login failed:", error);
      throw new Error("Failed to login. Please check your credentials.");
    }
  },


  // ✅ Register User
  register: async (userData) => {
    try {
      const response = await axiosInstance.post('/auth/register', userData);
      return response.data;
    } catch (error) {
      console.error("Registration failed:", error); // Log error
      throw new Error("Failed to register. Please try again.");
    }
  },

  // ✅ Register a new admin
  // authService.js
registerAdmin: async (adminData, adminId) => {
  try {
    const response = await axiosInstance.post(`/auth/register/admin/${adminId}`, adminData);
    return response.data;
  } catch (error) {
    console.error("❌ Failed to register admin:", error);
    throw new Error("Failed to register admin. Please try again.");
  }
}
};