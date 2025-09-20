"use client";

import { useState } from "react";
import { useRouter } from "next/navigation"; // For redirecting
import Link from "next/link";
import { motion } from "framer-motion";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { z } from "zod";

// Define Zod schema for form validation
const officerSchema = z.object({
  fullName: z.string().min(1, { message: "Full name is required" }),
  age: z.number().min(18, { message: "Must be at least 18 years old" }).max(100, { message: "Invalid age" }),
  email: z.string().email({ message: "Enter a valid email address" }),
  username: z.string().min(5, { message: "Username must be at least 5 characters" }),
  password: z.string().min(8, { message: "Password must be at least 8 characters" }),
  confirmPassword: z.string(),
  employeeId: z.string().regex(/^[A-Z0-9]{6,12}$/, { message: "Enter a valid employee ID (6-12 alphanumeric characters)" }),
  departmentName: z.string().min(1, { message: "Department name is required" }),
}).refine(data => data.password === data.confirmPassword, {
  message: "Passwords do not match",
  path: ["confirmPassword"],
});

export default function GovernmentOfficerSignUp() {
  const router = useRouter(); 
  const [formData, setFormData] = useState({
    fullName: "",
    age: "",
    email: "",
    username: "",
    password: "",
    confirmPassword: "",
    employeeId: "",
    departmentName: "",
  });
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "age" && value !== "" && !/^\d*$/.test(value)) return; 
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: "" }));
    }
  };

  const validateForm = () => {
    try {
      const submissionData = {
        ...formData,
        age: formData.age === "" ? NaN : parseInt(formData.age), // Handle empty age
      };
      officerSchema.parse(submissionData);
      setErrors({});
      return true;
    } catch (error) {
      if (error instanceof z.ZodError) {
        const newErrors = {};
        error.errors.forEach((err) => {
          newErrors[err.path[0]] = err.message;
        });
        setErrors(newErrors);
      }
      return false;
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) return;

    setIsSubmitting(true);
    const toastId = console.loading("Submitting registration request...");

    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 1500));

      // Uncomment and adjust this for real API
      // const response = await fetch('/api/government/register', {
      //   method: 'POST',
      //   headers: { 'Content-Type': 'application/json' },
      //   body: JSON.stringify(formData),
      // });
      // if (!response.ok) throw new Error('Registration failed');

      console.log("Registration request sent successfully!", { id: toastId });
      // Redirect to a confirmation page or login
      setTimeout(() => router.push("/sign-in/government"), 1000);
    } catch (error) {
      console.error("Error submitting form:", error);
      console.log("Failed to submit request. Please try again.", { id: toastId });
    } finally {
      setIsSubmitting(false);
    }
  };

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: { opacity: 1, transition: { staggerChildren: 0.1 } },
  };

  const itemVariants = {
    hidden: { y: 20, opacity: 0 },
    visible: { y: 0, opacity: 1, transition: { type: "spring", stiffness: 100, damping: 10 } },
  };

  const MotionButton = motion.create(Button);

  return (
    <div className="flex flex-col min-h-screen bg-gradient-to-b from-gray-900 via-gray-800 to-gray-700">
      {/* Decorative background elements */}
      <div className="fixed inset-0 overflow-hidden -z-10">
        <div className="absolute -top-16 -left-16 w-64 h-64 rounded-full bg-blue-500/10 blur-3xl"></div>
        <div className="absolute top-1/4 -right-20 w-80 h-80 rounded-full bg-indigo-500/10 blur-3xl"></div>
        <div className="absolute bottom-0 left-1/3 w-96 h-96 rounded-full bg-emerald-500/10 blur-3xl"></div>
      </div>

      {/* Header */}
      <header className="bg-gray-900/80 backdrop-blur-sm border-b border-gray-800">
        <div className="container mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16 sm:h-20">
            <div className="flex items-center">
              <div className="bg-gradient-to-r from-blue-500 to-indigo-700 rounded-full p-2 mr-3">
                <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 11c0 3.517-1.009 6.799-2.753 9.571m-3.44-2.04l.054-.09A13.916 13.916 0 008 11a4 4 0 118 0c0 1.017-.07 2.019-.203 3m-2.118 6.844A21.88 21.88 0 0015.171 17m3.839 1.132c.645-2.266.99-4.659.99-7.132A8 8 0 008 4.07M3 15.364c.64-1.319 1-2.8 1-4.364 0-1.457.39-2.823 1.07-4" />
                </svg>
              </div>
              <span className="text-xl font-bold text-white">SecurePortal</span>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex-grow py-12 px-4 sm:px-6 lg:px-8">
        <motion.div className="w-full max-w-3xl mx-auto" initial="hidden" animate="visible" variants={containerVariants}>
          <motion.div variants={itemVariants} className="text-center mb-8">
            <h1 className="text-3xl sm:text-4xl font-bold text-zinc-300 mb-2">Government Officer Registration</h1>
            <p className="text-gray-300">Create your government officer account</p>
          </motion.div>

          <motion.div variants={itemVariants}>
            <Card className="border border-gray-700 rounded-xl bg-gray-800/90 backdrop-blur-sm shadow-2xl">
              <CardHeader className="pb-2 pt-6">
                <CardTitle className="text-xl sm:text-2xl font-bold text-white">Fill in your details</CardTitle>
              </CardHeader>
              <CardContent className="pt-4 pb-8">
                <form onSubmit={handleSubmit} className="space-y-6">
                  {/* Full Name and Age */}
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <Label htmlFor="fullName" className="text-gray-200">Full Name</Label>
                      <Input
                        id="fullName"
                        name="fullName"
                        value={formData.fullName}
                        onChange={handleChange}
                        placeholder="Enter your full name"
                        className="bg-gray-900/70 border-gray-700 text-gray-300 placeholder-gray-500"
                      />
                      {errors.fullName && <p className="text-red-400 text-sm">{errors.fullName}</p>}
                    </div>
                    <div className="space-y-2">
                      <Label htmlFor="age" className="text-gray-200">Age</Label>
                      <Input
                        id="age"
                        name="age"
                        type="number"
                        value={formData.age}
                        onChange={handleChange}
                        placeholder="Enter your age"
                        className="bg-gray-900/70 border-gray-700 text-gray-300 placeholder-gray-500"
                      />
                      {errors.age && <p className="text-red-400 text-sm">{errors.age}</p>}
                    </div>
                  </div>

                  {/* Email and Username */}
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <Label htmlFor="email" className="text-gray-200">Email Address</Label>
                      <Input
                        id="email"
                        name="email"
                        type="email"
                        value={formData.email}
                        onChange={handleChange}
                        placeholder="Enter your email address"
                        className="bg-gray-900/70 border-gray-700 text-gray-300 placeholder-gray-500"
                      />
                      {errors.email && <p className="text-red-400 text-sm">{errors.email}</p>}
                    </div>
                    <div className="space-y-2">
                      <Label htmlFor="username" className="text-gray-200">Username</Label>
                      <Input
                        id="username"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        placeholder="Choose a username"
                        className="bg-gray-900/70 border-gray-700 text-gray-300 placeholder-gray-500"
                      />
                      {errors.username && <p className="text-red-400 text-sm">{errors.username}</p>}
                    </div>
                  </div>

                  {/* Password and Confirm Password */}
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <Label htmlFor="password" className="text-gray-200">Password</Label>
                      <Input
                        id="password"
                        name="password"
                        type="password"
                        value={formData.password}
                        onChange={handleChange}
                        placeholder="Create a password"
                        className="bg-gray-900/70 border-gray-700 text-gray-300 placeholder-gray-500"
                      />
                      {errors.password && <p className="text-red-400 text-sm">{errors.password}</p>}
                    </div>
                    <div className="space-y-2">
                      <Label htmlFor="confirmPassword" className="text-gray-200">Confirm Password</Label>
                      <Input
                        id="confirmPassword"
                        name="confirmPassword"
                        type="password"
                        value={formData.confirmPassword}
                        onChange={handleChange}
                        placeholder="Confirm your password"
                        className="bg-gray-900/70 border-gray-700 text-gray-300 placeholder-gray-500"
                      />
                      {errors.confirmPassword && <p className="text-red-400 text-sm">{errors.confirmPassword}</p>}
                    </div>
                  </div>

                  {/* Employee ID and Department */}
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <Label htmlFor="employeeId" className="text-gray-200">Government Employee ID</Label>
                      <Input
                        id="employeeId"
                        name="employeeId"
                        value={formData.employeeId}
                        onChange={handleChange}
                        placeholder="Enter your employee ID"
                        className="bg-gray-900/70 border-gray-700 text-gray-300 placeholder-gray-500"
                      />
                      {errors.employeeId && <p className="text-red-400 text-sm">{errors.employeeId}</p>}
                    </div>
                    <div className="space-y-2">
                      <Label htmlFor="departmentName" className="text-gray-200">Department Name</Label>
                      <Input
                        id="departmentName"
                        name="departmentName"
                        value={formData.departmentName}
                        onChange={handleChange}
                        placeholder="Enter your department"
                        className="bg-gray-900/70 border-gray-700 text-gray-300 placeholder-gray-500"
                      />
                      {errors.departmentName && <p className="text-red-400 text-sm">{errors.departmentName}</p>}
                    </div>
                  </div>

                  {/* Submit Button */}
                  <div className="pt-2">
                    <MotionButton
                      type="submit"
                      disabled={isSubmitting}
                      className="w-full bg-gradient-to-r from-blue-600 to-indigo-700 text-base sm:text-lg py-3 rounded-lg shadow-lg"
                      whileHover={{ scale: 1.02 }}
                      whileTap={{ scale: 0.98 }}
                    >
                      {isSubmitting ? "Submitting Request..." : "Request Registration"}
                    </MotionButton>
                  </div>

                  {/* Sign In Link */}
                  <div className="text-center mt-4">
                    <p className="text-gray-400">
                      Already have an account?{" "}
                      <Link href="/sign-in/government" className="text-blue-400 hover:text-blue-300">
                        Sign in instead
                      </Link>
                    </p>
                  </div>
                </form>
              </CardContent>
            </Card>
          </motion.div>
        </motion.div>
      </main>

      {/* Footer */}
      <footer className="bg-gray-900/80 backdrop-blur-sm border-t border-gray-800 mt-auto">
        <div className="container mx-auto px-4 py-6 text-center text-gray-400 text-sm">
          <p>© 2025 SecurePortal. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
}