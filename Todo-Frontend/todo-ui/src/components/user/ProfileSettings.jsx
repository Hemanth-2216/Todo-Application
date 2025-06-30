import React, { useState, useEffect } from "react";
import { Container, Form, Button, Card, Alert, InputGroup } from "react-bootstrap";
import { useAuth } from "../../context/AuthContext";
import { userService } from "../../services/userService";
import { FaUser, FaUserTag, FaEnvelope, FaSave } from "react-icons/fa";

const ProfileSettings = () => {
  const { user, setUser } = useAuth(); 
  const [profile, setProfile] = useState({
    fullname: "",
    username: "",
    email: "",
  });
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  console.log("Current User:", user); 

  useEffect(() => {
    if (user && user.userId) { 
      const fetchProfile = async () => {
        try {
          const userData = await userService.getUserProfile(user.userId);
          console.log("Fetched Profile Data:", userData);

          setProfile({
            fullname: userData.fullname || "",
            username: userData.username || "",
            email: userData.email || "",
          });
        } catch (error) {
          console.error("Failed to fetch profile", error);
          setError("Could not fetch profile data. Please try again later.");
        }
      };
      fetchProfile();
    }
  }, [user]); 

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setError("");

    try {
      console.log("Updating Profile Data:", profile);
      const updatedProfile = await userService.updateUserProfile(user.userId, profile);
      
      setUser((prevUser) => ({
        ...prevUser,
        fullname: updatedProfile.fullname,
        username: updatedProfile.username,
        email: updatedProfile.email,
      }));

      setMessage("Profile updated successfully!");
    } catch (error) {
      console.error("Update profile error", error);
      setError("Failed to update profile. Please try again.");
    }
  };

  const handleChange = (e) => {
    setProfile({
      ...profile,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <Container className="mt-5 d-flex justify-content-center">
      <Card className="shadow-lg profile-card">
        <Card.Header className="text-center profile-header">⚙ Profile Settings</Card.Header>
        <Card.Body>
          {message && <Alert variant="success">{message}</Alert>}
          {error && <Alert variant="danger">{error}</Alert>}

          <Form onSubmit={handleSubmit}>
            {/* Full Name Field */}
            <Form.Group className="mb-3">
              <Form.Label>Full Name</Form.Label>
              <InputGroup>
                <InputGroup.Text><FaUser /></InputGroup.Text>
                <Form.Control
                  type="text"
                  name="fullname"
                  value={profile.fullname}
                  onChange={handleChange}
                  required
                  className="profile-input"
                />
              </InputGroup>
            </Form.Group>

            {/* Username Field */}
            <Form.Group className="mb-3">
              <Form.Label>Username</Form.Label>
              <InputGroup>
                <InputGroup.Text><FaUserTag /></InputGroup.Text>
                <Form.Control
                  type="text"
                  name="username"
                  value={profile.username}
                  onChange={handleChange}
                  required
                  className="profile-input"
                />
              </InputGroup>
            </Form.Group>

            {/* Email Field */}
            <Form.Group className="mb-3">
              <Form.Label>Email</Form.Label>
              <InputGroup>
                <InputGroup.Text><FaEnvelope /></InputGroup.Text>
                <Form.Control
                  type="email"
                  name="email"
                  value={profile.email}
                  onChange={handleChange}
                  required
                  className="profile-input"
                />
              </InputGroup>
            </Form.Group>

            {/* Submit Button */}
            <div className="text-center">
              <Button variant="primary" type="submit" className="profile-btn">
                <FaSave className="me-2" /> Update Profile
              </Button>
            </div>
          </Form>
        </Card.Body>
      </Card>

      {/* ✅ Custom Styles */}
      <style jsx>{`
        .profile-card {
          width: 100%;
          max-width: 500px;
          border-radius: 10px;
          overflow: hidden;
        }

        .profile-header {
          background: linear-gradient(45deg, #007bff, #6610f2);
          color: white;
          font-size: 20px;
          font-weight: bold;
          padding: 15px;
        }

        .profile-input {
          transition: all 0.3s ease-in-out;
        }

        .profile-input:focus {
          border-color: #007bff;
          box-shadow: 0 0 10px rgba(0, 123, 255, 0.5);
        }

        .profile-btn {
          background: linear-gradient(45deg, #007bff, #6610f2);
          border: none;
          font-size: 18px;
          padding: 10px 20px;
          border-radius: 5px;
          transition: all 0.3s ease;
        }

        .profile-btn:hover {
          background: linear-gradient(45deg, #6610f2, #007bff);
          transform: scale(1.05);
        }
      `}</style>
    </Container>
  );
};

export default ProfileSettings;