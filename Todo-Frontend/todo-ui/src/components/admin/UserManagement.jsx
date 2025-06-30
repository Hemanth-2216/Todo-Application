import React, { useState, useEffect } from "react";
import { Container, Table, Button, Modal, Form, Card } from "react-bootstrap";
import { FaUserPlus, FaTrash, FaUsers } from "react-icons/fa";
import { userService } from "../../services/userService";
import { authService } from "../../services/authService";

const UserManagement = () => {
  const [users, setUsers] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [newAdmin, setNewAdmin] = useState({ username: "", email: "", fullname: "", password: "" });
  const [deletedUsers, setDeletedUsers] = useState(new Set()); // Track deleted users in state

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const userList = await userService.getAllUsers();
      setUsers(userList);
    } catch (error) {
      console.error("❌ Failed to fetch users:", error);
    }
  };

  const handleDeleteUser = async (userId) => {
    if (!window.confirm("⚠️ Are you sure you want to delete this user?")) return;

    try {
      await userService.deleteUser(userId);
      alert("✅ User deleted successfully!");
      setDeletedUsers((prev) => new Set([...prev, userId])); // Soft delete from UI
    } catch (error) {
      console.error("❌ Failed to delete user:", error);

      if (error.message.includes("foreign key constraint fails")) {
        alert("⚠️ Cannot delete user because they have assigned tasks. Hiding them instead.");
        setDeletedUsers((prev) => new Set([...prev, userId])); // Hide user from UI
      } else {
        alert("❌ Unable to delete user. Please try again.");
      }
    }
  };

  const handleCreateAdmin = async (e) => {
    e.preventDefault();
    if (!newAdmin.username || !newAdmin.email || !newAdmin.fullname || !newAdmin.password) {
      alert("⚠️ Please fill in all fields.");
      return;
    }

    const loggedInUser = JSON.parse(localStorage.getItem("user"));
    try {
      await authService.registerAdmin(newAdmin, loggedInUser.userId);
      alert("✅ Admin created successfully!");
      setShowModal(false);
      fetchUsers();
    } catch (error) {
      console.error("❌ Failed to create admin:", error);
    }
  };

  return (
    <Container className="mt-5">
      <h2 className="mb-4 text-center text-primary fw-bold d-flex align-items-center justify-content-center">
        <FaUsers className="me-2 text-danger" size={35} /> User Management
      </h2>

      <Button variant="primary" className="mb-3 rounded-pill px-4 py-2 d-flex align-items-center" onClick={() => setShowModal(true)}>
        <FaUserPlus className="me-2" /> Create Admin
      </Button>

      <Card className="shadow-lg border-0">
        <Card.Body>
          <Table responsive striped bordered hover className="mb-0 text-center">
            <thead className="bg-dark text-white">
              <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Email</th>
                <th>Role</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {users
                .filter((user) => !deletedUsers.has(user.id)) // Hide deleted users
                .map((user) => (
                  <tr key={user.id}>
                    <td>{user.id}</td>
                    <td>{user.username}</td>
                    <td>{user.email}</td>
                    <td>
                      <span className={`badge ${user.roles[0]?.rolename === "ADMIN" ? "bg-primary" : "bg-success"}`}>
                        {user.roles[0]?.rolename || "USER"}
                      </span>
                    </td>
                    <td>
                      <Button variant="danger" size="sm" onClick={() => handleDeleteUser(user.id)}>
                        <FaTrash className="me-1" /> Delete
                      </Button>
                    </td>
                  </tr>
                ))}
            </tbody>
          </Table>
        </Card.Body>
      </Card>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton className="text-white" style={{ background: "linear-gradient(45deg, #007bff, #6610f2)" }}>
          <Modal.Title>Create Admin</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleCreateAdmin}>
            <Form.Group className="mb-3">
              <Form.Label>Full Name</Form.Label>
              <Form.Control type="text" required value={newAdmin.fullname} onChange={(e) => setNewAdmin({ ...newAdmin, fullname: e.target.value })} />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Username</Form.Label>
              <Form.Control type="text" required value={newAdmin.username} onChange={(e) => setNewAdmin({ ...newAdmin, username: e.target.value })} />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Email</Form.Label>
              <Form.Control type="email" required value={newAdmin.email} onChange={(e) => setNewAdmin({ ...newAdmin, email: e.target.value })} />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Password</Form.Label>
              <Form.Control type="password" required value={newAdmin.password} onChange={(e) => setNewAdmin({ ...newAdmin, password: e.target.value })} />
            </Form.Group>
            <Button variant="primary" type="submit" className="w-100 fw-bold">
              Create Admin
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    </Container>
  );
};

export default UserManagement;