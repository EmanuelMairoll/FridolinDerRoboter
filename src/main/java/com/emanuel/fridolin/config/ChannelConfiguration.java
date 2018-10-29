package com.emanuel.fridolin.config;

import com.emanuel.fridolin.exception.InvalidUserInputException;
import com.emanuel.fridolin.exception.NoSuchPropertyException;

public class ChannelConfiguration implements Manipulable {

    private boolean imageOnly = false;
    private boolean selfDestruct = true;
    private long messageMaxAge = -1;

    public boolean imageOnly() {
        return imageOnly;
    }

    public boolean selfDestruct() {
        return selfDestruct;
    }

    public long messageMaxAge() {
        return messageMaxAge;
    }

    @Override
    public Manipulator manipulator() {
        return new Manipulator() {
            private static final String IMAGE_ONLY_STRING = "imageOnly";
            private static final String SELF_DESTRUCT_STRING = "selfDestruct";
            private static final String MESSAGE_MAX_AGE_STRING = "messageMaxAge";

            @Override
            public String getProperty(String forName) throws NoSuchPropertyException {
                switch (forName) {
                    case IMAGE_ONLY_STRING:
                        return String.valueOf(imageOnly);
                    case SELF_DESTRUCT_STRING:
                        return String.valueOf(selfDestruct);
                    case MESSAGE_MAX_AGE_STRING:
                        return String.valueOf(messageMaxAge);
                    default:
                        throw new NoSuchPropertyException(forName);
                }
            }

            @Override
            public String setProperty(String name, String value) throws InvalidUserInputException {
                boolean equalsTrue;
                boolean equalsFalse;
                long longValue;

                switch (name) {
                    case IMAGE_ONLY_STRING:
                        equalsTrue = value.equalsIgnoreCase("true");
                        equalsFalse = value.equalsIgnoreCase("false");
                        if (equalsTrue || equalsFalse) {
                            imageOnly = equalsTrue;
                            return String.valueOf(imageOnly);
                        } else {
                            throw new InvalidUserInputException("Specify ether \"true\" or \"false\"!");
                        }
                    case SELF_DESTRUCT_STRING:
                        equalsTrue = value.equalsIgnoreCase("true");
                        equalsFalse = value.equalsIgnoreCase("false");
                        if (equalsTrue || equalsFalse) {
                            selfDestruct = equalsTrue;
                            return String.valueOf(selfDestruct);
                        } else {
                            throw new InvalidUserInputException("Specify ether \"true\" or \"false\"!");
                        }
                    case MESSAGE_MAX_AGE_STRING:
                        try{
                             longValue = Long.parseLong(value);
                            messageMaxAge = longValue;
                             return String.valueOf(messageMaxAge);
                        }catch(NumberFormatException ex){
                            throw new InvalidUserInputException("\"" + value + "\" is not a number!");
                        }
                    default:
                        throw new NoSuchPropertyException(name);
                }
            }

            @Override
            public String[][] propertiesByCategory() {
                String[] messages = {IMAGE_ONLY_STRING, SELF_DESTRUCT_STRING, MESSAGE_MAX_AGE_STRING};
                return new String[][]{messages};
            }
        };
    }
}
