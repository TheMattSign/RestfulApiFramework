package com.themattsign.apiframework.models;


import com.themattsign.apiframework.annotations.*;
import com.themattsign.apiframework.errors.InvalidModelException;
import org.apache.commons.lang3.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbstractModel {

    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected void additionalValidation() throws InvalidModelException {
        // This is a stub to override if additional validation is needed
    }

    public void scrubAndValidate(Required.RequiredActionEnum requiredActionEnum) throws InvalidModelException {
        Field[] allFields = this.getClass().getDeclaredFields();

        scrub(allFields);
        validate(allFields, requiredActionEnum);
    }

    /**
     * Trims all of the white space from the front and back of a string input.  We can also add other scrubbing here if needs be.
     * @param allFields - The fields to loop through and scrub
     */
    private void scrub(Field[] allFields) {
        for (Field field : allFields) {
            try {
                // See if we have a getter and try to call it, if either of these fail it's ok to move onto the next since we can't get the value
                Method getter = new PropertyDescriptor(field.getName(), this.getClass()).getReadMethod();
                Method setter = new PropertyDescriptor(field.getName(), this.getClass()).getWriteMethod();
                Object obj = getter.invoke(this);

                if (obj != null) {
                    if (obj instanceof String) {
                        String value = (String) obj;
                        value = value.trim();

                        setter.invoke(this, value);
                    }
                }
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException ie) {
                // This is fine, just means we can't check this field.
            }
        }
    }

    /**
     * Loops through the fields sent in and validates each one based on the annotations on the field
     * @param allFields - The fields to loop through and validate
     * @param requiredActionEnum - The action we are performing this validation for
     * @throws InvalidModelException - Thrown if one of the validations isn't met.
     */
    private void validate(Field[] allFields, Required.RequiredActionEnum requiredActionEnum) throws InvalidModelException {
        for (Field field : allFields) {
            try {
                // See if we have a getter and try to call it, if either of these fail it's ok to move onto the next since we can't get the value
                Method getter = new PropertyDescriptor(field.getName(), this.getClass()).getReadMethod();
                Object obj = getter.invoke(this);

                if (field.isAnnotationPresent(Required.class)) {
                    Required requiredAnnotation = field.getAnnotation(Required.class);
                    Required.RequiredActionEnum fieldRequiredValue = requiredAnnotation.value();

                    if (fieldRequiredValue == Required.RequiredActionEnum.BOTH || requiredActionEnum == fieldRequiredValue) {
                        if (obj == null) {
                            throw new InvalidModelException("The field " + field.getName() + " is required and a value has not been provided");
                        } else if (obj instanceof String) {
                            if (StringUtils.isEmpty((String) obj)) {
                                throw new InvalidModelException("The field " + field.getName() + " is required and a value has not been provided");
                            }
                        }
                    }
                }

                if (obj != null) {
                    if (field.isAnnotationPresent(Pattern.class)) {
                        if (obj instanceof String) {
                            String value = (String) obj;

                            if (StringUtils.isNotEmpty(value)) {
                                Pattern pattern = field.getAnnotation(Pattern.class);
                                String regex = pattern.value();

                                if (!value.matches(regex)) {
                                    throw new InvalidModelException("The field " + field.getName() + " doesn't match the pattern " + regex);
                                }
                            }
                        }
                    }

                    // Check that all of the length fields are greater than or equal to their lengths
                    if (field.isAnnotationPresent(Length.class)) {
                        if (obj instanceof String) {
                            Length lengthAnnotation = field.getAnnotation(Length.class);
                            int length = lengthAnnotation.value();
                            String value = (String) obj;

                            if (value.length() < length) {
                                throw new InvalidModelException("The field " + field.getName() + " isn't long enough.  It must have " + length + " characters.  It has: " + value.length() + " characters");
                            }
                        }
                    }

                    // Verify the max length fields
                    if (field.isAnnotationPresent(MaxLength.class)) {
                        if (obj instanceof String) {
                            MaxLength maxLengthAnnotation = field.getAnnotation(MaxLength.class);
                            int maxLength = maxLengthAnnotation.value();
                            String value = (String) obj;

                            if (value.length() > maxLength) {
                                throw new InvalidModelException("The field " + field.getName() + " is too long.  It can only be " + maxLength + " characters long.  It is: " + value.length());
                            }
                        }
                    }

                    // Check to see if the accepted values fields have values that are expected
                    if (field.isAnnotationPresent(AcceptedValues.class)) {
                        if (obj instanceof String) {

                            AcceptedValues acceptedValuesAnnotation = field.getAnnotation(AcceptedValues.class);

                            Class<?> enumClass = acceptedValuesAnnotation.enumClass();
                            String[] acceptedValues = acceptedValuesAnnotation.value();

                            String value = (String) obj;

                            if (StringUtils.isNotEmpty(value)) {
                                if (acceptedValues.length > 0) {
                                    List<String> acceptedValuesList = Arrays.asList(acceptedValues);

                                    if (!acceptedValuesList.contains(value)) {
                                        throw new InvalidModelException("The value provided for field " + field.getName() + " '" + value + "' isn't an accepted value for that field.  Accepted values are: " + acceptedValuesList.toString());
                                    }
                                } else {
                                    // If we designated an enum as the list of expected values verify that enum exists and check our value against the list of constants
                                    if (enumClass.isEnum()) {
                                        Field[] fields = enumClass.getDeclaredFields();
                                        List<String> enumConstants = new ArrayList<>();  // enum constants

                                        for (Field f : fields) {
                                            if (f.isEnumConstant()) {
                                                enumConstants.add(f.getName());
                                            }
                                        }

                                        if (!enumConstants.contains(value)) {
                                            throw new InvalidModelException("The value provided for field " + field.getName() + " '" + value + "' isn't an accepted value for that field.  Accepted values are: " + enumConstants.toString());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                additionalValidation();
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException ie) {
                // This is fine, just means we can't check this field.
            }
        }
    }
}
