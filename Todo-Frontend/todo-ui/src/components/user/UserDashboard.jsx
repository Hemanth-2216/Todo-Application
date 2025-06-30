import React, { useState, useEffect } from "react";
import { Container, Row, Col, Card, Button, Modal, Form } from "react-bootstrap";
import { useAuth } from "../../context/AuthContext";
import { taskService } from "../../services/taskService";
import { FaTasks, FaClipboardList, FaHourglassHalf, FaCheckCircle } from "react-icons/fa";

const UserDashboard = () => {
  const [tasks, setTasks] = useState([]);
  const [filteredTasks, setFilteredTasks] = useState([]);
  const { user } = useAuth();
  const [showModal, setShowModal] = useState(false);
  const [selectedTask, setSelectedTask] = useState(null);
  const [searchQuery, setSearchQuery] = useState("");
  const [filterStatus, setFilterStatus] = useState("");

  useEffect(() => {
    if (user?.userId) {
      const fetchTasks = async () => {
        try {
          const userTasks = await taskService.getUserTasks(user.userId);
          setTasks(userTasks);
          setFilteredTasks(userTasks);
        } catch (error) {
          console.error("Failed to fetch tasks", error);
        }
      };
      fetchTasks();
    }
  }, [user?.userId]);

  const handleUpdateStatus = async () => {
    if (!selectedTask) return;

    try {
      const newStatus =
        selectedTask.status === "PENDING"
          ? "ON_PROGRESS"
          : selectedTask.status === "ON_PROGRESS"
          ? "COMPLETED"
          : "PENDING";

      await taskService.updateTaskStatus(selectedTask.id, newStatus);
      setTasks((prevTasks) =>
        prevTasks.map((task) =>
          task.id === selectedTask.id ? { ...task, status: newStatus } : task
        )
      );
      setShowModal(false);
    } catch (error) {
      console.error("Failed to update task status", error);
    }
  };

  useEffect(() => {
    let updatedTasks = tasks;

    if (searchQuery) {
      updatedTasks = updatedTasks.filter((task) =>
        task.task.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }

    if (filterStatus) {
      updatedTasks = updatedTasks.filter((task) => task.status === filterStatus);
    }

    setFilteredTasks(updatedTasks);
  }, [searchQuery, filterStatus, tasks]);

  const getStatusVariant = (status) => {
    switch (status) {
      case "PENDING":
        return "warning";
      case "ON_PROGRESS":
        return "primary";
      case "COMPLETED":
        return "success";
      default:
        return "secondary";
    }
  };

  return (
    <Container className="mt-5">
      {/* ✅ Stylish Welcome Heading */}
      <h2 className="mb-4 text-center fw-bold">
        <FaTasks className="me-2" /> Welcome, {user?.username}!
      </h2>

      {/* ✅ Task Summary Cards with Icons */}
      <Row className="mb-4">
        {[
          { label: "Total Tasks", value: tasks.length, icon: <FaClipboardList /> },
          { label: "Pending", value: tasks.filter((t) => t.status === "PENDING").length, icon: <FaHourglassHalf /> },
          { label: "In Progress", value: tasks.filter((t) => t.status === "ON_PROGRESS").length, icon: <FaTasks /> },
          { label: "Completed", value: tasks.filter((t) => t.status === "COMPLETED").length, icon: <FaCheckCircle /> }
        ].map(({ label, value, icon }, index) => (
          <Col key={index} md={3}>
            <Card className="shadow-lg border task-summary">
              <Card.Body className="text-center">
                <Card.Title className="d-flex align-items-center justify-content-center gap-2">
                  {icon} {label}
                </Card.Title>
                <h3>{value}</h3>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      {/* ✅ Search & Filter Controls */}
      <Row className="mb-3">
        <Col md={6}>
          <Form.Control
            type="text"
            placeholder="Search tasks..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </Col>
        <Col md={4}>
          <Form.Select value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)}>
            <option value="">All Statuses</option>
            <option value="PENDING">Pending</option>
            <option value="ON_PROGRESS">In Progress</option>
            <option value="COMPLETED">Completed</option>
          </Form.Select>
        </Col>
      </Row>

      {/* ✅ Task List UI Improvement */}
      <Row>
        <Col md={12}>
          <Card className="shadow">
            <Card.Header className="bg-primary text-white">Your Tasks</Card.Header>
            <Card.Body>
              {filteredTasks.length === 0 ? (
                <p className="text-center text-muted">No tasks found.</p>
              ) : (
                filteredTasks.map((task) => (
                  <div
                    key={task.id}
                    className="d-flex justify-content-between align-items-center mb-3 p-2 border rounded task-item"
                  >
                    <span className="fw-bold">{task.task}</span>
                    <Button
                      variant={getStatusVariant(task.status)}
                      size="sm"
                      onClick={() => {
                        setSelectedTask(task);
                        setShowModal(true);
                      }}
                    >
                      {task.status.replace("_", " ")}
                    </Button>
                  </div>
                ))
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* ✅ Task Status Update Confirmation Modal */}
      <Modal show={showModal} onHide={() => setShowModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Update Task Status</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>
            Are you sure you want to update the status of "<b>{selectedTask?.task}</b>"?
          </p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleUpdateStatus}>
            Yes, Update
          </Button>
        </Modal.Footer>
      </Modal>

      {/* ✅ Custom Styles */}
      <style jsx>{`
        .task-summary {
          transition: all 0.3s ease-in-out;
        }

        .task-summary:hover {
          transform: scale(1.05);
        }

        .task-item {
          transition: all 0.3s ease-in-out;
          background-color: #f8f9fa;
        }

        .task-item:hover {
          background-color: rgba(0, 123, 255, 0.1);
          transform: scale(1.02);
        }
      `}</style>
    </Container>
  );
};

export default UserDashboard;