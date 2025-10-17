import React from 'react';
import { createStackNavigator } from '@react-navigation/stack';
import { useAuth } from '../contexts/AuthContext';
import { View, ActivityIndicator, StyleSheet } from 'react-native';
import { COLORS } from '../utils/constants';

// Import role-based dashboards
import PatientDashboard from '../screens/Dashboard/PatientDashboard';
import DoctorDashboard from '../screens/Dashboard/DoctorDashboard';
import AdminDashboard from '../screens/Dashboard/AdminDashboard';
import InsuranceProviderDashboard from '../screens/Dashboard/InsuranceProviderDashboard';

// Import additional screens
import FileClaimScreen from '../screens/Claims/FileClaimScreen';
import MyClaimsScreen from '../screens/Claims/MyClaimsScreen';

const Stack = createStackNavigator();

/**
 * Role-based Dashboard Component
 */
const RoleBasedDashboard = ({ navigation }) => {
  const { user } = useAuth();
  
  // Get the primary role from user roles array
  const userRole = user?.roles?.[0] || user?.role || 'patient';
  
  // Return appropriate dashboard based on user role
  switch (userRole.toLowerCase()) {
    case 'patient':
      return <PatientDashboard navigation={navigation} />;
    case 'doctor':
      return <DoctorDashboard navigation={navigation} />;
    case 'admin':
      return <AdminDashboard navigation={navigation} />;
    case 'insurance_provider':
      return <InsuranceProviderDashboard navigation={navigation} />;
    default:
      return <PatientDashboard navigation={navigation} />;
  }
};

/**
 * Main App Navigator - shows dashboard based on user role
 */
const AppNavigator = () => {
  const { user, loading } = useAuth();

  // Show loading indicator while checking auth status
  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color={COLORS.primary} />
      </View>
    );
  }

  return (
    <Stack.Navigator
      screenOptions={{
        headerStyle: {
          backgroundColor: COLORS.primary,
        },
        headerTintColor: COLORS.white,
        headerTitleStyle: {
          fontWeight: '600',
        },
      }}
    >
      <Stack.Screen
        name="Dashboard"
        component={RoleBasedDashboard}
        options={{ headerShown: false }}
      />
      
      {/* Claims Screens */}
      <Stack.Screen
        name="FileClaim"
        component={FileClaimScreen}
        options={{ 
          title: 'File New Claim',
          headerStyle: { backgroundColor: COLORS.primary },
          headerTintColor: COLORS.white,
        }}
      />
      
      <Stack.Screen
        name="MyClaims"
        component={MyClaimsScreen}
        options={{ 
          title: 'My Claims',
          headerStyle: { backgroundColor: COLORS.primary },
          headerTintColor: COLORS.white,
        }}
      />
    </Stack.Navigator>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: COLORS.background,
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: COLORS.primary,
    marginBottom: 10,
  },
  subtitle: {
    fontSize: 16,
    color: COLORS.textLight,
    marginBottom: 5,
  },
  name: {
    fontSize: 18,
    fontWeight: '600',
    color: COLORS.text,
    marginBottom: 5,
  },
  email: {
    fontSize: 14,
    color: COLORS.textLight,
    marginBottom: 30,
  },
  buttonContainer: {
    backgroundColor: COLORS.primary,
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 8,
  },
  button: {
    color: COLORS.white,
    fontSize: 16,
    fontWeight: '600',
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: COLORS.background,
  },
});

export default AppNavigator;

