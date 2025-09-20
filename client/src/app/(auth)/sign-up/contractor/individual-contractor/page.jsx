"use client"

import { useState } from 'react';
import Link from 'next/link';
import { motion } from 'framer-motion';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { z } from 'zod';

// Define Zod schema for form validation
const contractorSchema = z.object({
  name: z.string().min(1, { message: "Name is required" }),
  age: z.number().min(18, { message: "Must be at least 18 years old" }).max(100, { message: "Invalid age" }),
  mobile: z.string().regex(/^\d{10}$/, { message: "Enter a valid 10-digit mobile number" }),
  username: z.string().min(5, { message: "Username must be at least 5 characters" }),
  password: z.string().min(8, { message: "Password must be at least 8 characters" }),
  confirmPassword: z.string(),
  email: z.string().email({ message: "Enter a valid email address" }),
  gstNumber: z.string().regex(/^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[0-9A-Z]{1}[Z]{1}[0-9A-Z]{1}$/, { message: "Enter a valid GST number" }),
  companyAddress: z.string().min(1, { message: "Company address is required" }),
  companyName: z.string().min(1, { message: "Company name is required" })
}).refine(data => data.password === data.confirmPassword, {
  message: "Passwords do not match",
  path: ["confirmPassword"]
});

export default function IndividualSignUp() {
  const [formData, setFormData] = useState({
    name: '',
    age: '',
    mobile: '',
    username: '',
    password: '',
    confirmPassword: '',
    email: '',
    gstNumber: '',
    Address: '',
  });
  
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'age' ? value : value
    }));
    
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const validateForm = () => {
    try {
      const submissionData = {
        ...formData,
        age: parseInt(formData.age)
      };
      contractorSchema.parse(submissionData);
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
    
    if (validateForm()) {
      setIsSubmitting(true);
      
      try {
        // Uncommented API call code is currently disabled
        // const response = await fetch('/api/government/register', {
        //   method: 'POST',
        //   headers: { 'Content-Type': 'application/json' },
        //   body: JSON.stringify(formData),
        // });
        // if (!response.ok) throw new Error('Registration failed');
       
        alert("Contractor account created successfully!");
      } catch (error) {
        console.error("Error submitting form:", error);
        alert("Failed to create account. Please try again later.");
      } finally {
        setIsSubmitting(false);
      }
    }
  };

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: { opacity: 1, transition: { staggerChildren: 0.1 } }
  };

  const itemVariants = {
    hidden: { y: 20, opacity: 0 },
    visible: { y: 0, opacity: 1, transition: { type: "spring", stiffness: 100, damping: 10 } }
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
        <motion.div
          className="w-full max-w-3xl mx-auto"
          initial="hidden"
          animate="visible"
          variants={containerVariants}
        >
          <motion.div variants={itemVariants} className="text-center mb-8">
            <h1 className="text-3xl sm:text-4xl font-bold text-zinc-300 mb-2">Individual Contractor Registration</h1>
            <p className="text-gray-300">Create your contractor account to access the portal</p>
          </motion.div>

          <motion.div variants={itemVariants}>
            <Card className="border border-gray-700 rounded-xl bg-gray-800/90 backdrop-blur-sm shadow-2xl">
              <CardHeader className="pb-2 pt-6">
                <CardTitle className="text-xl sm:text-2xl font-bold text-white">
                  Fill in your details
                </CardTitle>
              </CardHeader>
              <CardContent className="pt-4 pb-8">
                <form onSubmit={handleSubmit} className="space-y-6">
                  {/* Name and Age */}
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <Label htmlFor="name" className="text-gray-200">Full Name</Label>
                      <Input
                        id="name"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        placeholder="Enter your full name"
                        className="bg-gray-900/70 border-gray-700 text-gray-300 placeholder-gray-500"
                      />
                      {errors.name && <p className="text-red-400 text-sm">{errors.name}</p>}
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

                  {/* Mobile and Email */}
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <Label htmlFor="mobile" className="text-gray-200">Mobile Number</Label>
                      <Input
                        id="mobile"
                        name="mobile"
                        value={formData.mobile}
                        onChange={handleChange}
                        placeholder="Enter your 10-digit mobile"
                        className="bg-gray-900/70 border-gray-700 text-gray-300 placeholder-gray-500"
                      />
                      {errors.mobile && <p className="text-red-400 text-sm">{errors.mobile}</p>}
                    </div>
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
                  </div>

                  {/* Username and GST Number */}
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
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
                    <div className="space-y-2">
                      <Label htmlFor="gstNumber" className="text-gray-200">GST Number</Label>
                      <Input
                        id="gstNumber"
                        name="gstNumber"
                        value={formData.gstNumber}
                        onChange={handleChange}
                        placeholder="Enter your GST number"
                        className="bg-gray-900/70 border-gray-700 text-gray-300 placeholder-gray-500"
                      />
                      {errors.gstNumber && <p className="text-red-400 text-sm">{errors.gstNumber}</p>}
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


                  {/*Address */}
                  <div className="space-y-2">
                    <Label htmlFor="Address" className="text-gray-200"> Address</Label>
                    <Textarea
                      id="Address"
                      name="Address"
                      value={formData.Address}
                      onChange={handleChange}
                      placeholder="Enter your company address"
                      className="bg-gray-900/70 border-gray-700 text-gray-300 placeholder-gray-500"
                      rows={3}
                    />
                    {errors.Address && <p className="text-red-400 text-sm">{errors.Address}</p>}
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
                      {isSubmitting ? 'Creating Account...' : 'Create Contractor Account'}
                    </MotionButton>
                  </div>

                  {/* Sign In Link */}
                  <div className="text-center mt-4">
                  <p className="text-gray-400">
                      In your an CorporateContractor?{' '}
                      <Link href="/sign-up/contractor/corporate-contractor" className="text-indigo-600 hover:text-blue-300">
                        Sign up here
                      </Link>
                    </p>
                    <p className="text-gray-400">
                      Already have an account?{' '}
                      <Link href="/sign-in/contractor/individual-contractor" className="text-blue-400 hover:text-blue-300">
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