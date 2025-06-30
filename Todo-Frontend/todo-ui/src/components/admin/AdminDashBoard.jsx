import React, { useState, useEffect } from "react";
import { Container, Row, Col, Card, Table, Spinner, Alert } from "react-bootstrap";
import { FaUsers, FaTasks, FaUserCheck, FaUserCircle } from "react-icons/fa"; 
import { useAuth } from "../../context/AuthContext";
import { userService } from "../../services/userService";
import { taskService } from "../../services/taskService";

const AdminDashboard = () => {
  const { user } = useAuth();
  const [users, setUsers] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const userList = await userService.getAllUsers();
        const taskList = await taskService.getAllTasks();
        setUsers(userList || []);
        setTasks(taskList || []);
      } catch (error) {
        console.error("Failed to fetch data", error);
        setError("Failed to load data. Check console for details.");
      } finally {
        setLoading(false);
      }
    };

    if (user?.role === "ADMIN") {
      fetchData();
    } else {
      setError("Unauthorized access.");
      setLoading(false);
    }
  }, [user]);

  return (
    <Container className="mt-4">
      <h2 className="mb-4 text-center text-primary fw-bold d-flex align-items-center justify-content-center">
        <FaUsers className="me-2 text-danger" size={35} /> Admin Dashboard
      </h2>

      {loading && <div className="text-center"><Spinner animation="border" variant="primary" /></div>}
      {error && <Alert variant="danger" className="text-center">{error}</Alert>}

      {!loading && !error && (
        <>
          {/* ✅ Overview Cards with Icons & Bootstrap */}
          <Row className="mb-4">
            {[
              { label: "Total Users", value: users.length, variant: "primary", icon: <FaUsers size={35} className="text-primary" /> },
              { label: "Total Tasks", value: tasks.length, variant: "success", icon: <FaTasks size={35} className="text-success" /> },
              { label: "Active Users", value: users.filter((u) => u.enabled).length, variant: "info", icon: <FaUserCheck size={35} className="text-info" /> },
            ].map((item, index) => (
              <Col key={index} md={4} className="mb-3">
                <Card className="shadow-lg border-0 text-center" style={{ transition: "transform 0.3s ease-in-out" }} 
                  onMouseEnter={(e) => e.currentTarget.style.transform = "translateY(-5px)"} 
                  onMouseLeave={(e) => e.currentTarget.style.transform = "translateY(0)"}>
                  <Card.Body>
                    <div className="mb-2">{item.icon}</div>
                    <Card.Title className={`text-${item.variant} fw-bold`}>{item.label}</Card.Title>
                    <h2 className="fw-bold text-dark">{item.value}</h2>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>

          {/* ✅ Stylish Recent Users Table with Icons */}
          <Row>
            <Col md={12}>
              <Card className="shadow-lg border-0">
                <Card.Header className="text-white text-center fw-bold" style={{ background: "linear-gradient(45deg, #007bff, #6610f2)" }}>
                  <FaUsers className="me-2" /> Recent Users
                </Card.Header>
                <Card.Body>
                  <Table responsive striped bordered hover className="mb-0 text-center">
                    <thead style={{ background: "#f8f9fa" }}>
                      <tr>
                        <th className="fw-bold">Profile</th>
                        <th className="fw-bold">Username</th>
                        <th className="fw-bold">Email</th>
                        <th className="fw-bold">Role</th>
                      </tr>
                    </thead>
                    <tbody>
                      {users.slice(0, 5).map((u) => (
                        <tr key={u.id} style={{ cursor: "pointer" }} 
                          onMouseEnter={(e) => e.currentTarget.style.backgroundColor = "hsla(198, 74.60%, 35.50%, 0.59)"} 
                          onMouseLeave={(e) => e.currentTarget.style.backgroundColor = "transparent"}>
                          <td><FaUserCircle size={25} className="text-secondary" /></td>
                          <td>{u.username}</td>
                          <td>{u.email}</td>
                          <td className={`fw-bold ${u.roles?.[0]?.rolename === "ADMIN" ? "text-primary" : "text-success"}`}>
                            {u.roles?.[0]?.rolename || "USER"}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </Table>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </>
      )}
    </Container>
  );
};

export default AdminDashboard;