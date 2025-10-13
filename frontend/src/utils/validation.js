import * as Yup from 'yup';

// Email validation
export const emailValidation = Yup.string()
  .email('Invalid email address')
  .required('Email is required');

// Password validation
export const passwordValidation = Yup.string()
  .min(8, 'Password must be at least 8 characters')
  .matches(
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])/,
    'Password must contain uppercase, lowercase, number and special character'
  )
  .required('Password is required');

// Phone validation
export const phoneValidation = Yup.string()
  .matches(/^[0-9]{10}$/, 'Phone number must be 10 digits')
  .required('Phone number is required');

// Name validation
export const nameValidation = Yup.string()
  .min(2, 'Name must be at least 2 characters')
  .matches(/^[a-zA-Z\s]+$/, 'Name can only contain letters')
  .required('Name is required');

// Login schema
export const loginSchema = Yup.object().shape({
  email: emailValidation,
  password: Yup.string().required('Password is required'),
});

// Signup schema
export const signupSchema = Yup.object().shape({
  firstName: nameValidation,
  lastName: nameValidation,
  email: emailValidation,
  phone: phoneValidation,
  password: passwordValidation,
  confirmPassword: Yup.string()
    .oneOf([Yup.ref('password'), null], 'Passwords must match')
    .required('Confirm password is required'),
  role: Yup.string()
    .oneOf(['patient', 'doctor', 'admin', 'insurance_provider'])
    .required('Please select a role'),
  // Conditional validations
  medicalLicenseNumber: Yup.string().when('role', {
    is: 'doctor',
    then: (schema) => schema.required('Medical license number is required for doctors'),
    otherwise: (schema) => schema.notRequired(),
  }),
  specialty: Yup.string().when('role', {
    is: 'doctor',
    then: (schema) => schema.required('Specialty is required for doctors'),
    otherwise: (schema) => schema.notRequired(),
  }),
  insurancePolicyNumber: Yup.string().when('role', {
    is: 'patient',
    then: (schema) => schema.required('Insurance policy number is required for patients'),
    otherwise: (schema) => schema.notRequired(),
  }),
});

