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
import { COLORS, SPACING, FONT_SIZES, BORDER_RADIUS } from '../../utils/constants';

const MyClaimsScreen = ({ navigation }) => {
  const { user } = useAuth();
  const [claims, setClaims] = useState([]);
  const [filter, setFilter] = useState('all');
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const filters = [
    { id: 'all', label: 'All Claims' },
    { id: 'pending', label: 'Pending' },
    { id: 'approved', label: 'Approved' },
    { id: 'rejected', label: 'Rejected' },
  ];

  useEffect(() => {
    loadClaims();
  }, [filter]);

  const loadClaims = async () => {
    try {
      setLoading(true);
      // TODO: Replace with actual API call
      const mockClaims = [
        {
          id: 1,
          claimNumber: 'CLM-001',
          type: 'Medical Treatment',
          provider: 'City General Hospital',
          amount: '$1,250.00',
          status: 'Under Review',
          submittedDate: '2025-10-15',
          serviceDate: '2025-10-10',
          description: 'Emergency room visit for chest pain'
        },
        {
          id: 2,
          claimNumber: 'CLM-002',
          type: 'Pharmacy',
          provider: 'MedCare Pharmacy',
          amount: '$85.50',
          status: 'Approved',
          submittedDate: '2025-10-14',
          serviceDate: '2025-10-12',
          description: 'Prescription medication'
        },
        {
          id: 3,
          claimNumber: 'CLM-003',
          type: 'Laboratory',
          provider: 'LabCorp',
          amount: '$320.00',
          status: 'Pending',
          submittedDate: '2025-10-13',
          serviceDate: '2025-10-11',
          description: 'Blood work and lab tests'
        },
        {
          id: 4,
          claimNumber: 'CLM-004',
          type: 'Dental Care',
          provider: 'Smile Dental Clinic',
          amount: '$450.00',
          status: 'Rejected',
          submittedDate: '2025-10-12',
          serviceDate: '2025-10-09',
          description: 'Dental cleaning and checkup'
        }
      ];

      // Filter claims based on selected filter
      const filteredClaims = filter === 'all' 
        ? mockClaims 
        : mockClaims.filter(claim => claim.status.toLowerCase().includes(filter.toLowerCase()));
      
      setClaims(filteredClaims);
    } catch (error) {
      console.error('Error loading claims:', error);
      Alert.alert('Error', 'Failed to load claims');
    } finally {
      setLoading(false);
    }
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await loadClaims();
    setRefreshing(false);
  };

  const getStatusColor = (status) => {
    switch (status.toLowerCase()) {
      case 'approved':
        return '#4CAF50';
      case 'under review':
        return '#FF9800';
      case 'pending':
        return '#2196F3';
      case 'rejected':
        return '#F44336';
      default:
        return COLORS.textLight;
    }
  };

  const handleClaimPress = (claim) => {
    navigation.navigate('ClaimDetails', { claimId: claim.id });
  };

  return (
    <SafeAreaView style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <Text style={styles.title}>My Claims</Text>
        <Text style={styles.subtitle}>Track and manage your insurance claims</Text>
      </View>

      {/* Filter Tabs */}
      <View style={styles.filterContainer}>
        <ScrollView horizontal showsHorizontalScrollIndicator={false}>
          {filters.map((filterItem) => (
            <TouchableOpacity
              key={filterItem.id}
              style={[
                styles.filterTab,
                filter === filterItem.id && styles.filterTabActive
              ]}
              onPress={() => setFilter(filterItem.id)}
            >
              <Text style={[
                styles.filterTabText,
                filter === filterItem.id && styles.filterTabTextActive
              ]}>
                {filterItem.label}
              </Text>
            </TouchableOpacity>
          ))}
        </ScrollView>
      </View>

      {/* Claims List */}
      <ScrollView 
        style={styles.claimsList}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
      >
        {claims.map((claim) => (
          <TouchableOpacity
            key={claim.id}
            style={styles.claimCard}
            onPress={() => handleClaimPress(claim)}
          >
            <View style={styles.claimHeader}>
              <Text style={styles.claimNumber}>{claim.claimNumber}</Text>
              <Text style={[styles.claimStatus, { color: getStatusColor(claim.status) }]}>
                {claim.status}
              </Text>
            </View>
            
            <View style={styles.claimDetails}>
              <Text style={styles.claimType}>{claim.type}</Text>
              <Text style={styles.claimAmount}>{claim.amount}</Text>
            </View>
            
            <Text style={styles.claimProvider}>{claim.provider}</Text>
            <Text style={styles.claimDescription}>{claim.description}</Text>
            
            <View style={styles.claimFooter}>
              <Text style={styles.claimDate}>Service: {claim.serviceDate}</Text>
              <Text style={styles.claimDate}>Submitted: {claim.submittedDate}</Text>
            </View>
          </TouchableOpacity>
        ))}
        
        {claims.length === 0 && (
          <View style={styles.emptyState}>
            <Text style={styles.emptyStateText}>No claims found</Text>
            <Text style={styles.emptyStateSubtext}>
              {filter === 'all' 
                ? 'You haven\'t submitted any claims yet'
                : `No ${filter} claims found`
              }
            </Text>
          </View>
        )}
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.backgroundLight,
  },
  header: {
    padding: SPACING.lg,
    paddingBottom: SPACING.md,
  },
  title: {
    fontSize: FONT_SIZES.xxl,
    fontWeight: 'bold',
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  subtitle: {
    fontSize: FONT_SIZES.md,
    color: COLORS.textLight,
  },
  filterContainer: {
    paddingHorizontal: SPACING.lg,
    marginBottom: SPACING.md,
  },
  filterTab: {
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.sm,
    marginRight: SPACING.sm,
    borderRadius: BORDER_RADIUS.lg,
    backgroundColor: COLORS.white,
  },
  filterTabActive: {
    backgroundColor: COLORS.primary,
  },
  filterTabText: {
    fontSize: FONT_SIZES.sm,
    color: COLORS.text,
    fontWeight: '500',
  },
  filterTabTextActive: {
    color: COLORS.white,
    fontWeight: '600',
  },
  claimsList: {
    flex: 1,
    paddingHorizontal: SPACING.lg,
  },
  claimCard: {
    backgroundColor: COLORS.white,
    padding: SPACING.md,
    borderRadius: BORDER_RADIUS.lg,
    marginBottom: SPACING.md,
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
    fontWeight: 'bold',
    color: COLORS.text,
  },
  claimStatus: {
    fontSize: FONT_SIZES.sm,
    fontWeight: '600',
  },
  claimDetails: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.xs,
  },
  claimType: {
    fontSize: FONT_SIZES.sm,
    color: COLORS.textLight,
  },
  claimAmount: {
    fontSize: FONT_SIZES.md,
    fontWeight: 'bold',
    color: COLORS.primary,
  },
  claimProvider: {
    fontSize: FONT_SIZES.sm,
    color: COLORS.text,
    fontWeight: '500',
    marginBottom: SPACING.xs,
  },
  claimDescription: {
    fontSize: FONT_SIZES.sm,
    color: COLORS.textLight,
    marginBottom: SPACING.sm,
  },
  claimFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  claimDate: {
    fontSize: FONT_SIZES.xs,
    color: COLORS.textLight,
  },
  emptyState: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: SPACING.xxl,
  },
  emptyStateText: {
    fontSize: FONT_SIZES.lg,
    fontWeight: '600',
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  emptyStateSubtext: {
    fontSize: FONT_SIZES.md,
    color: COLORS.textLight,
    textAlign: 'center',
  },
});

export default MyClaimsScreen;
