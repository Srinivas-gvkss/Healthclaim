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

const PatientDashboard = ({ navigation }) => {
  const { user, logout } = useAuth();
  const [dashboardData, setDashboardData] = useState({
    activeClaims: 0,
    pendingClaims: 0,
    approvedClaims: 0,
    totalClaims: 0,
    recentClaims: [],
    notifications: []
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
      const apiData = await dashboardService.getPatientDashboard();
      // Ensure we have a valid data structure
      if (apiData && typeof apiData === 'object') {
        setDashboardData({
          activeClaims: apiData.activeClaims || 0,
          pendingClaims: apiData.pendingClaims || 0,
          approvedClaims: apiData.approvedClaims || 0,
          totalClaims: apiData.totalClaims || 0,
          recentClaims: apiData.recentClaims || [],
          notifications: apiData.notifications || []
        });
      } else {
        throw new Error('Invalid API response');
      }
    } catch (error) {
      console.error('Error loading dashboard data:', error);
      // Fallback to mock data if API fails
      const mockData = {
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
        navigation.navigate('FileClaim');
        break;
      case 2:
        navigation.navigate('MyClaims');
        break;
      case 3:
        navigation.navigate('MedicalRecords');
        break;
      case 4:
        navigation.navigate('InsuranceDetails');
        break;
      case 5:
        navigation.navigate('FindDoctors');
        break;
      case 6:
        navigation.navigate('Profile');
        break;
      default:
        Alert.alert('Coming Soon', `${item.title} feature will be available soon!`);
    }
  };

  const menuItems = [
    { id: 1, title: 'File New Claim', icon: '📝', description: 'Submit a new insurance claim', color: COLORS.primary },
    { id: 2, title: 'My Claims', icon: '📋', description: 'View and track your claims', color: COLORS.secondary },
    { id: 3, title: 'Medical Records', icon: '🏥', description: 'Access your medical history', color: COLORS.accent },
    { id: 4, title: 'Insurance Details', icon: '💳', description: 'View your policy information', color: '#4CAF50' },
    { id: 5, title: 'Find Doctors', icon: '👨‍⚕️', description: 'Search for healthcare providers', color: '#FF9800' },
    { id: 6, title: 'Profile', icon: '👤', description: 'Manage your profile', color: '#9C27B0' },
  ];

  const getStatusColor = (status) => {
    switch (status) {
      case 'Approved':
        return { color: '#4CAF50' };
      case 'Under Review':
        return { color: '#FF9800' };
      case 'Pending':
        return { color: '#2196F3' };
      case 'Rejected':
        return { color: '#F44336' };
      default:
        return { color: COLORS.textLight };
    }
  };

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
            <Text style={styles.greeting}>Welcome back,</Text>
            <Text style={styles.userName}>{user?.firstName} {user?.lastName}</Text>
            <Text style={styles.userRole}>Patient</Text>
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
            <Text style={styles.statValue}>{dashboardData?.activeClaims || 0}</Text>
            <Text style={styles.statLabel}>Active Claims</Text>
          </View>
          <View style={styles.statCard}>
            <Text style={styles.statValue}>{dashboardData?.pendingClaims || 0}</Text>
            <Text style={styles.statLabel}>Pending</Text>
          </View>
          <View style={styles.statCard}>
            <Text style={styles.statValue}>{dashboardData?.approvedClaims || 0}</Text>
            <Text style={styles.statLabel}>Approved</Text>
          </View>
        </View>

        {/* Recent Claims */}
        <View style={styles.sectionContainer}>
          <Text style={styles.sectionTitle}>Recent Claims</Text>
          {dashboardData?.recentClaims?.map((claim) => (
            <View key={claim.id} style={styles.claimCard}>
              <View style={styles.claimHeader}>
                <Text style={styles.claimNumber}>{claim.claimNumber}</Text>
                <Text style={[styles.claimStatus, getStatusColor(claim.status)]}>
                  {claim.status}
                </Text>
              </View>
              <View style={styles.claimDetails}>
                <Text style={styles.claimAmount}>{claim.amount}</Text>
                <Text style={styles.claimDate}>{claim.date}</Text>
              </View>
            </View>
          )) || (
            <View style={styles.emptyState}>
              <Text style={styles.emptyStateText}>No recent claims</Text>
            </View>
          )}
        </View>

        {/* Notifications */}
        {dashboardData?.notifications?.length > 0 && (
          <View style={styles.sectionContainer}>
            <Text style={styles.sectionTitle}>Notifications</Text>
            {dashboardData.notifications.map((notification) => (
              <View key={notification.id} style={styles.notificationCard}>
                <Text style={styles.notificationMessage}>{notification.message}</Text>
                <Text style={styles.notificationDate}>{notification.date}</Text>
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
    color: COLORS.primary,
    fontWeight: '600',
    marginTop: SPACING.xs,
  },
  profileIcon: {
    width: 60,
    height: 60,
    borderRadius: 30,
    backgroundColor: COLORS.primary,
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
    color: COLORS.primary,
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
  claimCard: {
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
  claimHeader: {
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
  claimStatus: {
    fontSize: FONT_SIZES.xs,
    fontWeight: '500',
  },
  claimDetails: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  claimAmount: {
    fontSize: FONT_SIZES.md,
    fontWeight: 'bold',
    color: COLORS.primary,
  },
  claimDate: {
    fontSize: FONT_SIZES.xs,
    color: COLORS.textLight,
  },
  notificationCard: {
    backgroundColor: COLORS.white,
    padding: SPACING.md,
    borderRadius: BORDER_RADIUS.lg,
    marginBottom: SPACING.sm,
    borderLeftWidth: 4,
    borderLeftColor: COLORS.primary,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 1,
  },
  notificationMessage: {
    fontSize: FONT_SIZES.sm,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  notificationDate: {
    fontSize: FONT_SIZES.xs,
    color: COLORS.textLight,
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

export default PatientDashboard;

