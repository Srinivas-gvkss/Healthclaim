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

const InsuranceProviderDashboard = ({ navigation }) => {
  const { user, logout } = useAuth();
  const [dashboardData, setDashboardData] = useState({
    pendingReview: 0,
    approvedToday: 0,
    totalClaims: 0,
    rejectionRate: 0,
    pendingClaims: [],
    recentApprovals: []
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
      const apiData = await dashboardService.getInsuranceDashboard();
      // Ensure we have a valid data structure
      if (apiData && typeof apiData === 'object') {
        setDashboardData({
          pendingReview: apiData.pendingReview || 0,
          approvedToday: apiData.approvedToday || 0,
          totalClaims: apiData.totalClaims || 0,
          rejectionRate: apiData.rejectionRate || 0,
          pendingClaims: apiData.pendingClaims || [],
          recentApprovals: apiData.recentApprovals || []
        });
      } else {
        throw new Error('Invalid API response');
      }
    } catch (error) {
      console.error('Error loading dashboard data:', error);
      // Fallback to mock data if API fails
      const mockData = {
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
        navigation.navigate('ReviewClaims');
        break;
      case 2:
        navigation.navigate('ApproveClaims');
        break;
      case 3:
        navigation.navigate('ClaimHistory');
        break;
      case 4:
        navigation.navigate('PolicyManagement');
        break;
      case 5:
        navigation.navigate('Reports');
        break;
      case 6:
        navigation.navigate('Profile');
        break;
      default:
        Alert.alert('Coming Soon', `${item.title} feature will be available soon!`);
    }
  };

  const handleClaimAction = (claimId, action) => {
    Alert.alert(
      'Claim Action',
      `Are you sure you want to ${action} this claim?`,
      [
        { text: 'Cancel', style: 'cancel' },
        { text: action, onPress: () => console.log(`${action} claim ${claimId}`) }
      ]
    );
  };

  const getPriorityColor = (priority) => {
    switch (priority) {
      case 'High':
        return '#F44336';
      case 'Medium':
        return '#FF9800';
      case 'Low':
        return '#4CAF50';
      default:
        return COLORS.textLight;
    }
  };

  const menuItems = [
    { id: 1, title: 'Review Claims', icon: '📋', description: 'Claims pending review', color: COLORS.accent },
    { id: 2, title: 'Approve Claims', icon: '✅', description: 'Process approvals', color: '#4CAF50' },
    { id: 3, title: 'Claim History', icon: '📊', description: 'View processed claims', color: '#2196F3' },
    { id: 4, title: 'Policy Management', icon: '📄', description: 'Manage policies', color: '#FF9800' },
    { id: 5, title: 'Reports', icon: '📈', description: 'Generate reports', color: '#9C27B0' },
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
            <Text style={styles.greeting}>Welcome,</Text>
            <Text style={styles.userName}>{user?.firstName} {user?.lastName}</Text>
            <Text style={styles.userRole}>Insurance Provider</Text>
            <Text style={styles.rejectionRate}>Rejection Rate: {dashboardData.rejectionRate}%</Text>
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
            <Text style={styles.statValue}>{dashboardData?.pendingReview || 0}</Text>
            <Text style={styles.statLabel}>Pending Review</Text>
          </View>
          <View style={styles.statCard}>
            <Text style={styles.statValue}>{dashboardData?.approvedToday || 0}</Text>
            <Text style={styles.statLabel}>Approved Today</Text>
          </View>
          <View style={styles.statCard}>
            <Text style={styles.statValue}>{(dashboardData?.totalClaims || 0).toLocaleString()}</Text>
            <Text style={styles.statLabel}>Total Claims</Text>
          </View>
        </View>

        {/* Pending Claims */}
        <View style={styles.sectionContainer}>
          <Text style={styles.sectionTitle}>Pending Claims Review</Text>
          {dashboardData?.pendingClaims?.map((claim) => (
            <View key={claim.id} style={styles.claimCard}>
              <View style={styles.claimHeader}>
                <Text style={styles.claimNumber}>{claim.claimNumber}</Text>
                <Text style={[styles.priorityBadge, { color: getPriorityColor(claim.priority) }]}>
                  {claim.priority}
                </Text>
              </View>
              <View style={styles.claimDetails}>
                <Text style={styles.patientName}>{claim.patientName}</Text>
                <Text style={styles.claimAmount}>{claim.amount}</Text>
              </View>
              <View style={styles.claimActions}>
                <TouchableOpacity 
                  style={styles.approveButton}
                  onPress={() => handleClaimAction(claim.id, 'Approve')}
                >
                  <Text style={styles.approveButtonText}>Approve</Text>
                </TouchableOpacity>
                <TouchableOpacity 
                  style={styles.rejectButton}
                  onPress={() => handleClaimAction(claim.id, 'Reject')}
                >
                  <Text style={styles.rejectButtonText}>Reject</Text>
                </TouchableOpacity>
              </View>
            </View>
          )) || (
            <View style={styles.emptyState}>
              <Text style={styles.emptyStateText}>No pending claims</Text>
            </View>
          )}
        </View>

        {/* Recent Approvals */}
        <View style={styles.sectionContainer}>
          <Text style={styles.sectionTitle}>Recent Approvals</Text>
          {dashboardData?.recentApprovals?.map((approval) => (
            <View key={approval.id} style={styles.approvalCard}>
              <View style={styles.approvalHeader}>
                <Text style={styles.claimNumber}>{approval.claimNumber}</Text>
                <Text style={styles.approvalDate}>{approval.date}</Text>
              </View>
              <View style={styles.approvalDetails}>
                <Text style={styles.patientName}>{approval.patientName}</Text>
                <Text style={styles.claimAmount}>{approval.amount}</Text>
              </View>
            </View>
          )) || (
            <View style={styles.emptyState}>
              <Text style={styles.emptyStateText}>No recent approvals</Text>
            </View>
          )}
        </View>

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
    color: COLORS.accent,
    fontWeight: '600',
    marginTop: SPACING.xs,
  },
  rejectionRate: {
    fontSize: FONT_SIZES.xs,
    color: COLORS.textLight,
    marginTop: SPACING.xs,
  },
  profileIcon: {
    width: 60,
    height: 60,
    borderRadius: 30,
    backgroundColor: COLORS.accent,
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
    color: COLORS.accent,
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
  priorityBadge: {
    fontSize: FONT_SIZES.xs,
    fontWeight: '500',
  },
  claimDetails: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.sm,
  },
  patientName: {
    fontSize: FONT_SIZES.sm,
    color: COLORS.textLight,
  },
  claimAmount: {
    fontSize: FONT_SIZES.md,
    fontWeight: 'bold',
    color: COLORS.primary,
  },
  claimActions: {
    flexDirection: 'row',
    gap: SPACING.sm,
  },
  approveButton: {
    backgroundColor: '#4CAF50',
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.xs,
    borderRadius: BORDER_RADIUS.sm,
    flex: 1,
  },
  approveButtonText: {
    color: COLORS.white,
    fontSize: FONT_SIZES.xs,
    fontWeight: '600',
    textAlign: 'center',
  },
  rejectButton: {
    backgroundColor: '#F44336',
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.xs,
    borderRadius: BORDER_RADIUS.sm,
    flex: 1,
  },
  rejectButtonText: {
    color: COLORS.white,
    fontSize: FONT_SIZES.xs,
    fontWeight: '600',
    textAlign: 'center',
  },
  approvalCard: {
    backgroundColor: COLORS.white,
    padding: SPACING.md,
    borderRadius: BORDER_RADIUS.lg,
    marginBottom: SPACING.sm,
    borderLeftWidth: 4,
    borderLeftColor: '#4CAF50',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 1,
  },
  approvalHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.xs,
  },
  approvalDate: {
    fontSize: FONT_SIZES.xs,
    color: COLORS.textLight,
  },
  approvalDetails: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
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

export default InsuranceProviderDashboard;

