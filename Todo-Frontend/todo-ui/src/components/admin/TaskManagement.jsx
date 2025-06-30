import React, { useState, useEffect } from "react";
import { Container, Table, Button, Modal, Form, Alert } from "react-bootstrap";
import { FaTasks, FaTrashAlt, FaEdit, FaPlusCircle } from "react-icons/fa";
import { taskService } from "../../services/taskService";
import { userService } from "../../services/userService";

const TaskManagement = () => {
  const [tasks, setTasks] = useState([]);
  const [users, setUsers] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editModal, setEditModal] = useState(false);
  const [newTask, setNewTask] = useState({ task: "", assignedTo: "" });
  const [editTask, setEditTask] = useState(null);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState("");

  useEffect(() => {
    fetchTasks();
    fetchUsers();
  }, []);

  const fetchTasks = async () => {
    try {
      const taskList = await taskService.getAllTasks();
      setTasks(taskList);
    } catch (error) {
      setError("Failed to fetch tasks. Please try again.");
    }
  };

  const fetchUsers = async () => {
    try {
      const userList = await userService.getAllUsers();
      setUsers(userList);
    } catch (error) {
      setError("Failed to fetch users. Please try again.");
    }
  };

  const handleDeleteTask = async (taskId) => {
    if (window.confirm("Are you sure you want to delete this task?")) {
      try {
        await taskService.deleteTask(taskId);
        setSuccessMessage("Task deleted successfully!");
        fetchTasks();
        setTimeout(() => setSuccessMessage(""), 3000);
      } catch (error) {
        setError("Failed to delete task. Please try again.");
      }
    }
  };

  const handleCreateTask = async (e) => {
    e.preventDefault();
    if (!newTask.assignedTo) {
      setError("Please select a user.");
      return;
    }

    const taskData = {
      task: newTask.task,
      userId: newTask.assignedTo,
      status: "PENDING",
    };

    try {
      await taskService.createOrAssignTask(taskData);
      setSuccessMessage("Task assigned successfully!");
      setShowModal(false);
      fetchTasks();
      setTimeout(() => setSuccessMessage(""), 3000);
    } catch (error) {
      setError(error.response?.data?.message || "Failed to create task. Please try again.");
    }
  };

  const handleEditClick = (task) => {
    setEditTask({
      id: task.id,
      task: task.task,
      assignedTo: task.user?.id || "",
      status: task.status || "PENDING",
    });
    setEditModal(true);
  };

  const handleEditTask = async (e) => {
    e.preventDefault();

    if (!editTask.assignedTo) {
      setError("Please select a user.");
      return;
    }

    try {
      const updatedTask = {
        task: editTask.task,
        userId: editTask.assignedTo,
        status: editTask.status,
      };

      await taskService.updateTask(editTask.id, updatedTask);
      setSuccessMessage("Task updated successfully!");
      setEditModal(false);
      fetchTasks();
      setTimeout(() => setSuccessMessage(""), 3000);
    } catch (error) {
      setError("Failed to update task. Please try again.");
    }
  };

  return (
    <Container className="mt-5">
      <h2 className="mb-4 text-center text-success fw-bold d-flex align-items-center justify-content-center">
        <FaTasks className="me-2 text-warning" size={35} /> Task Management
      </h2>

      {successMessage && <Alert variant="success" className="text-center">{successMessage}</Alert>}
      {error && <Alert variant="danger" className="text-center">{error}</Alert>}

      <div className="text-end mb-3">
        <Button 
          variant="primary" 
          className="fw-bold d-flex align-items-center gap-2"
          onClick={() => setShowModal(true)}
        >
          <FaPlusCircle /> Create Task
        </Button>
      </div>

      <Table striped bordered hover responsive className="shadow-sm text-center">
        <thead className="bg-light">
          <tr>
            <th className="fw-bold">ID</th>
            <th className="fw-bold">Task</th>
            <th className="fw-bold">Assigned To</th> 
            <th className="fw-bold">Status</th>
            <th className="fw-bold">Actions</th>
          </tr>
        </thead>
        <tbody>
          {tasks.map((task) => (
            <tr key={task.id}>
              <td>{task.id}</td>
              <td>{task.task}</td>
              <td className="fw-bold">{task.user?.username || "Unassigned"}</td> 
              <td>
                <span className={`badge ${task.status === "PENDING" ? "bg-warning text-dark" : "bg-success"}`}>
                  {task.status}
                </span>
              </td>
              <td className="d-flex justify-content-center gap-2">
                <Button 
                  variant="warning" 
                  size="sm"
                  className="d-flex align-items-center gap-2"
                  onClick={() => handleEditClick(task)}
                >
                  <FaEdit /> Edit
                </Button>
                <Button 
                  variant="danger" 
                  size="sm" 
                  className="d-flex align-items-center gap-2"
                  onClick={() => handleDeleteTask(task.id)}
                >
                  <FaTrashAlt /> Delete
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {/* Task Creation Modal */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Create Task</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleCreateTask}>
            <Form.Group className="mb-3">
              <Form.Label>Task</Form.Label>
              <Form.Control
                type="text"
                required
                value={newTask.task}
                onChange={(e) => setNewTask({ ...newTask, task: e.target.value })}
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Assign To</Form.Label>
              <Form.Control as="select" required value={newTask.assignedTo} onChange={(e) => setNewTask({ ...newTask, assignedTo: e.target.value })}>
                <option value="">Select User</option>
                {users.map((user) => (
                  <option key={user.id} value={user.id}>{user.username}</option>
                ))}
              </Form.Control>
            </Form.Group>
            <Button variant="primary" type="submit">Create Task</Button>
          </Form>
        </Modal.Body>
      </Modal>

      {/* Edit Task Modal */}
      <Modal show={editModal} onHide={() => setEditModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Edit Task</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleEditTask}> 
            <Form.Group className="mb-3">
              <Form.Label>Task</Form.Label>
              <Form.Control
                type="text"
                required
                value={editTask?.task || ""}
                onChange={(e) => setEditTask({ ...editTask, task: e.target.value })}
              />
            </Form.Group>
            <Button variant="primary" type="submit">Update Task</Button>
          </Form>
        </Modal.Body>
      </Modal>
    </Container>
  );
};

export default TaskManagement;