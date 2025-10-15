import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  SafeAreaView,
  Alert,
  RefreshControl,
} from 'react-native';
import { useAuth } from '../../contexts/AuthContext';
import Button from '../../components/Button';
import { COLORS, SPACING, FONT_SIZES, BORDER_RADIUS } from '../../utils/constants';
import api from '../../services/authService';

const PatientDashboard = ({ navigation }) => {
  const { user, logout } = useAuth();
  const [stats, setStats] = useState({
    activeClaims: 0,
    pendingClaims: 0,
    approvedClaims: 0,
    totalClaims: 0
  });
  const [recentClaims, setRecentClaims] = useState([]);
  const [loading, setLoading] = useState(false);
  const [refreshing, setRefreshing] = useState(false);

  const handleLogout = async () => {
    try {
      await logout();
    } catch (error) {
      console.error('Logout error:', error);
    }
  };

  const loadDashboardData = async () => {
    try {
      setLoading(true);
      // TODO: Replace with actual API calls when backend endpoints are ready
      // const claimsResponse = await api.get('/claims/my-claims');
      // const statsResponse = await api.get('/claims/stats');
      
      // Mock data for now
      setStats({
        activeClaims: 3,
        pendingClaims: 2,
        approvedClaims: 5,
        totalClaims: 10
      });
      
      setRecentClaims([
        { id: 1, claimNumber: 'CLM-001', status: 'Under Review', amount: '$1,200', date: '2024-01-15' },
        { id: 2, claimNumber: 'CLM-002', status: 'Approved', amount: '$850', date: '2024-01-10' },
        { id: 3, claimNumber: 'CLM-003', status: 'Pending', amount: '$2,100', date: '2024-01-08' },
      ]);
    } catch (error) {
      console.error('Error loading dashboard data:', error);
      Alert.alert('Error', 'Failed to load dashboard data');
    } finally {
      setLoading(false);
    }
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await loadDashboardData();
    setRefreshing(false);
  };

  useEffect(() => {
    loadDashboardData();
  }, []);

  const menuItems = [
    { 
      id: 1, 
      title: 'File New Claim', 
      icon: '📝', 
      description: 'Submit a new insurance claim',
      onPress: () => Alert.alert('Coming Soon', 'File New Claim feature will be available soon!')
    },
    { 
      id: 2, 
      title: 'My Claims', 
      icon: '📋', 
      description: 'View and track your claims',
      onPress: () => Alert.alert('Coming Soon', 'My Claims feature will be available soon!')
    },
    { 
      id: 3, 
      title: 'Medical Records', 
      icon: '🏥', 
      description: 'Access your medical history',
      onPress: () => Alert.alert('Coming Soon', 'Medical Records feature will be available soon!')
    },
    { 
      id: 4, 
      title: 'Insurance Details', 
      icon: '💳', 
      description: 'View your policy information',
      onPress: () => Alert.alert('Coming Soon', 'Insurance Details feature will be available soon!')
    },
    { 
      id: 5, 
      title: 'Find Doctors', 
      icon: '👨‍⚕️', 
      description: 'Search for healthcare providers',
      onPress: () => Alert.alert('Coming Soon', 'Find Doctors feature will be available soon!')
    },
    { 
      id: 6, 
      title: 'Profile', 
      icon: '👤', 
      description: 'Manage your profile',
      onPress: () => Alert.alert('Coming Soon', 'Profile management will be available soon!')
    },
  ];

  const getStatusColor = (status) => {
    switch (status.toLowerCase()) {
      case 'approved': return COLORS.success;
      case 'pending': return COLORS.warning;
      case 'under review': return COLORS.info;
      case 'rejected': return COLORS.error;
      default: return COLORS.textLight;
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
            <Text style={styles.statValue}>{stats.activeClaims}</Text>
            <Text style={styles.statLabel}>Active Claims</Text>
          </View>
          <View style={styles.statCard}>
            <Text style={styles.statValue}>{stats.pendingClaims}</Text>
            <Text style={styles.statLabel}>Pending</Text>
          </View>
          <View style={styles.statCard}>
            <Text style={styles.statValue}>{stats.approvedClaims}</Text>
            <Text style={styles.statLabel}>Approved</Text>
          </View>
        </View>

        {/* Recent Claims */}
        {recentClaims.length > 0 && (
          <View style={styles.recentClaimsContainer}>
            <Text style={styles.sectionTitle}>Recent Claims</Text>
            {recentClaims.map((claim) => (
              <TouchableOpacity key={claim.id} style={styles.claimCard}>
                <View style={styles.claimHeader}>
                  <Text style={styles.claimNumber}>{claim.claimNumber}</Text>
                  <View style={[styles.statusBadge, { backgroundColor: getStatusColor(claim.status) }]}>
                    <Text style={styles.statusText}>{claim.status}</Text>
                  </View>
                </View>
                <View style={styles.claimDetails}>
                  <Text style={styles.claimAmount}>{claim.amount}</Text>
                  <Text style={styles.claimDate}>{claim.date}</Text>
                </View>
              </TouchableOpacity>
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
                style={styles.menuCard}
                onPress={item.onPress}
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
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
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
  recentClaimsContainer: {
    marginBottom: SPACING.xl,
  },
  claimCard: {
    backgroundColor: COLORS.white,
    padding: SPACING.md,
    borderRadius: BORDER_RADIUS.lg,
    marginBottom: SPACING.sm,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  claimHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.sm,
  },
  claimNumber: {
    fontSize: FONT_SIZES.md,
    fontWeight: '600',
    color: COLORS.text,
  },
  statusBadge: {
    paddingHorizontal: SPACING.sm,
    paddingVertical: SPACING.xs,
    borderRadius: BORDER_RADIUS.sm,
  },
  statusText: {
    fontSize: FONT_SIZES.xs,
    color: COLORS.white,
    fontWeight: '600',
  },
  claimDetails: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  claimAmount: {
    fontSize: FONT_SIZES.lg,
    fontWeight: 'bold',
    color: COLORS.primary,
  },
  claimDate: {
    fontSize: FONT_SIZES.sm,
    color: COLORS.textLight,
  },
});

export default PatientDashboard;

