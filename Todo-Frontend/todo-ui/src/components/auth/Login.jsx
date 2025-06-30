import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { authService } from '../../services/authService';
import { Container, Form, Button, Alert, Card, Row, Col } from 'react-bootstrap';
import { FaUser, FaLock } from 'react-icons/fa';

const Login = () => {
  const navigate = useNavigate();
  const { setUser, setIsAuthenticated } = useAuth();
  const [formData, setFormData] = useState({ username: '', password: '' });
  const [error, setError] = useState(null);
  const [validationErrors, setValidationErrors] = useState({});

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const validateForm = () => {
    const errors = {};

    if (!formData.username.trim()) {
      errors.username = 'Username cannot be empty';
    } else if (formData.username.length < 4 || formData.username.length > 20) {
      errors.username = 'Username must be between 4 and 20 characters';
    }

    if (!formData.password.trim()) {
      errors.password = 'Password cannot be empty';
    } else if (formData.password.length < 4) {
      errors.password = 'Password must be at least 4 characters long';
    }

    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    if (!validateForm()) return; // Prevent API call if validation fails

    try {
      const response = await authService.login(formData.username, formData.password);

      localStorage.setItem('accessToken', response.accessToken);
      localStorage.setItem('refreshToken', response.refreshToken);

      const user = {
        userId: response.userId,
        username: response.username,
        role: response.role
      };
      localStorage.setItem('user', JSON.stringify(user));

      setUser(user);
      setIsAuthenticated(true);

      if (response.role === 'ADMIN') {
        navigate('/admin/dashboard');
      } else {
        navigate('/user/dashboard');
      }
    } catch (error) {
      setError('Invalid credentials. Please try again.');
      console.error('Login error:', error);
    }
  };

  return (
    <div 
      style={{
        height: '100vh',
        width: '100%',
        background: 'linear-gradient(to right, #e3f2fd, #ffffff)',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        position: 'relative'
      }}
    >
      <div 
        style={{
          position: 'absolute',
          top: 0,
          left: 0,
          width: '100%',
          height: '100%',
          background: 'url("https://source.unsplash.com/1600x900/?productivity,tasks") center/cover',
          opacity: 0.2,
          zIndex: -1
        }}
      />

      <Container>
        <Row className="w-100">
          <Col xs={12} md={6} lg={4} className="mx-auto">
            <Card className="shadow-lg rounded-3 border-0">
              <Card.Header className="text-center bg-primary text-white fw-bold" style={{ fontSize: '1.4rem' }}>
                Welcome Back!
              </Card.Header>
              <Card.Body className="p-4">
                <p className="text-center text-muted fw-bold" style={{ fontSize: '1.1rem', fontStyle: 'italic' }}>
                  "Stay organized, stay productive!" ✨
                </p>

                {error && <Alert variant="danger" className="text-center">{error}</Alert>}

                <Form onSubmit={handleSubmit}>
                  <Form.Group className="mb-3">
                    <Form.Label>Username</Form.Label>
                    <div className="input-group">
                      <span className="input-group-text"><FaUser /></span>
                      <Form.Control
                        type="text"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        placeholder="Enter your username"
                        className="rounded-2"
                        isInvalid={!!validationErrors.username}
                      />
                      <Form.Control.Feedback type="invalid">
                        {validationErrors.username}
                      </Form.Control.Feedback>
                    </div>
                  </Form.Group>

                  <Form.Group className="mb-3">
                    <Form.Label>Password</Form.Label>
                    <div className="input-group">
                      <span className="input-group-text"><FaLock /></span>
                      <Form.Control
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        placeholder="Enter your password"
                        className="rounded-2"
                        isInvalid={!!validationErrors.password}
                      />
                      <Form.Control.Feedback type="invalid">
                        {validationErrors.password}
                      </Form.Control.Feedback>
                    </div>
                  </Form.Group>

                  <Button variant="primary" type="submit" className="w-100 rounded-2 fw-bold" style={{ fontSize: '1.1rem' }}>
                    Login
                  </Button>
                </Form>

                <div className="mt-3 text-center">
                  <p className="mb-1 text-muted">Don't have an account?</p>
                  <Button variant="link" onClick={() => navigate('/register')} className="fw-bold">
                    Register Here
                  </Button>
                </div>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Login;
