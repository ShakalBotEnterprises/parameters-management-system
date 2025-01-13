package mavmi.parameters_management_system.common.utils;

import lombok.experimental.UtilityClass;
import mavmi.parameters_management_system.common.parameter.api.PARAMETER_TYPE;

@UtilityClass
public class Utils {

    public static boolean verifyProperty(String propertyValue, PARAMETER_TYPE parameterType) {
        if (parameterType == PARAMETER_TYPE.STRING) {
            return true;
        } else if (parameterType == PARAMETER_TYPE.LONG) {
            try {
                Long.parseLong(propertyValue);
                return true;
            } catch (Exception ignored) {
                return false;
            }
        } else if (parameterType == PARAMETER_TYPE.BOOLEAN) {
            try {
                Boolean.parseBoolean(propertyValue);
                return true;
            } catch (Exception ignored) {
                return false;
            }
        } else {
            return false;
        }
    }
}
