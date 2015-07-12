package webservices;

/**
 * Created by NESTOR on 12/07/2015.
 */
public class Routes {
    private static final String address = "http://52.24.4.177:8383";
    public static final String wsLogin = address + "/user/login";
    public static final String wsRegister = address + "/user/register";
    public static final String wsSyncTurns = address + "/sync/turns";
    public static final String wsTurnsUpdate = address + "/sync/turns/update";
    public static final String wsChangesUpdate = address + "/sync/changes/update";
    public static final String wsDubbingsUpdate = address + "/sync/dubbings/update";
}
