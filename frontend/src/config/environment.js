import { Platform } from 'react-native';

/**
 * Environment configuration for API endpoints
 * 
 * Android Emulator: Use 10.0.2.2 to access host machine's localhost
 * iOS Simulator: Use localhost
 * Physical Device: Use your computer's IP address (e.g., 192.168.1.100)
 * 
 * To find your IP address:
 * - Windows: ipconfig
 * - Mac/Linux: ifconfig or ip addr
 */

// Development configurations
const DEV_CONFIG = {
  // For Android Emulator (10.0.2.2 maps to host's localhost)
  ANDROID_API_URL: 'http://10.0.2.2:8080/api',
  
  // For iOS Simulator
  IOS_API_URL: 'http://localhost:8080/api',
  
  // For Physical Device (replace with your computer's IP address)
  PHYSICAL_DEVICE_API_URL: 'http://192.168.1.100:8080/api',
};

// Production configuration (replace with your production API URL)
const PROD_CONFIG = {
  API_URL: 'https://api.yourdomain.com/api',
};

// Determine which API URL to use
const getApiUrl = () => {
  if (__DEV__) {
    // Development mode
    if (Platform.OS === 'android') {
      return DEV_CONFIG.ANDROID_API_URL;
    } else if (Platform.OS === 'ios') {
      return DEV_CONFIG.IOS_API_URL;
    }
    // Fallback for web or other platforms
    return DEV_CONFIG.IOS_API_URL;
  } else {
    // Production mode
    return PROD_CONFIG.API_URL;
  }
};

export const API_BASE_URL = getApiUrl();

export const API_ENDPOINTS = {
  // Auth endpoints
  LOGIN: '/auth/login',
  SIGNUP: '/auth/signup',
  LOGOUT: '/auth/logout',
  REFRESH: '/auth/refresh',
  ME: '/auth/me',
  
  // User endpoints
  USERS: '/users',
  USER_PROFILE: '/users/profile',
  
  // Add more endpoints as needed
};

// Request timeout in milliseconds
export const REQUEST_TIMEOUT = 10000;

// Storage keys
export const STORAGE_KEYS = {
  USER_TOKEN: 'userToken',
  REFRESH_TOKEN: 'refreshToken',
  USER_DATA: 'userData',
};

export default {
  API_BASE_URL,
  API_ENDPOINTS,
  REQUEST_TIMEOUT,
  STORAGE_KEYS,
};

