import { signIn } from "next-auth/react";
import Link from "next/link";
import React, { useState } from "react";
import {
  Alert,
  Button,
  Col,
  Form,
  FormGroup,
  Label,
  Row,
  Container,
  Input,
  Card,
  CardTitle,
  CardBody,
} from "reactstrap";
import { createAccount } from "@/app/api/users/post";

const AuthForm = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isRegistering, setIsRegistering] = useState(false);

  const clearFields = () => {
    setEmail("");
    setPassword("");
    setError("");
  }

  const handleLogin = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    try {
      if (!email || !password)
        throw new Error("Email and password are required");

      const res = await signIn("credentials", {
        username: email,
        password: password,
        redirect: false,

        // If we want to redirect user to different page
        // redirect: true,
        // callbackUrl: "/profile"
      });

      if (res && res.error) {
        throw new Error(res.error ? res.error : "Failed to login");
      }

      console.log("Login successful");
    } catch (error) {
      console.error("Login failed:", error);
      setError("" + error);
    }
  };

  const handleRegister = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    try {
      if (!email || !password)
        throw new Error("Email and password are required");

      const res = await createAccount(email, password);

      if (res && res.error) {
        throw new Error(res.error ? res.error : "Failed to login");
      }

      console.log("Create Account successful");
    } catch (error) {
      console.error("Create Account failed:", error);
      setError("" + error);
    }
  };

  const loginForm = () => (
    <Row className="justify-content-center mb-6">
      <Col lg="5" className="rounded border p-4">
        <h3 className="text-center">Login</h3>
        {error && (
          <Alert fade={false} color="danger">
            {error}
          </Alert>
        )}
        <Form onSubmit={handleLogin}>
          <FormGroup>
            <Label for="email">Email</Label>
            <Input
              type="email"
              name="email"
              id="email"
              value={email}
              onChange={e => setEmail(e.target.value)}
            />
          </FormGroup>
          <FormGroup>
            <Label for="password">Password</Label>
            <Input
              type="password"
              name="password"
              id="password"
              value={password}
              onChange={e => setPassword(e.target.value)}
            />
          </FormGroup>
          <Button type="submit" color="primary" block>
            Login
          </Button>
        </Form>
      </Col>
    </Row>
  );

  const registerForm = () => (
    <Row className="justify-content-center mb-6">
      <Col lg="5" className="rounded border p-4">
        <h3 className="text-center">Create an account</h3>
        {error && (
          <Alert fade={false} color="danger">
            {error}
          </Alert>
        )}
        <Form onSubmit={handleRegister}>
          <FormGroup>
            <Label for="email">Email</Label>
            <Input
              type="email"
              name="email"
              id="email"
              value={email}
              onChange={e => setEmail(e.target.value)}
            />
          </FormGroup>
          <FormGroup>
            <Label for="password">Password</Label>
            <Input
              type="password"
              name="password"
              id="password"
              value={password}
              onChange={e => setPassword(e.target.value)}
            />
          </FormGroup>
          <Button type="submit" color="primary" block>
            Login
          </Button>
        </Form>
      </Col>
    </Row>
  );

  return (
    <Container className="mt-8">
      {isRegistering ? registerForm() : loginForm()}
      <Row className="justify-center">
        <Col md="6" className="text-center">
          {isRegistering ? (
            <span>
              Already have an account?{" "}
              <a
                className="underline cursor-pointer"
                onClick={() => {
                  setIsRegistering(false);
                  clearFields();
                }}
              >
                Login
              </a>
            </span>
          ) : (
            <span>
              Don't have an account?{" "}
              <a
                className="underline cursor-pointer"
                onClick={() => {
                  setIsRegistering(true);
                  clearFields();
                }}
              >
                Sign Up
              </a>
            </span>
          )}
        </Col>
      </Row>
    </Container>
    // <form onSubmit={handleLogin}>
    //   <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" />
    //   <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password" />
    //   <button type="submit">Login</button>
    // </form>
  );
};

export default AuthForm;
