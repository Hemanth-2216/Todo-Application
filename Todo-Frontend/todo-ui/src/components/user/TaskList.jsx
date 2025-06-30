import React, { useState, useEffect } from "react";
import { Container, ListGroup, Badge } from "react-bootstrap";
import { useAuth } from "../../context/AuthContext";
import { taskService } from "../../services/taskService";
import { FaTasks } from "react-icons/fa";

const TaskList = () => {
  const [tasks, setTasks] = useState([]);
  const { user } = useAuth();

  useEffect(() => {
    if (user?.id) {
      const fetchTasks = async () => {
        try {
          const userTasks = await taskService.getUserTasks(user.id);
          setTasks(userTasks);
        } catch (error) {
          console.error("Failed to fetch tasks", error);
        }
      };
      fetchTasks();
    }
  }, [user?.id]);

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
      {/* ✅ Stylish Heading */}
      <h2 className="mb-4 text-center task-heading">
        <FaTasks className="me-2" /> Your Tasks
      </h2>

      {tasks.length === 0 ? (
        <p className="text-center text-muted">No tasks available.</p>
      ) : (
        <ListGroup className="shadow-lg rounded task-list">
          {tasks.map((task) => (
            <ListGroup.Item
              key={task.id}
              className="d-flex justify-content-between align-items-center task-item"
            >
              <span className="task-text">{task.task}</span>
              <Badge bg={getStatusVariant(task.status)} pill>
                {task.status.replace("_", " ")}
              </Badge>
            </ListGroup.Item>
          ))}
        </ListGroup>
      )}

      {/* ✅ Custom Styles */}
      <style jsx>{`
        .task-heading {
          font-weight: bold;
          text-decoration: underline;
        }

        .task-list {
          border-radius: 10px;
          overflow: hidden;
        }

        .task-item {
          transition: all 0.3s ease-in-out;
          font-size: 18px;
        }

        .task-item:hover {
          background-color: rgba(0, 123, 255, 0.1);
          transform: scale(1.02);
        }

        .task-text {
          font-weight: 500;
        }
      `}</style>
    </Container>
  );
};

export default TaskList;