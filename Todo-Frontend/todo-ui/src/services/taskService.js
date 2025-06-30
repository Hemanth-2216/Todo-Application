
import axiosInstance from '../utils/axiosInterceptor';

export const taskService = {
  // ✅ Fetch all tasks (Admin Only)
  getAllTasks: async () => {
    try {
      const response = await axiosInstance.get('/admin/tasks');
      return response.data;
    } catch (error) {
      console.error("Error fetching all tasks:", error);
      throw new Error(`Failed to fetch tasks: ${error.response?.data?.message || error.message}`);
    }
  },

  // ✅ Fetch tasks assigned to a specific user (User Only)
  getUserTasks: async (userId) => {
    try {
      const response = await axiosInstance.get(`/user/tasks/${userId}`); // ✅ Corrected API path
      return response.data;
    } catch (error) {
      console.error("Error fetching user tasks:", error);
      throw new Error(`Failed to fetch user tasks: ${error.response?.data?.message || error.message}`);
    }
  },

  // ✅ Create or assign a new task (Admin Only)
  createOrAssignTask: async (taskData) => {
    try {
      const response = await axiosInstance.post('/admin/tasks/assign', taskData);
      return response.data;
    } catch (error) {
      console.error("Error creating or assigning task:", error);
      throw new Error(`Failed to create or assign task: ${error.response?.data?.message || error.message}`);
    }
  },

  // ✅ Update task status (User Only)
  updateTaskStatus: async (taskId, newStatus) => {
    try {
      const response = await axiosInstance.put(`/user/tasks/${taskId}/status`, { status: newStatus }); // ✅ Corrected API path
      return response.data;
    } catch (error) {
      console.error("Error updating task status:", error);
      throw new Error(`Failed to update task status: ${error.response?.data?.message || error.message}`);
    }
  },
  // ✅ Update an existing task (Admin Only)
  updateTask: async (taskId, updatedTask) => {
    try {
      const response = await axiosInstance.put(`/admin/tasks/${taskId}`, updatedTask);
      return response.data;
    } catch (error) {
      console.error("Error updating task:", error);
      throw new Error(`Failed to update task: ${error.response?.data?.message || error.message}`);
    }
  },
  
  // ✅ Delete a task (Admin Only)
  deleteTask: async (taskId) => {
    try {
      const response = await axiosInstance.delete(`/admin/tasks/${taskId}`);
      return response.data;
    } catch (error) {
      console.error("Error deleting task:", error);
      throw new Error(`Failed to delete task: ${error.response?.data?.message || error.message}`);
    }
  }
};