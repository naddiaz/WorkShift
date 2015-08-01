package webservices;

/**
 * Created by NESTOR on 12/07/2015.
 */
public class Routes {
    private static final String address = "http://52.24.4.177:8383";
    public static final String wsLogin = address + "/user/login";
    public static final String wsRegister = address + "/user/register";

    public static final String wsSyncTurns = address + "/sync/turns";
    public static final String wsActionAddMonth = address + "/actions/add/month";
    public static final String wsActionAddChange = address + "/actions/add/change";
    public static final String wsActionDelChange = address + "/actions/del/change";
    public static final String wsActionAddDubbing = address + "/actions/add/dubbing";
    public static final String wsActionDelDubbing = address + "/actions/del/dubbing";
    public static final String wsActionAddComment = address + "/actions/add/comment";
    public static final String wsActionDelComment = address + "/actions/del/comment";
}
