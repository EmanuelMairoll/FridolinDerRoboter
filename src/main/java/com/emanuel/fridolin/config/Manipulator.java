package com.emanuel.fridolin.config;

import com.emanuel.fridolin.exception.InvalidUserInputException;
import com.emanuel.fridolin.exception.NoSuchPropertyException;

public interface Manipulator {

    String getProperty(String forName) throws NoSuchPropertyException;
    String setProperty(String name, String value) throws InvalidUserInputException;
    String[][] propertiesByCategory();
}
