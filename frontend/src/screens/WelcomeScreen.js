import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  Image,
  SafeAreaView,
} from 'react-native';
import Button from '../components/Button';
import { COLORS, SPACING, FONT_SIZES } from '../utils/constants';

const WelcomeScreen = ({ navigation }) => {
  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.content}>
        {/* Logo/Icon Section */}
        <View style={styles.logoContainer}>
          <View style={styles.logoCircle}>
            <Text style={styles.logoIcon}>🏥</Text>
          </View>
          <Text style={styles.appName}>HealthCare</Text>
          <Text style={styles.appTagline}>Insurance Claim Manager</Text>
        </View>

        {/* Feature Highlights */}
        <View style={styles.featuresContainer}>
          <FeatureItem icon="✅" text="Easy Insurance Claims" />
          <FeatureItem icon="👨‍⚕️" text="Connect with Doctors" />
          <FeatureItem icon="📱" text="Track Claim Status" />
          <FeatureItem icon="🔒" text="Secure & Private" />
        </View>

        {/* Buttons */}
        <View style={styles.buttonContainer}>
          <Button
            title="Login"
            onPress={() => navigation.navigate('Login')}
            style={styles.loginButton}
            textStyle={styles.loginButtonText}
          />
          <Button
            title="Create Account"
            onPress={() => navigation.navigate('Signup')}
            variant="outline"
            style={styles.signupButton}
          />
        </View>

        {/* Footer */}
        <Text style={styles.footer}>
          Your trusted healthcare insurance companion
        </Text>
      </View>
    </SafeAreaView>
  );
};

const FeatureItem = ({ icon, text }) => (
  <View style={styles.featureItem}>
    <Text style={styles.featureIcon}>{icon}</Text>
    <Text style={styles.featureText}>{text}</Text>
  </View>
);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.primary,
  },
  content: {
    flex: 1,
    padding: SPACING.lg,
    justifyContent: 'space-between',
  },
  logoContainer: {
    alignItems: 'center',
    marginTop: SPACING.xxl * 2,
  },
  logoCircle: {
    width: 120,
    height: 120,
    borderRadius: 60,
    backgroundColor: COLORS.white,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: SPACING.lg,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 4,
    },
    shadowOpacity: 0.3,
    shadowRadius: 4.65,
    elevation: 8,
  },
  logoIcon: {
    fontSize: 60,
  },
  appName: {
    fontSize: FONT_SIZES.xxl + 8,
    fontWeight: 'bold',
    color: COLORS.white,
    marginBottom: SPACING.xs,
  },
  appTagline: {
    fontSize: FONT_SIZES.md,
    color: COLORS.white,
    opacity: 0.9,
  },
  featuresContainer: {
    backgroundColor: 'rgba(255, 255, 255, 0.15)',
    borderRadius: 16,
    padding: SPACING.lg,
    marginVertical: SPACING.xl,
  },
  featureItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  featureIcon: {
    fontSize: 24,
    marginRight: SPACING.sm,
  },
  featureText: {
    fontSize: FONT_SIZES.md,
    color: COLORS.white,
    fontWeight: '500',
  },
  buttonContainer: {
    gap: SPACING.sm,
  },
  loginButton: {
    backgroundColor: COLORS.white,
  },
  loginButtonText: {
    color: COLORS.primary,
  },
  signupButton: {
    borderColor: COLORS.white,
  },
  footer: {
    textAlign: 'center',
    fontSize: FONT_SIZES.sm,
    color: COLORS.white,
    opacity: 0.8,
    marginTop: SPACING.lg,
  },
});

export default WelcomeScreen;

