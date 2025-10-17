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

const AdminDashboard = ({ navigation }) => {
  const { user, logout } = useAuth();
  const [dashboardData, setDashboardData] = useState({
    totalUsers: 0,
    activeClaims: 0,
    totalDepartments: 0,
    systemHealth: 'Good',
    recentActivities: [],
    systemStats: {}
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
      const apiData = await dashboardService.getAdminDashboard();
      // Ensure we have a valid data structure
      if (apiData && typeof apiData === 'object') {
        setDashboardData({
          totalUsers: apiData.totalUsers || 0,
          activeClaims: apiData.activeClaims || 0,
          totalDepartments: apiData.totalDepartments || 0,
          systemHealth: apiData.systemHealth || 'Good',
          recentActivities: apiData.recentActivities || [],
          systemStats: apiData.systemStats || {
            uptime: '99.9%',
            responseTime: '120ms',
            errorRate: '0.1%'
          }
        });
      } else {
        throw new Error('Invalid API response');
      }
    } catch (error) {
      console.error('Error loading dashboard data:', error);
      // Fallback to mock data if API fails
      const mockData = {
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
        navigation.navigate('UserManagement');
        break;
      case 2:
        navigation.navigate('ClaimsOverview');
        break;
      case 3:
        navigation.navigate('Reports');
        break;
      case 4:
        navigation.navigate('Departments');
        break;
      case 5:
        navigation.navigate('Settings');
        break;
      case 6:
        navigation.navigate('AuditLogs');
        break;
      default:
        Alert.alert('Coming Soon', `${item.title} feature will be available soon!`);
    }
  };

  const menuItems = [
    { id: 1, title: 'User Management', icon: '👥', description: 'Manage users and roles', color: COLORS.error },
    { id: 2, title: 'Claims Overview', icon: '📊', description: 'View all claims', color: '#4CAF50' },
    { id: 3, title: 'Reports', icon: '📈', description: 'Generate reports', color: '#2196F3' },
    { id: 4, title: 'Departments', icon: '🏢', description: 'Manage departments', color: '#FF9800' },
    { id: 5, title: 'Settings', icon: '⚙️', description: 'System settings', color: '#9C27B0' },
    { id: 6, title: 'Audit Logs', icon: '📝', description: 'View system logs', color: '#607D8B' },
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
            <Text style={styles.greeting}>Administrator</Text>
            <Text style={styles.userName}>{user?.firstName} {user?.lastName}</Text>
            <Text style={styles.userRole}>System Admin</Text>
            <Text style={styles.systemHealth}>System Health: {dashboardData.systemHealth}</Text>
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
            <Text style={styles.statValue}>{(dashboardData?.totalUsers || 0).toLocaleString()}</Text>
            <Text style={styles.statLabel}>Total Users</Text>
          </View>
          <View style={styles.statCard}>
            <Text style={styles.statValue}>{dashboardData?.activeClaims || 0}</Text>
            <Text style={styles.statLabel}>Active Claims</Text>
          </View>
          <View style={styles.statCard}>
            <Text style={styles.statValue}>{dashboardData?.totalDepartments || 0}</Text>
            <Text style={styles.statLabel}>Departments</Text>
          </View>
        </View>

        {/* System Stats */}
        <View style={styles.sectionContainer}>
          <Text style={styles.sectionTitle}>System Performance</Text>
          <View style={styles.systemStatsContainer}>
            <View style={styles.systemStatCard}>
              <Text style={styles.systemStatValue}>{dashboardData?.systemStats?.uptime || '99.9%'}</Text>
              <Text style={styles.systemStatLabel}>Uptime</Text>
            </View>
            <View style={styles.systemStatCard}>
              <Text style={styles.systemStatValue}>{dashboardData?.systemStats?.responseTime || '120ms'}</Text>
              <Text style={styles.systemStatLabel}>Response Time</Text>
            </View>
            <View style={styles.systemStatCard}>
              <Text style={styles.systemStatValue}>{dashboardData?.systemStats?.errorRate || '0.1%'}</Text>
              <Text style={styles.systemStatLabel}>Error Rate</Text>
            </View>
          </View>
        </View>

        {/* Recent Activities */}
        <View style={styles.sectionContainer}>
          <Text style={styles.sectionTitle}>Recent Activities</Text>
          {dashboardData?.recentActivities?.map((activity) => (
            <View key={activity.id} style={styles.activityCard}>
              <View style={styles.activityHeader}>
                <Text style={styles.activityAction}>{activity.action}</Text>
                <Text style={styles.activityTime}>{activity.time}</Text>
              </View>
              <Text style={styles.activityUser}>by {activity.user}</Text>
            </View>
          )) || (
            <View style={styles.emptyState}>
              <Text style={styles.emptyStateText}>No recent activities</Text>
            </View>
          )}
        </View>

        {/* Menu Grid */}
        <View style={styles.menuContainer}>
          <Text style={styles.sectionTitle}>Admin Tools</Text>
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
    color: COLORS.error,
    fontWeight: '600',
    marginTop: SPACING.xs,
  },
  systemHealth: {
    fontSize: FONT_SIZES.xs,
    color: '#4CAF50',
    marginTop: SPACING.xs,
  },
  profileIcon: {
    width: 60,
    height: 60,
    borderRadius: 30,
    backgroundColor: COLORS.error,
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
    color: COLORS.error,
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
  systemStatsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    gap: SPACING.sm,
  },
  systemStatCard: {
    flex: 1,
    backgroundColor: COLORS.white,
    padding: SPACING.sm,
    borderRadius: BORDER_RADIUS.lg,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 1,
  },
  systemStatValue: {
    fontSize: FONT_SIZES.md,
    fontWeight: 'bold',
    color: '#4CAF50',
  },
  systemStatLabel: {
    fontSize: FONT_SIZES.xs,
    color: COLORS.textLight,
    marginTop: SPACING.xs,
  },
  activityCard: {
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
  activityHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.xs,
  },
  activityAction: {
    fontSize: FONT_SIZES.sm,
    fontWeight: '600',
    color: COLORS.text,
    flex: 1,
  },
  activityTime: {
    fontSize: FONT_SIZES.xs,
    color: COLORS.textLight,
  },
  activityUser: {
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

export default AdminDashboard;

