"use client"

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { motion } from 'framer-motion';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';

export default function HomePage() {
  const [mounted, setMounted] = useState(false);
  const [isSignedIn, setIsSignedIn] = useState(false);
  const [userInitials, setUserInitials] = useState("");
  const [loading, setLoading] = useState(true);
  
  // Check authentication status when component mounts
  useEffect(() => {
    setMounted(true);
    
    // Function to check user authentication status
    const checkAuthStatus = async () => {
    
    };
    
    checkAuthStatus();
  }, []);

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.2
      }
    }
  };

  const itemVariants = {
    hidden: { y: 20, opacity: 0 },
    visible: {
      y: 0,
      opacity: 1,
      transition: {
        type: "spring",
        stiffness: 100,
        damping: 10
      }
    }
  };

  const MotionButton = motion.create(Button);
  const MotionCard = motion.create(Card);

  return (
    <div className="flex flex-col bg-gradient-to-b from-gray-900 via-gray-800 to-gray-700">
      {/* Decorative background elements */}
      <div className="fixed inset-0 overflow-hidden -z-10">
        <div className="absolute -top-16 -left-16 w-64 h-64 rounded-full bg-blue-500/10 blur-3xl"></div>
        <div className="absolute top-1/4 -right-20 w-80 h-80 rounded-full bg-indigo-500/10 blur-3xl"></div>
        <div className="absolute bottom-0 left-1/3 w-96 h-96 rounded-full bg-emerald-500/10 blur-3xl"></div>
      </div>

      {/* Optimized Header - Logo and User Avatar */}
      <header className="bg-gray-900/80 backdrop-blur-sm border-b border-gray-800">
        <div className="container mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16 sm:h-20">
            {/* Logo */}
            <div className="flex items-center">
              <div className="bg-gradient-to-r from-blue-500 to-indigo-700 rounded-full p-2 mr-3">
                <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 11c0 3.517-1.009 6.799-2.753 9.571m-3.44-2.04l.054-.09A13.916 13.916 0 008 11a4 4 0 118 0c0 1.017-.07 2.019-.203 3m-2.118 6.844A21.88 21.88 0 0015.171 17m3.839 1.132c.645-2.266.99-4.659.99-7.132A8 8 0 008 4.07M3 15.364c.64-1.319 1-2.8 1-4.364 0-1.457.39-2.823 1.07-4" />
                </svg>
              </div>
              <span className="text-xl font-bold text-white">SecurePortal</span>
            </div>
            
            {/* User Avatar (shown when logged in) or Sign In button */}
            <div>
              {loading ? (
                <div className="h-10 w-10 rounded-full bg-gray-700 animate-pulse"></div>
              ) : isSignedIn ? (
                <div className="relative">
                  <button 
                    className="flex text-sm rounded-full focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                    id="user-menu-button"
                    aria-expanded="false"
                    aria-haspopup="true"
                  >
                    <span className="sr-only">Open user menu</span>
                    <div className="h-10 w-10 rounded-full bg-indigo-600 flex items-center justify-center text-white">
                      {userInitials || "U"}
                    </div>
                  </button>
                </div>
              ) : (
                ""
              )}
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex-grow py-12 px-4 sm:px-6 lg:px-8">
        <motion.div
          className="w-full max-w-5xl mx-auto space-y-12"
          initial="hidden"
          animate={mounted ? "visible" : "hidden"}
          variants={containerVariants}
        >
          {/* Hero Section */}
          <motion.div 
            initial={{ y: -20, opacity: 0 }}
            animate={{ y: 0, opacity: 1 }}
            transition={{ duration: 0.7 }}
            className="text-center mb-8"
          >
            <h1 className="text-4xl sm:text-5xl font-bold text-white mb-3">SecurePortal</h1>
            <p className="text-gray-300 max-w-md mx-auto">Don't reinvent the wheel with every design. Team libraries let you share styles and components across files, with everyone on your team.</p>
          </motion.div>

          {/* Join As Section */}
          <MotionCard 
            variants={itemVariants} 
            className="border border-gray-700 rounded-xl bg-gray-800/90 backdrop-blur-sm shadow-2xl"
          >
            <CardHeader className="pb-2 pt-6">
              <CardTitle className="text-2xl sm:text-3xl font-bold text-white text-center">
                Join As
              </CardTitle>
              <p className="text-gray-300 text-center mt-2">
                Choose your role to access the system
              </p>
            </CardHeader>
            <CardContent className="pt-6 pb-8 px-6 sm:px-8">
              <div className="grid grid-cols-1 sm:grid-cols-3 gap-5">
                <motion.div variants={itemVariants} whileHover={{ scale: 1.03 }}>
                  <Link href="/sign-in/supplier" className="block w-full">
                    <MotionButton
                      variant="default"
                      className="w-full p-4 bg-gradient-to-r from-blue-600 to-indigo-700 text-base sm:text-lg flex items-center justify-center rounded-lg shadow-lg"
                      whileHover={{ scale: 1.04, y: -4 }}
                      whileTap={{ scale: 0.97 }}
                    >
                      <svg className="w-5 h-5 sm:w-6 sm:h-6 mr-3 sm:mr-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
                      </svg>
                      <span>Supplier</span>
                    </MotionButton>
                  </Link>
                </motion.div>
                
                <motion.div variants={itemVariants} whileHover={{ scale: 1.03 }}>
                  <Link href="/sign-up/contractor/corporate-contractor" className="block w-full">
                    <MotionButton
                      variant="default"
                      className="w-full p-4 bg-gradient-to-r from-blue-600 to-indigo-700 text-base sm:text-lg flex items-center justify-center rounded-lg shadow-lg"
                      whileHover={{ scale: 1.04, y: -4 }}
                      whileTap={{ scale: 0.97 }}
                    >
                      <svg className="w-5 h-5 sm:w-6 sm:h-6 mr-3 sm:mr-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
                      </svg>
                      <span>Contractor</span>
                    </MotionButton>
                  </Link>
                </motion.div>

                <motion.div variants={itemVariants} whileHover={{ scale: 1.03 }}>
                  <Link href="/sign-in/government" className="block w-full">
                    <MotionButton
                      variant="default"
                      className="w-full p-4 bg-gradient-to-r from-blue-600 to-indigo-700 text-base sm:text-lg flex items-center justify-center rounded-lg shadow-lg"
                      whileHover={{ scale: 1.04, y: -4 }}
                      whileTap={{ scale: 0.97 }}
                    >
                      <svg className="w-5 h-5 sm:w-6 sm:h-6 mr-3 sm:mr-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                      </svg>
                      <span>Government Official</span>
                    </MotionButton>
                  </Link>
                </motion.div>
              </div>
            </CardContent>
          </MotionCard>

          {/* Rest of the content remains the same */}
          {/* Vision Section */}
          <MotionCard 
            variants={itemVariants} 
            className="border border-gray-700 rounded-xl bg-gray-800/90 backdrop-blur-sm shadow-2xl"
          >
            <CardHeader className="pb-2 pt-6">
              <CardTitle className="text-2xl sm:text-3xl font-bold text-white text-center">
                Vision
              </CardTitle>
            </CardHeader>
            <CardContent className="pt-6 pb-8 px-6 sm:px-8">
              <p className="text-gray-300 text-center">
                Our vision is to create a seamless and secure platform that empowers government officials, suppliers, and contractors to collaborate efficiently, ensuring transparency and accountability in every process.
              </p>
            </CardContent>
          </MotionCard>

          {/* Stats Section */}
          <MotionCard 
            variants={itemVariants} 
            className="border border-gray-700 rounded-xl bg-gray-800/90 backdrop-blur-sm shadow-2xl"
          >
            <CardHeader className="pb-2 pt-6">
              <CardTitle className="text-2xl sm:text-3xl font-bold text-white text-center">
                Our Stats
              </CardTitle>
              <p className="text-gray-300 text-center mt-2">
                Real-time ticker showing total registration counts, projects uploaded, projects bids, total market caps
              </p>
            </CardHeader>
            <CardContent className="pt-6 pb-8 px-6 sm:px-8">
              <div className="grid grid-cols-2 sm:grid-cols-4 gap-6 text-center">
                <div>
                  <h3 className="text-3xl font-bold text-blue-400">1,234</h3>
                  <p className="text-gray-300 text-sm mt-1">Registrations</p>
                </div>
                <div>
                  <h3 className="text-3xl font-bold text-blue-400">567</h3>
                  <p className="text-gray-300 text-sm mt-1">Projects Uploaded</p>
                </div>
                <div>
                  <h3 className="text-3xl font-bold text-blue-400">890</h3>
                  <p className="text-gray-300 text-sm mt-1">Project Bids</p>
                </div>
                <div>
                  <h3 className="text-3xl font-bold text-blue-400">$12M</h3>
                  <p className="text-gray-300 text-sm mt-1">Total Market Cap</p>
                </div>
              </div>
            </CardContent>
          </MotionCard>

          {/* Carousel Section */}
          <MotionCard 
            variants={itemVariants} 
            className="border border-gray-700 rounded-xl bg-gray-800/90 backdrop-blur-sm shadow-2xl"
          >
            <CardContent className="pt-6 pb-8 px-6 sm:px-8">
              <div className="relative">
                <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
                  <div className="bg-gray-900/70 p-4 rounded-lg">
                    <h3 className="text-lg font-semibold text-white mb-2">Feature 1</h3>
                    <p className="text-gray-300 text-sm">Streamlined document submission for faster approvals.</p>
                  </div>
                  <div className="bg-gray-900/70 p-4 rounded-lg">
                    <h3 className="text-lg font-semibold text-white mb-2">Feature 2</h3>
                    <p className="text-gray-300 text-sm">Real-time contract tracking and updates.</p>
                  </div>
                  <div className="bg-gray-900/70 p-4 rounded-lg">
                    <h3 className="text-lg font-semibold text-white mb-2">Feature 3</h3>
                    <p className="text-gray-300 text-sm">Secure budget allocation and reporting tools.</p>
                  </div>
                </div>
                <button className="absolute top-1/2 -left-4 transform -translate-y-1/2 bg-gray-700 p-2 rounded-full">
                  <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
                  </svg>
                </button>
                <button className="absolute top-1/2 -right-4 transform -translate-y-1/2 bg-gray-700 p-2 rounded-full">
                  <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                  </svg>
                </button>
              </div>
            </CardContent>
          </MotionCard>

          {/* Contact Form Section */}
          <MotionCard 
            variants={itemVariants} 
            className="border border-gray-700 rounded-xl bg-gray-800/90 backdrop-blur-sm shadow-2xl"
          >
            <CardHeader className="pb-2 pt-6">
              <CardTitle className="text-2xl sm:text-3xl font-bold text-white text-center">
                Drop a Message
              </CardTitle>
              <p className="text-gray-300 text-center mt-2">
                Ask user email and number then allow to drop message
              </p>
            </CardHeader>
            <CardContent className="pt-6 pb-8 px-6 sm:px-8">
              <form className="space-y-5">
                <div>
                  <Input 
                    type="email" 
                    placeholder="Your Email" 
                    className="w-full bg-gray-900/70 border-gray-700 text-gray-300 placeholder-gray-500" 
                  />
                </div>
                <div>
                  <Input 
                    type="tel" 
                    placeholder="Your Phone Number" 
                    className="w-full bg-gray-900/70 border-gray-700 text-gray-300 placeholder-gray-500" 
                  />
                </div>
                <div>
                  <Textarea 
                    placeholder="Your Message" 
                    className="w-full bg-gray-900/70 border-gray-700 text-gray-300 placeholder-gray-500" 
                    rows={4}
                  />
                </div>
                <MotionButton
                  type="submit"
                  className="w-full bg-gradient-to-r from-blue-600 to-indigo-700 text-base sm:text-lg rounded-lg shadow-lg"
                  whileHover={{ scale: 1.04, y: -4 }}
                  whileTap={{ scale: 0.97 }}
                >
                  Send Message
                </MotionButton>
              </form>
            </CardContent>
          </MotionCard>
        </motion.div>
      </main>

      {/* Footer */}
      <footer className="bg-gray-900/80 backdrop-blur-sm border-t border-gray-800 mt-auto">
        <div className="container mx-auto px-4 sm:px-6 lg:px-8">
          <div className="py-6">
            <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
              <div className="md:col-span-1">
                <div className="flex items-center mb-4">
                  <div className="bg-gradient-to-r from-blue-500 to-indigo-600 rounded-full p-2 mr-3">
                    <svg className="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 11c0 3.517-1.009 6.799-2.753 9.571m-3.44-2.04l.054-.09A13.916 13.916 0 008 11a4 4 0 118 0c0 1.017-.07 2.019-.203 3m-2.118 6.844A21.88 21.88 0 0015.171 17m3.839 1.132c.645-2.266.99-4.659.99-7.132A8 8 0 008 4.07M3 15.364c.64-1.319 1-2.8 1-4.364 0-1.457.39-2.823 1.07-4" />
                    </svg>
                  </div>
                  <span className="text-lg font-bold text-white">SecurePortal</span>
                </div>
                <p className="text-sm text-gray-400">Secure access management for government, suppliers, and contractors.</p>
              </div>
              
              <div className="md:col-span-3">
                <div className="grid grid-cols-2 sm:grid-cols-3 gap-8">
                  <div>
                    <h3 className="text-sm font-semibold text-white tracking-wider uppercase mb-4">Resources</h3>
                    <ul className="space-y-2">
                      <li><a href="#" className="text-sm text-gray-400 hover:text-white transition-colors">Documentation</a></li>
                      <li><a href="#" className="text-sm text-gray-400 hover:text-white transition-colors">Tutorials</a></li>
                      <li><a href="#" className="text-sm text-gray-400 hover:text-white transition-colors">FAQs</a></li>
                    </ul>
                  </div>
                  
                  <div>
                    <h3 className="text-sm font-semibold text-white tracking-wider uppercase mb-4">Company</h3>
                    <ul className="space-y-2">
                      <li><a href="#" className="text-sm text-gray-400 hover:text-white transition-colors">About Us</a></li>
                      <li><a href="#" className="text-sm text-gray-400 hover:text-white transition-colors">Careers</a></li>
                      <li><a href="#" className="text-sm text-gray-400 hover:text-white transition-colors">Contact</a></li>
                    </ul>
                  </div>
                  
                  <div>
                    <h3 className="text-sm font-semibold text-white tracking-wider uppercase mb-4">Legal</h3>
                    <ul className="space-y-2">
                      <li><a href="#" className="text-sm text-gray-400 hover:text-white transition-colors">Privacy Policy</a></li>
                      <li><a href="#" className="text-sm text-gray-400 hover:text-white transition-colors">Terms of Service</a></li>
                      <li><a href="#" className="text-sm text-gray-400 hover:text-white transition-colors">Security</a></li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
            
            <div className="border-t border-gray-800 mt-8 pt-8 flex flex-col md:flex-row justify-between items-center">
              <p className="text-sm text-gray-400">© 2025 SecurePortal. All rights reserved.</p>
              <div className="flex space-x-6 mt-4 md:mt-0">
                <a href="#" className="text-gray-400 hover:text-white">
                  <span className="sr-only">Facebook</span>
                  <svg className="h-5 w-5" fill="currentColor" viewBox="0 0 24 24">
                    <path fillRule="evenodd" d="M22 12c0-5.523-4.477-10-10-10S2 6.477 2 12c0 4.991 3.657 9.128 8.438 9.878v-6.987h-2.54V12h2.54V9.797c0-2.506 1.492-3.89 3.777-3.89 1.094 0 2.238.195 2.238.195v2.46h-1.26c-1.243 0-1.63.771-1.63 1.562V12h2.773l-.443 2.89h-2.33v6.988C18.343 21.128 22 16.991 22 12z" clipRule="evenodd" />
                  </svg>
                </a>
                <a href="#" className="text-gray-400 hover:text-white">
                  <span className="sr-only">Twitter</span>
                  <svg className="h-5 w-5" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M8.29 20.251c7.547 0 11.675-6.253 11.675-11.675 0-.178 0-.355-.012-.53A8.348 8.348 0 0022 5.92a8.19 8.19 0 01-2.357.646 4.118 4.118 0 001.804-2.27 8.224 8.224 0 01-2.605.996 4.107 4.107 0 00-6.993 3.743 11.65 11.65 0 01-8.457-4.287 4.106 4.106 0 001.27 5.477A4.072 4.072 0 012.8 9.713v.052a4.105 4.105 0 003.292 4.022 4.095 4.095 0 01-1.853.07 4.108 4.108 0 003.834 2.85A8.233 8.233 0 012 18.407a11.616 11.616 0 006.29 1.84" />
                  </svg>
                </a>
                <a href="#" className="text-gray-400 hover:text-white">
                  <span className="sr-only">LinkedIn</span>
                  <svg className="h-5 w-5" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M19 0h-14c-2.761 0-5 2.239-5 5v14c0 2.761 2.239 5 5 5h14c2.762 0 5-2.239 5-5v-14c0-2.761-2.238-5-5-5zm-11 19h-3v-11h3v11zm-1.5-12.268c-.966 0-1.75-.79-1.75-1.764s.784-1.764 1.75-1.764 1.75.79 1.75 1.764-.783 1.764-1.75 1.764zm13.5 12.268h-3v-5.604c0-3.368-4-3.113-4 0v5.604h-3v-11h3v1.765c1.396-2.586 7-2.777 7 2.476v6.759z"/>
                  </svg>
                </a>
              </div>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
}