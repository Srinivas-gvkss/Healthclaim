import api from './api';

/**
 * Dashboard Service - Handles all dashboard-related API calls
 */
export const dashboardService = {
  
  // Patient Dashboard Data
  getPatientDashboard: async () => {
    try {
      const response = await api.get('/users/dashboard');
      return response.data.data;
    } catch (error) {
      console.error('Error fetching patient dashboard:', error);
      // Return mock data for now
      return {
        activeClaims: 3,
        pendingClaims: 2,
        approvedClaims: 8,
        totalClaims: 13,
        recentClaims: [
          { id: 1, claimNumber: 'CLM-001', status: 'Under Review', amount: '$1,250', date: '2025-10-15' },
          { id: 2, claimNumber: 'CLM-002', status: 'Approved', amount: '$850', date: '2025-10-14' },
          { id: 3, claimNumber: 'CLM-003', status: 'Pending', amount: '$2,100', date: '2025-10-13' }
        ],
        notifications: [
          { id: 1, message: 'Claim CLM-001 is under review', type: 'info', date: '2025-10-15' },
          { id: 2, message: 'Claim CLM-002 has been approved', type: 'success', date: '2025-10-14' }
        ]
      };
    }
  },

  // Doctor Dashboard Data
  getDoctorDashboard: async () => {
    try {
      const response = await api.get('/users/dashboard');
      return response.data.data;
    } catch (error) {
      console.error('Error fetching doctor dashboard:', error);
      // Return mock data for now
      return {
        patientsToday: 8,
        totalAppointments: 12,
        pendingClaims: 5,
        totalPatients: 156,
        todayAppointments: [
          { id: 1, patientName: 'John Doe', time: '09:00 AM', type: 'Consultation' },
          { id: 2, patientName: 'Jane Smith', time: '10:30 AM', type: 'Follow-up' },
          { id: 3, patientName: 'Mike Johnson', time: '02:00 PM', type: 'Check-up' }
        ],
        pendingVerifications: [
          { id: 1, claimNumber: 'CLM-001', patientName: 'John Doe', amount: '$1,250' },
          { id: 2, claimNumber: 'CLM-002', patientName: 'Jane Smith', amount: '$850' }
        ]
      };
    }
  },

  // Admin Dashboard Data
  getAdminDashboard: async () => {
    try {
      const response = await api.get('/users/dashboard');
      return response.data.data;
    } catch (error) {
      console.error('Error fetching admin dashboard:', error);
      // Return mock data for now
      return {
        totalUsers: 1247,
        activeClaims: 89,
        totalDepartments: 12,
        systemHealth: 'Good',
        recentActivities: [
          { id: 1, action: 'New user registered', user: 'John Doe', time: '2 minutes ago' },
          { id: 2, action: 'Claim approved', user: 'Jane Smith', time: '5 minutes ago' },
          { id: 3, action: 'Department updated', user: 'Admin', time: '10 minutes ago' }
        ],
        systemStats: {
          uptime: '99.9%',
          responseTime: '120ms',
          errorRate: '0.1%'
        }
      };
    }
  },

  // Insurance Provider Dashboard Data
  getInsuranceDashboard: async () => {
    try {
      const response = await api.get('/users/dashboard');
      return response.data.data;
    } catch (error) {
      console.error('Error fetching insurance dashboard:', error);
      // Return mock data for now
      return {
        pendingReview: 23,
        approvedToday: 8,
        totalClaims: 1247,
        rejectionRate: 12.5,
        pendingClaims: [
          { id: 1, claimNumber: 'CLM-001', patientName: 'John Doe', amount: '$1,250', priority: 'High' },
          { id: 2, claimNumber: 'CLM-002', patientName: 'Jane Smith', amount: '$850', priority: 'Medium' },
          { id: 3, claimNumber: 'CLM-003', patientName: 'Mike Johnson', amount: '$2,100', priority: 'Low' }
        ],
        recentApprovals: [
          { id: 1, claimNumber: 'CLM-004', patientName: 'Sarah Wilson', amount: '$1,500', date: '2025-10-15' },
          { id: 2, claimNumber: 'CLM-005', patientName: 'David Brown', amount: '$750', date: '2025-10-14' }
        ]
      };
    }
  },

  // Get user profile
  getUserProfile: async () => {
    try {
      const response = await api.get('/users/profile');
      return response.data.data;
    } catch (error) {
      console.error('Error fetching user profile:', error);
      throw error;
    }
  },

  // Update user profile
  updateUserProfile: async (profileData) => {
    try {
      const response = await api.put('/users/profile', profileData);
      return response.data.data;
    } catch (error) {
      console.error('Error updating user profile:', error);
      throw error;
    }
  }
};

export default dashboardService;
