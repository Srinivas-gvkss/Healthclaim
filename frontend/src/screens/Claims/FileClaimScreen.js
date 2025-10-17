import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  SafeAreaView,
  Alert,
} from 'react-native';
import { useAuth } from '../../contexts/AuthContext';
import Button from '../../components/Button';
import Input from '../../components/Input';
import { COLORS, SPACING, FONT_SIZES, BORDER_RADIUS } from '../../utils/constants';

const FileClaimScreen = ({ navigation }) => {
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    claimType: '',
    providerName: '',
    serviceDate: '',
    amount: '',
    description: '',
    diagnosis: '',
  });
  const [loading, setLoading] = useState(false);

  const claimTypes = [
    { id: 'medical', label: 'Medical Treatment', icon: '🏥' },
    { id: 'dental', label: 'Dental Care', icon: '🦷' },
    { id: 'pharmacy', label: 'Pharmacy', icon: '💊' },
    { id: 'lab', label: 'Laboratory', icon: '🔬' },
    { id: 'imaging', label: 'Imaging/X-Ray', icon: '📷' },
  ];

  const handleInputChange = (field, value) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleSubmit = async () => {
    try {
      setLoading(true);
      
      // Validate form
      if (!formData.claimType || !formData.providerName || !formData.serviceDate || !formData.amount) {
        Alert.alert('Error', 'Please fill in all required fields');
        return;
      }

      // TODO: Implement API call to submit claim
      console.log('Submitting claim:', formData);
      
      Alert.alert(
        'Success', 
        'Your claim has been submitted successfully! You will receive a confirmation email shortly.',
        [
          { text: 'OK', onPress: () => navigation.goBack() }
        ]
      );
    } catch (error) {
      console.error('Error submitting claim:', error);
      Alert.alert('Error', 'Failed to submit claim. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollContainer}>
        {/* Header */}
        <View style={styles.header}>
          <Text style={styles.title}>File New Claim</Text>
          <Text style={styles.subtitle}>Submit your insurance claim for review</Text>
        </View>

        {/* Claim Type Selection */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Claim Type *</Text>
          <View style={styles.claimTypeGrid}>
            {claimTypes.map((type) => (
              <TouchableOpacity
                key={type.id}
                style={[
                  styles.claimTypeCard,
                  formData.claimType === type.id && styles.claimTypeCardSelected
                ]}
                onPress={() => handleInputChange('claimType', type.id)}
              >
                <Text style={styles.claimTypeIcon}>{type.icon}</Text>
                <Text style={[
                  styles.claimTypeLabel,
                  formData.claimType === type.id && styles.claimTypeLabelSelected
                ]}>
                  {type.label}
                </Text>
              </TouchableOpacity>
            ))}
          </View>
        </View>

        {/* Form Fields */}
        <View style={styles.section}>
          <Input
            label="Healthcare Provider Name *"
            value={formData.providerName}
            onChangeText={(value) => handleInputChange('providerName', value)}
            placeholder="Enter provider name"
          />

          <Input
            label="Service Date *"
            value={formData.serviceDate}
            onChangeText={(value) => handleInputChange('serviceDate', value)}
            placeholder="YYYY-MM-DD"
          />

          <Input
            label="Amount *"
            value={formData.amount}
            onChangeText={(value) => handleInputChange('amount', value)}
            placeholder="Enter amount"
            keyboardType="numeric"
          />

          <Input
            label="Description"
            value={formData.description}
            onChangeText={(value) => handleInputChange('description', value)}
            placeholder="Describe the service received"
            multiline
            numberOfLines={3}
          />

          <Input
            label="Diagnosis"
            value={formData.diagnosis}
            onChangeText={(value) => handleInputChange('diagnosis', value)}
            placeholder="Enter diagnosis (if applicable)"
            multiline
            numberOfLines={2}
          />
        </View>

        {/* Submit Button */}
        <Button
          title="Submit Claim"
          onPress={handleSubmit}
          loading={loading}
          style={styles.submitButton}
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
    marginBottom: SPACING.xl,
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
  section: {
    marginBottom: SPACING.xl,
  },
  sectionTitle: {
    fontSize: FONT_SIZES.lg,
    fontWeight: '600',
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  claimTypeGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: SPACING.sm,
  },
  claimTypeCard: {
    width: '48%',
    backgroundColor: COLORS.white,
    padding: SPACING.md,
    borderRadius: BORDER_RADIUS.lg,
    alignItems: 'center',
    borderWidth: 2,
    borderColor: 'transparent',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  claimTypeCardSelected: {
    borderColor: COLORS.primary,
    backgroundColor: COLORS.backgroundLight,
  },
  claimTypeIcon: {
    fontSize: 30,
    marginBottom: SPACING.xs,
  },
  claimTypeLabel: {
    fontSize: FONT_SIZES.sm,
    fontWeight: '500',
    color: COLORS.text,
    textAlign: 'center',
  },
  claimTypeLabelSelected: {
    color: COLORS.primary,
    fontWeight: '600',
  },
  submitButton: {
    marginTop: SPACING.lg,
  },
});

export default FileClaimScreen;
