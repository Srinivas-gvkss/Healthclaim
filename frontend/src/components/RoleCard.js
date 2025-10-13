import React from 'react';
import { TouchableOpacity, Text, StyleSheet, View } from 'react-native';
import { COLORS, SPACING, FONT_SIZES, BORDER_RADIUS } from '../utils/constants';

const RoleCard = ({ role, isSelected, onPress }) => {
  return (
    <TouchableOpacity
      style={[
        styles.card,
        isSelected && styles.cardSelected,
      ]}
      onPress={onPress}
      activeOpacity={0.7}
    >
      <View style={styles.radioButton}>
        {isSelected && <View style={styles.radioButtonInner} />}
      </View>
      <View style={styles.content}>
        <Text style={[styles.title, isSelected && styles.titleSelected]}>
          {role.label}
        </Text>
        <Text style={[styles.description, isSelected && styles.descriptionSelected]}>
          {role.description}
        </Text>
      </View>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  card: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: COLORS.white,
    borderWidth: 2,
    borderColor: COLORS.border,
    borderRadius: BORDER_RADIUS.lg,
    padding: SPACING.md,
    marginBottom: SPACING.sm,
  },
  cardSelected: {
    borderColor: COLORS.primary,
    backgroundColor: '#E3F2FD',
  },
  radioButton: {
    width: 24,
    height: 24,
    borderRadius: 12,
    borderWidth: 2,
    borderColor: COLORS.border,
    marginRight: SPACING.sm,
    alignItems: 'center',
    justifyContent: 'center',
  },
  radioButtonInner: {
    width: 14,
    height: 14,
    borderRadius: 7,
    backgroundColor: COLORS.primary,
  },
  content: {
    flex: 1,
  },
  title: {
    fontSize: FONT_SIZES.md,
    fontWeight: '600',
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  titleSelected: {
    color: COLORS.primary,
  },
  description: {
    fontSize: FONT_SIZES.sm,
    color: COLORS.textLight,
  },
  descriptionSelected: {
    color: COLORS.text,
  },
});

export default RoleCard;

