import React, { useState } from "react";
import { Link } from "react-router-dom";
import { Offcanvas, Button, Nav } from "react-bootstrap";
import { useAuth } from "../../context/AuthContext";
import { FaBars, FaTachometerAlt, FaUsers, FaTasks, FaUserCog, FaHome, FaTimes } from "react-icons/fa";

const Sidebar = () => {
  const { user } = useAuth();
  const [show, setShow] = useState(false);

  const toggleSidebar = () => setShow(!show);

  return (
    <>
      {/* ✅ Sidebar Toggle Button */}
      <Button variant="primary" className="sidebar-toggle-btn" onClick={toggleSidebar}>
        <FaBars />
      </Button>

      {/* ✅ Sidebar Offcanvas */}
      <Offcanvas show={show} onHide={toggleSidebar} backdrop="true" placement="start" className="custom-sidebar">
        <Offcanvas.Header className="sidebar-header">
          <Offcanvas.Title className="sidebar-title">📌 Navigation</Offcanvas.Title>
          <button className="close-btn" onClick={toggleSidebar}>
            <FaTimes />
          </button>
        </Offcanvas.Header>
        <Offcanvas.Body>
          <Nav className="flex-column">
            {/* ✅ Admin Links */}
            {user?.role === "ADMIN" ? (
              <>
                <Nav.Link as={Link} to="/admin/dashboard" onClick={toggleSidebar} className="sidebar-item">
                  <FaTachometerAlt className="me-2" /> Admin Dashboard
                </Nav.Link>
                <Nav.Link as={Link} to="/admin/users" onClick={toggleSidebar} className="sidebar-item">
                  <FaUsers className="me-2" /> Manage Users
                </Nav.Link>
                <Nav.Link as={Link} to="/admin/tasks" onClick={toggleSidebar} className="sidebar-item">
                  <FaTasks className="me-2" /> Manage Tasks
                </Nav.Link>
              </>
            ) : (
              <>
                <Nav.Link as={Link} to="/user/dashboard" onClick={toggleSidebar} className="sidebar-item">
                  <FaHome className="me-2" /> My Tasks
                </Nav.Link>
                <Nav.Link as={Link} to="/user/profile" onClick={toggleSidebar} className="sidebar-item">
                  <FaUserCog className="me-2" /> Profile Settings
                </Nav.Link>
              </>
            )}
          </Nav>
        </Offcanvas.Body>
      </Offcanvas>

      {/* ✅ Sidebar Custom Styles */}
      <style jsx>{`
        .sidebar-toggle-btn {
          position: fixed;
          top: 15px;
          left: 15px;
          width: 50px;
          height: 50px;
          border-radius: 50%;
          font-size: 22px;
          padding: 0;
          background: linear-gradient(45deg, #007bff, #6610f2);
          border: none;
          color: white;
          transition: 0.3s ease-in-out;
          box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
        }

        .sidebar-toggle-btn:hover {
          background: linear-gradient(45deg, #6610f2, #007bff);
          transform: scale(1.1);
        }

        .custom-sidebar {
          width: 270px;
          background: #222; /* Light black */
          color: #ddd;
          box-shadow: 3px 0px 8px rgba(0, 0, 0, 0.2);
          padding-right: 15px;
        }

        .sidebar-header {
          display: flex;
          align-items: center;
          justify-content: space-between;
          padding-right: 20px;
        }

        .sidebar-title {
          font-weight: bold;
          font-size: 20px;
          color: #fff;
        }

        .close-btn {
          font-size: 24px;
          font-weight: bold;
          width: 40px;
          height: 40px;
          border-radius: 50%;
          background: #dc3545;
          color: white;
          border: none;
          display: flex;
          align-items: center;
          justify-content: center;
          cursor: pointer;
          transition: background 0.3s ease, transform 0.2s ease;
          margin-left: auto;
        }

        .close-btn:hover {
          background: #ff4c5b;
          transform: scale(1.1);
        }

        .sidebar-item {
          font-size: 18px;
          padding: 12px 15px;
          font-weight: 500;
          color: #ddd;
          transition: all 0.3s ease;
          display: flex;
          align-items: center;
        }

        .sidebar-item:hover {
          background: #007bff;
          color: #fff;
          border-radius: 5px;
        }

        @media (max-width: 992px) {
          .sidebar-toggle-btn {
            top: 10px;
            left: 10px;
            width: 45px;
            height: 45px;
            font-size: 20px;
          }
        }
      `}</style>
    </>
  );
};

export default Sidebar;