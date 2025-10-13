import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  KeyboardAvoidingView,
  Platform,
  Alert,
  TouchableOpacity,
} from 'react-native';
import { Formik } from 'formik';
import Input from '../components/Input';
import Button from '../components/Button';
import RoleCard from '../components/RoleCard';
import { signupSchema } from '../utils/validation';
import { useAuth } from '../contexts/AuthContext';
import { COLORS, SPACING, FONT_SIZES, USER_ROLES } from '../utils/constants';

const SignupScreen = ({ navigation }) => {
  const [loading, setLoading] = useState(false);
  const [currentStep, setCurrentStep] = useState(1);
  const { signup } = useAuth();

  const handleSignup = async (values) => {
    setLoading(true);
    try {
      const response = await signup(values);
      // Navigation will happen automatically via RootNavigator
      // No need to manually navigate - AuthContext will update isAuthenticated
    } catch (error) {
      Alert.alert('Signup Failed', error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
    >
      <ScrollView
        contentContainerStyle={styles.scrollContainer}
        showsVerticalScrollIndicator={false}
      >
        {/* Header */}
        <View style={styles.header}>
          <Text style={styles.title}>Create Account</Text>
          <Text style={styles.subtitle}>
            Step {currentStep} of 2
          </Text>
        </View>

        {/* Progress Indicator */}
        <View style={styles.progressContainer}>
          <View style={[styles.progressBar, currentStep >= 1 && styles.progressBarActive]} />
          <View style={[styles.progressBar, currentStep >= 2 && styles.progressBarActive]} />
        </View>

        {/* Form */}
        <Formik
          initialValues={{
            firstName: '',
            lastName: '',
            email: '',
            phone: '',
            password: '',
            confirmPassword: '',
            role: '',
            medicalLicenseNumber: '',
            specialty: '',
            insurancePolicyNumber: '',
          }}
          validationSchema={signupSchema}
          onSubmit={handleSignup}
        >
          {({
            handleChange,
            handleBlur,
            handleSubmit,
            setFieldValue,
            values,
            errors,
            touched,
          }) => (
            <View style={styles.form}>
              {currentStep === 1 ? (
                <>
                  {/* Step 1: Basic Information */}
                  <Text style={styles.sectionTitle}>Basic Information</Text>

                  <Input
                    label="First Name"
                    placeholder="Enter your first name"
                    value={values.firstName}
                    onChangeText={handleChange('firstName')}
                    onBlur={handleBlur('firstName')}
                    error={errors.firstName}
                    touched={touched.firstName}
                  />

                  <Input
                    label="Last Name"
                    placeholder="Enter your last name"
                    value={values.lastName}
                    onChangeText={handleChange('lastName')}
                    onBlur={handleBlur('lastName')}
                    error={errors.lastName}
                    touched={touched.lastName}
                  />

                  <Input
                    label="Email"
                    placeholder="Enter your email"
                    value={values.email}
                    onChangeText={handleChange('email')}
                    onBlur={handleBlur('email')}
                    error={errors.email}
                    touched={touched.email}
                    keyboardType="email-address"
                    autoCapitalize="none"
                  />

                  <Input
                    label="Phone Number"
                    placeholder="10 digit phone number"
                    value={values.phone}
                    onChangeText={handleChange('phone')}
                    onBlur={handleBlur('phone')}
                    error={errors.phone}
                    touched={touched.phone}
                    keyboardType="phone-pad"
                  />

                  <Input
                    label="Password"
                    placeholder="Create a strong password"
                    value={values.password}
                    onChangeText={handleChange('password')}
                    onBlur={handleBlur('password')}
                    error={errors.password}
                    touched={touched.password}
                    secureTextEntry
                  />

                  <Input
                    label="Confirm Password"
                    placeholder="Re-enter your password"
                    value={values.confirmPassword}
                    onChangeText={handleChange('confirmPassword')}
                    onBlur={handleBlur('confirmPassword')}
                    error={errors.confirmPassword}
                    touched={touched.confirmPassword}
                    secureTextEntry
                  />

                  <Button
                    title="Next"
                    onPress={() => {
                      // Validate step 1 fields
                      const step1Errors = ['firstName', 'lastName', 'email', 'phone', 'password', 'confirmPassword']
                        .filter(field => errors[field]);
                      
                      if (step1Errors.length > 0) {
                        Alert.alert('Error', 'Please fill all fields correctly before proceeding');
                        return;
                      }
                      setCurrentStep(2);
                    }}
                    style={styles.nextButton}
                  />
                </>
              ) : (
                <>
                  {/* Step 2: Role Selection and Additional Info */}
                  <Text style={styles.sectionTitle}>Select Your Role</Text>

                  {USER_ROLES.map((role) => (
                    <RoleCard
                      key={role.id}
                      role={role}
                      isSelected={values.role === role.id}
                      onPress={() => setFieldValue('role', role.id)}
                    />
                  ))}
                  {errors.role && touched.role && (
                    <Text style={styles.errorText}>{errors.role}</Text>
                  )}

                  {/* Conditional Fields Based on Role */}
                  {values.role === 'doctor' && (
                    <>
                      <Text style={styles.sectionTitle}>Doctor Information</Text>
                      <Input
                        label="Medical License Number"
                        placeholder="Enter your license number"
                        value={values.medicalLicenseNumber}
                        onChangeText={handleChange('medicalLicenseNumber')}
                        onBlur={handleBlur('medicalLicenseNumber')}
                        error={errors.medicalLicenseNumber}
                        touched={touched.medicalLicenseNumber}
                      />
                      <Input
                        label="Specialty"
                        placeholder="e.g., Cardiology, Pediatrics"
                        value={values.specialty}
                        onChangeText={handleChange('specialty')}
                        onBlur={handleBlur('specialty')}
                        error={errors.specialty}
                        touched={touched.specialty}
                      />
                    </>
                  )}

                  {values.role === 'patient' && (
                    <>
                      <Text style={styles.sectionTitle}>Insurance Information</Text>
                      <Input
                        label="Insurance Policy Number"
                        placeholder="Enter your policy number"
                        value={values.insurancePolicyNumber}
                        onChangeText={handleChange('insurancePolicyNumber')}
                        onBlur={handleBlur('insurancePolicyNumber')}
                        error={errors.insurancePolicyNumber}
                        touched={touched.insurancePolicyNumber}
                      />
                    </>
                  )}

                  <View style={styles.buttonGroup}>
                    <Button
                      title="Back"
                      onPress={() => setCurrentStep(1)}
                      variant="outline"
                      style={styles.backButton}
                    />
                    <Button
                      title="Sign Up"
                      onPress={handleSubmit}
                      loading={loading}
                      style={styles.signupButton}
                    />
                  </View>
                </>
              )}
            </View>
          )}
        </Formik>

        {/* Footer */}
        <View style={styles.footer}>
          <Text style={styles.footerText}>Already have an account? </Text>
          <TouchableOpacity onPress={() => navigation.navigate('Login')}>
            <Text style={styles.loginLink}>Login</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  scrollContainer: {
    flexGrow: 1,
    padding: SPACING.lg,
  },
  header: {
    marginTop: SPACING.xl,
    marginBottom: SPACING.lg,
    alignItems: 'center',
  },
  title: {
    fontSize: FONT_SIZES.xxl,
    fontWeight: 'bold',
    color: COLORS.primary,
    marginBottom: SPACING.xs,
  },
  subtitle: {
    fontSize: FONT_SIZES.md,
    color: COLORS.textLight,
  },
  progressContainer: {
    flexDirection: 'row',
    marginBottom: SPACING.lg,
    gap: SPACING.sm,
  },
  progressBar: {
    flex: 1,
    height: 4,
    backgroundColor: COLORS.border,
    borderRadius: 2,
  },
  progressBarActive: {
    backgroundColor: COLORS.primary,
  },
  form: {
    marginBottom: SPACING.lg,
  },
  sectionTitle: {
    fontSize: FONT_SIZES.lg,
    fontWeight: '600',
    color: COLORS.text,
    marginTop: SPACING.md,
    marginBottom: SPACING.md,
  },
  nextButton: {
    marginTop: SPACING.lg,
  },
  buttonGroup: {
    flexDirection: 'row',
    gap: SPACING.sm,
    marginTop: SPACING.lg,
  },
  backButton: {
    flex: 1,
  },
  signupButton: {
    flex: 2,
  },
  footer: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: SPACING.lg,
    marginBottom: SPACING.xl,
  },
  footerText: {
    fontSize: FONT_SIZES.md,
    color: COLORS.textLight,
  },
  loginLink: {
    fontSize: FONT_SIZES.md,
    color: COLORS.primary,
    fontWeight: '600',
  },
  errorText: {
    fontSize: FONT_SIZES.xs,
    color: COLORS.error,
    marginTop: SPACING.xs,
    marginBottom: SPACING.sm,
  },
});

export default SignupScreen;

