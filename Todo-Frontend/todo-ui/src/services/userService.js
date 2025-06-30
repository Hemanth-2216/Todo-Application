import axiosInstance from '../utils/axiosInterceptor';

export const userService = {
  // ✅ Fetch all users (Admin only)
  getAllUsers: async () => {
    try {
      const response = await axiosInstance.get('/admin/users');
      return response.data;
    } catch (error) {
      console.error("Error fetching users:", error.response?.data || error.message);
      throw new Error(error.response?.data?.message || "Failed to fetch users.");
    }
  },

  // ✅ Fetch all tasks (Admin only)
  getAllTasks: async () => {
    try {
      const response = await axiosInstance.get('/admin/tasks');
      return response.data;
    } catch (error) {
      console.error("Error fetching tasks:", error.response?.data || error.message);
      throw new Error(error.response?.data?.message || "Failed to fetch tasks.");
    }
  },

  // ✅ Delete a user (Admin only)
  deleteUser: async (userId) => {
    try {
      const response = await axiosInstance.delete(`/admin/users/${userId}`);
      return response.data;
    } catch (error) {
      console.error("Error deleting user:", error.response?.data || error.message);
      throw new Error(error.response?.data?.message || "Failed to delete user.");
    }
  },

    // ✅ Fetch user profile (Updated URL structure)
    getUserProfile: async (userId) => {
      try {
        const response = await axiosInstance.get(`/user/profile/${userId}`);
        return response.data;
      } catch (error) {
        console.error("Error fetching user profile:", error.response?.data || error.message);
        throw new Error(error.response?.data?.message || "Failed to fetch user profile.");
      }
    },
  
    // ✅ Update user profile (Fixed API request)
    updateUserProfile: async (userId, userData) => {
      try {
        const response = await axiosInstance.put(`/user/profile/${userId}`, userData);
        return response.data;
      } catch (error) {
        console.error("Error updating user profile:", error.response?.data || error.message);
        throw new Error(error.response?.data?.message || "Failed to update profile.");
      }
    }
  };