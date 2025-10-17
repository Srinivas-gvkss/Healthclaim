import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  SafeAreaView,
  RefreshControl,
  Alert,
} from 'react-native';
import { useAuth } from '../../contexts/AuthContext';
import Button from '../../components/Button';
import { COLORS, SPACING, FONT_SIZES, BORDER_RADIUS } from '../../utils/constants';
import { dashboardService } from '../../services/dashboardService';

const DoctorDashboard = ({ navigation }) => {
  const { user, logout } = useAuth();
  const [dashboardData, setDashboardData] = useState({
    patientsToday: 0,
    totalAppointments: 0,
    pendingClaims: 0,
    totalPatients: 0,
    todayAppointments: [],
    pendingVerifications: []
  });
  const [refreshing, setRefreshing] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      setLoading(true);
      // Try to get real data from API, fallback to mock data
      const apiData = await dashboardService.getDoctorDashboard();
      // Ensure we have a valid data structure
      if (apiData && typeof apiData === 'object') {
        setDashboardData({
          patientsToday: apiData.patientsToday || 0,
          totalAppointments: apiData.totalAppointments || 0,
          pendingClaims: apiData.pendingClaims || 0,
          totalPatients: apiData.totalPatients || 0,
          todayAppointments: apiData.todayAppointments || [],
          pendingVerifications: apiData.pendingVerifications || []
        });
      } else {
        throw new Error('Invalid API response');
      }
    } catch (error) {
      console.error('Error loading dashboard data:', error);
      // Fallback to mock data if API fails
      const mockData = {
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
      setDashboardData(mockData);
    } finally {
      setLoading(false);
    }
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await loadDashboardData();
    setRefreshing(false);
  };

  const handleLogout = async () => {
    try {
      await logout();
    } catch (error) {
      console.error('Logout error:', error);
    }
  };

  const handleMenuPress = (item) => {
    switch (item.id) {
      case 1:
        navigation.navigate('MyPatients');
        break;
      case 2:
        navigation.navigate('Appointments');
        break;
      case 3:
        navigation.navigate('SupportClaims');
        break;
      case 4:
        navigation.navigate('MedicalRecords');
        break;
      case 5:
        navigation.navigate('Prescriptions');
        break;
      case 6:
        navigation.navigate('Profile');
        break;
      default:
        Alert.alert('Coming Soon', `${item.title} feature will be available soon!`);
    }
  };

  const menuItems = [
    { id: 1, title: 'My Patients', icon: '👥', description: 'View patient list', color: COLORS.secondary },
    { id: 2, title: 'Appointments', icon: '📅', description: 'Manage appointments', color: '#4CAF50' },
    { id: 3, title: 'Support Claims', icon: '✅', description: 'Review and support claims', color: '#FF9800' },
    { id: 4, title: 'Medical Records', icon: '📋', description: 'Access patient records', color: '#2196F3' },
    { id: 5, title: 'Prescriptions', icon: '💊', description: 'Write prescriptions', color: '#9C27B0' },
    { id: 6, title: 'Profile', icon: '👤', description: 'Manage your profile', color: '#607D8B' },
  ];

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView 
        contentContainerStyle={styles.scrollContainer}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
      >
        {/* Header */}
        <View style={styles.header}>
          <View>
            <Text style={styles.greeting}>Welcome, Dr.</Text>
            <Text style={styles.userName}>{user?.firstName} {user?.lastName}</Text>
            <Text style={styles.userRole}>Healthcare Provider</Text>
            <Text style={styles.specialty}>{user?.specialty || 'General Medicine'}</Text>
          </View>
          <TouchableOpacity style={styles.profileIcon}>
            <Text style={styles.profileIconText}>
              {user?.firstName?.charAt(0)}{user?.lastName?.charAt(0)}
            </Text>
          </TouchableOpacity>
        </View>

        {/* Quick Stats */}
        <View style={styles.statsContainer}>
          <View style={styles.statCard}>
            <Text style={styles.statValue}>{dashboardData?.patientsToday || 0}</Text>
            <Text style={styles.statLabel}>Patients Today</Text>
          </View>
          <View style={styles.statCard}>
            <Text style={styles.statValue}>{dashboardData?.totalAppointments || 0}</Text>
            <Text style={styles.statLabel}>Appointments</Text>
          </View>
          <View style={styles.statCard}>
            <Text style={styles.statValue}>{dashboardData?.pendingClaims || 0}</Text>
            <Text style={styles.statLabel}>Pending Claims</Text>
          </View>
        </View>

        {/* Today's Appointments */}
        <View style={styles.sectionContainer}>
          <Text style={styles.sectionTitle}>Today's Appointments</Text>
          {dashboardData?.todayAppointments?.map((appointment) => (
            <View key={appointment.id} style={styles.appointmentCard}>
              <View style={styles.appointmentHeader}>
                <Text style={styles.appointmentTime}>{appointment.time}</Text>
                <Text style={styles.appointmentType}>{appointment.type}</Text>
              </View>
              <Text style={styles.appointmentPatient}>{appointment.patientName}</Text>
            </View>
          )) || (
            <View style={styles.emptyState}>
              <Text style={styles.emptyStateText}>No appointments today</Text>
            </View>
          )}
        </View>

        {/* Pending Verifications */}
        {dashboardData?.pendingVerifications?.length > 0 && (
          <View style={styles.sectionContainer}>
            <Text style={styles.sectionTitle}>Pending Claim Verifications</Text>
            {dashboardData.pendingVerifications.map((verification) => (
              <View key={verification.id} style={styles.verificationCard}>
                <View style={styles.verificationHeader}>
                  <Text style={styles.claimNumber}>{verification.claimNumber}</Text>
                  <Text style={styles.claimAmount}>{verification.amount}</Text>
                </View>
                <Text style={styles.patientName}>{verification.patientName}</Text>
                <TouchableOpacity style={styles.verifyButton}>
                  <Text style={styles.verifyButtonText}>Review Claim</Text>
                </TouchableOpacity>
              </View>
            ))}
          </View>
        )}

        {/* Menu Grid */}
        <View style={styles.menuContainer}>
          <Text style={styles.sectionTitle}>Quick Actions</Text>
          <View style={styles.menuGrid}>
            {menuItems.map((item) => (
              <TouchableOpacity
                key={item.id}
                style={[styles.menuCard, { borderLeftColor: item.color }]}
                onPress={() => handleMenuPress(item)}
              >
                <Text style={styles.menuIcon}>{item.icon}</Text>
                <Text style={styles.menuTitle}>{item.title}</Text>
                <Text style={styles.menuDescription}>{item.description}</Text>
              </TouchableOpacity>
            ))}
          </View>
        </View>

        {/* Logout Button */}
        <Button
          title="Logout"
          onPress={handleLogout}
          variant="outline"
          style={styles.logoutButton}
        />
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.backgroundLight,
  },
  scrollContainer: {
    padding: SPACING.lg,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.xl,
  },
  greeting: {
    fontSize: FONT_SIZES.md,
    color: COLORS.textLight,
  },
  userName: {
    fontSize: FONT_SIZES.xxl,
    fontWeight: 'bold',
    color: COLORS.text,
    marginTop: SPACING.xs,
  },
  userRole: {
    fontSize: FONT_SIZES.sm,
    color: COLORS.secondary,
    fontWeight: '600',
    marginTop: SPACING.xs,
  },
  specialty: {
    fontSize: FONT_SIZES.xs,
    color: COLORS.textLight,
    marginTop: SPACING.xs,
  },
  profileIcon: {
    width: 60,
    height: 60,
    borderRadius: 30,
    backgroundColor: COLORS.secondary,
    alignItems: 'center',
    justifyContent: 'center',
  },
  profileIconText: {
    fontSize: FONT_SIZES.xl,
    color: COLORS.white,
    fontWeight: 'bold',
  },
  statsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: SPACING.xl,
    gap: SPACING.sm,
  },
  statCard: {
    flex: 1,
    backgroundColor: COLORS.white,
    padding: SPACING.md,
    borderRadius: BORDER_RADIUS.lg,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  statValue: {
    fontSize: FONT_SIZES.xxl,
    fontWeight: 'bold',
    color: COLORS.secondary,
  },
  statLabel: {
    fontSize: FONT_SIZES.xs,
    color: COLORS.textLight,
    marginTop: SPACING.xs,
  },
  menuContainer: {
    marginBottom: SPACING.xl,
  },
  sectionTitle: {
    fontSize: FONT_SIZES.lg,
    fontWeight: '600',
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  menuGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: SPACING.sm,
  },
  menuCard: {
    width: '48%',
    backgroundColor: COLORS.white,
    padding: SPACING.md,
    borderRadius: BORDER_RADIUS.lg,
    alignItems: 'center',
    borderLeftWidth: 4,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  sectionContainer: {
    marginBottom: SPACING.lg,
  },
  appointmentCard: {
    backgroundColor: COLORS.white,
    padding: SPACING.md,
    borderRadius: BORDER_RADIUS.lg,
    marginBottom: SPACING.sm,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 1,
  },
  appointmentHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.xs,
  },
  appointmentTime: {
    fontSize: FONT_SIZES.sm,
    fontWeight: '600',
    color: COLORS.secondary,
  },
  appointmentType: {
    fontSize: FONT_SIZES.xs,
    color: COLORS.textLight,
    backgroundColor: COLORS.backgroundLight,
    paddingHorizontal: SPACING.xs,
    paddingVertical: 2,
    borderRadius: BORDER_RADIUS.sm,
  },
  appointmentPatient: {
    fontSize: FONT_SIZES.sm,
    color: COLORS.text,
    fontWeight: '500',
  },
  verificationCard: {
    backgroundColor: COLORS.white,
    padding: SPACING.md,
    borderRadius: BORDER_RADIUS.lg,
    marginBottom: SPACING.sm,
    borderLeftWidth: 4,
    borderLeftColor: '#FF9800',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 1,
  },
  verificationHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.xs,
  },
  claimNumber: {
    fontSize: FONT_SIZES.sm,
    fontWeight: '600',
    color: COLORS.text,
  },
  claimAmount: {
    fontSize: FONT_SIZES.sm,
    fontWeight: 'bold',
    color: COLORS.primary,
  },
  patientName: {
    fontSize: FONT_SIZES.sm,
    color: COLORS.textLight,
    marginBottom: SPACING.sm,
  },
  verifyButton: {
    backgroundColor: '#FF9800',
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.xs,
    borderRadius: BORDER_RADIUS.sm,
    alignSelf: 'flex-start',
  },
  verifyButtonText: {
    color: COLORS.white,
    fontSize: FONT_SIZES.xs,
    fontWeight: '600',
  },
  emptyState: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: SPACING.lg,
  },
  emptyStateText: {
    fontSize: FONT_SIZES.md,
    color: COLORS.textLight,
    textAlign: 'center',
  },
  menuIcon: {
    fontSize: 40,
    marginBottom: SPACING.sm,
  },
  menuTitle: {
    fontSize: FONT_SIZES.sm,
    fontWeight: '600',
    color: COLORS.text,
    textAlign: 'center',
    marginBottom: SPACING.xs,
  },
  menuDescription: {
    fontSize: FONT_SIZES.xs,
    color: COLORS.textLight,
    textAlign: 'center',
  },
  logoutButton: {
    marginTop: SPACING.lg,
  },
});

export default DoctorDashboard;

