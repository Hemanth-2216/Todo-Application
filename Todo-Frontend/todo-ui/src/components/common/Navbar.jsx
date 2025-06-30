import React, { useState } from "react";
import { Navbar, Nav, Container, Button, Modal } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { FaSignOutAlt } from "react-icons/fa";

const AppNavbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [showLogoutModal, setShowLogoutModal] = useState(false);

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <>
      {/* ✅ Improved Navbar */}
      <Navbar expand="lg" className="custom-navbar">
        <Container>
          <Navbar.Brand
            as={Link}
            to={user?.role === "ADMIN" ? "/admin/dashboard" : "/user/dashboard"}
            className="navbar-brand"
          >
            🚀 Task Management
          </Navbar.Brand>

          <Navbar.Toggle aria-controls="navbar-nav" />
          <Navbar.Collapse id="navbar-nav">
            <Nav className="ms-auto">
              {user?.role === "ADMIN" && (
                <>
                  <Nav.Link as={Link} to="/admin/users" className="nav-item">
                    Manage Users
                  </Nav.Link>
                  <Nav.Link as={Link} to="/admin/tasks" className="nav-item">
                    Manage Tasks
                  </Nav.Link>
                </>
              )}
              {user && (
                <>
                  <Nav.Link as={Link} to="/user/profile" className="nav-item">
                    Profile
                  </Nav.Link>
                  <Button variant="outline-light" className="logout-btn" onClick={() => setShowLogoutModal(true)}>
                    <FaSignOutAlt className="me-2" /> Logout
                  </Button>
                </>
              )}
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>

      {/* ✅ Logout Confirmation Modal */}
      <Modal show={showLogoutModal} onHide={() => setShowLogoutModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Confirm Logout</Modal.Title>
        </Modal.Header>
        <Modal.Body>Are you sure you want to logout?</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowLogoutModal(false)}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleLogout}>
            Yes, Logout
          </Button>
        </Modal.Footer>
      </Modal>

      {/* ✅ Custom Styles */}
      <style jsx>{`
        .custom-navbar {
          background: linear-gradient(45deg, #007bff, #6610f2);
          padding: 12px 20px;
          box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.15);
        }

        .navbar-brand {
          font-size: 1.6rem;
          font-weight: bold;
          color: white !important;
          transition: all 0.3s ease-in-out;
        }

        .navbar-brand:hover {
          transform: scale(1.05);
        }

        .nav-item {
          font-size: 1.1rem;
          font-weight: 500;
          color: white !important;
          margin-right: 20px;
          transition: all 0.3s ease;
        }

        .nav-item:hover {
          color: yellow !important;
        }

        .logout-btn {
          font-size: 1rem;
          font-weight: bold;
          padding: 8px 15px;
          border-radius: 50px;
          transition: all 0.3s ease-in-out;
        }

        .logout-btn:hover {
          background: white;
          color: black;
        }

        @media (max-width: 992px) {
          .navbar-brand {
            font-size: 1.4rem;
          }
        }
      `}</style>
    </>
  );
};

export default AppNavbar;