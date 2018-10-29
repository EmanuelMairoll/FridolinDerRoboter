package com.emanuel.fridolin.config;

import com.emanuel.fridolin.exception.InvalidUserInputException;
import com.emanuel.fridolin.exception.NoSuchPropertyException;
import com.emanuel.fridolin.util.ColorNames;

import java.awt.*;

public class ServerConfiguration implements Manipulable {

    private String prefix = "/";

    private String adminRoleName = "Administrator";
    private String moderatorRoleName = "Klassensprecher";
    private String trustedRoleName = "Alle";

    private String pollDefaultYesEmote = "Yay";
    private String pollDefaultNoEmote = "Nay";

    private Color showColor = Color.CYAN;
    private Color infoColor = Color.GREEN;
    private Color warnColor = Color.YELLOW;
    private Color errorColor = Color.RED;

    private boolean adminsBypassImageOnly = true;

    public String prefix() {
        return prefix;
    }

    public String adminRoleName() {
        return adminRoleName;
    }

    public String moderatorRoleName() {
        return moderatorRoleName;
    }

    public String trustedRoleName() {
        return trustedRoleName;
    }

    public String pollDefaultYesEmote() {
        return pollDefaultYesEmote;
    }

    public String pollDefaultNoEmote() {
        return pollDefaultNoEmote;
    }

    public Color showColor() {
        return showColor;
    }

    public Color infoColor() {
        return infoColor;
    }

    public Color warnColor() {
        return warnColor;
    }

    public Color errorColor() {
        return errorColor;
    }

    public boolean canAdminsBypassImageOnly() {
        return adminsBypassImageOnly;
    }

    @Override
    public Manipulator manipulator() {
        return new Manipulator() {
            private static final String PREFIX_STRING = "prefix";
            private static final String ADMIN_ROLE_NAME_STRING = "adminRoleName";
            private static final String MODERATOR_ROLE_NAME_STRING = "moderatorRoleName";
            private static final String TRUSTED_ROLE_NAME_STRING = "trustedRoleName";
            private static final String POLL_DEFAULT_YES_EMOTE_STRING = "pollDefaultYesEmote";
            private static final String POLL_DEFAULT_NO_EMOTE_STRING = "pollDefaultNoEmote";
            private static final String SHOW_COLOR_STRING = "showColor";
            private static final String INFO_COLOR_STRING = "infoColor";
            private static final String WARN_COLOR_STRING = "warnColor";
            private static final String ERROR_COLOR_STRING = "errorColor";
            private static final String ADMINS_BYPASS_IMAGE_ONLY_STRING = "adminsBypassImageOnly";

            @Override
            public String getProperty(String forName) throws NoSuchPropertyException {
                switch (forName) {
                    case PREFIX_STRING:
                        return prefix;
                    case ADMIN_ROLE_NAME_STRING:
                        return adminRoleName;
                    case MODERATOR_ROLE_NAME_STRING:
                        return moderatorRoleName;
                    case TRUSTED_ROLE_NAME_STRING:
                        return trustedRoleName;
                    case POLL_DEFAULT_YES_EMOTE_STRING:
                        return pollDefaultYesEmote;
                    case POLL_DEFAULT_NO_EMOTE_STRING:
                        return pollDefaultNoEmote;
                    case SHOW_COLOR_STRING:
                        return ColorNames.fromColor(showColor);
                    case INFO_COLOR_STRING:
                        return ColorNames.fromColor(infoColor);
                    case WARN_COLOR_STRING:
                        return ColorNames.fromColor(warnColor);
                    case ERROR_COLOR_STRING:
                        return ColorNames.fromColor(errorColor);
                    case ADMINS_BYPASS_IMAGE_ONLY_STRING:
                        return String.valueOf(adminsBypassImageOnly);
                    default:
                        throw new NoSuchPropertyException(forName);
                }
            }

            @Override
            public String setProperty(String name, String value) throws InvalidUserInputException {
                switch (name) {
                    case PREFIX_STRING:
                        if (value.length() > 1) {
                            throw new InvalidUserInputException("Prefix has to be a single character!");
                        }
                        prefix = value;
                        return prefix;
                    case ADMIN_ROLE_NAME_STRING:
                        adminRoleName = value;
                        return adminRoleName;
                    case MODERATOR_ROLE_NAME_STRING:
                        moderatorRoleName = value;
                        return moderatorRoleName;
                    case TRUSTED_ROLE_NAME_STRING:
                        trustedRoleName = value;
                        return trustedRoleName;
                    case POLL_DEFAULT_YES_EMOTE_STRING:
                        pollDefaultYesEmote = value;
                        return pollDefaultYesEmote;
                    case POLL_DEFAULT_NO_EMOTE_STRING:
                        pollDefaultNoEmote = value;
                        return pollDefaultNoEmote;
                    case SHOW_COLOR_STRING:
                        showColor = ColorNames.fromString(value);
                        return ColorNames.fromColor(showColor);
                    case INFO_COLOR_STRING:
                        infoColor = ColorNames.fromString(value);
                        return ColorNames.fromColor(infoColor);
                    case WARN_COLOR_STRING:
                        warnColor = ColorNames.fromString(value);
                        return ColorNames.fromColor(warnColor);
                    case ERROR_COLOR_STRING:
                        errorColor = ColorNames.fromString(value);
                        return ColorNames.fromColor(errorColor);
                    case ADMINS_BYPASS_IMAGE_ONLY_STRING:
                        boolean equalsTrue = value.equalsIgnoreCase("true");
                        boolean equalsFalse = value.equalsIgnoreCase("false");
                        if (equalsTrue || equalsFalse) {
                            adminsBypassImageOnly = equalsTrue;
                            return String.valueOf(adminsBypassImageOnly);
                        } else {
                            throw new InvalidUserInputException("Specify ether \"true\" or \"false\"!");
                        }
                    default:
                        throw new NoSuchPropertyException(name);
                }
            }


            @Override
            public String[][] propertiesByCategory() {
                String[] general = {PREFIX_STRING, POLL_DEFAULT_YES_EMOTE_STRING, POLL_DEFAULT_NO_EMOTE_STRING};
                String[] roleNames = {ADMIN_ROLE_NAME_STRING, MODERATOR_ROLE_NAME_STRING, TRUSTED_ROLE_NAME_STRING};
                String[] colors = {SHOW_COLOR_STRING, INFO_COLOR_STRING, WARN_COLOR_STRING, ERROR_COLOR_STRING};
                String[] other = {ADMINS_BYPASS_IMAGE_ONLY_STRING};
                return new String[][]{general, roleNames, colors, other};
            }
        };
    }
}
