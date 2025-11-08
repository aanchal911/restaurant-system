public class AdminAuth {
    private static boolean isAdminLoggedIn = false;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "aanchal123";
    
    public static boolean login(String username, String password) {
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            isAdminLoggedIn = true;
            return true;
        }
        return false;
    }
    
    public static void logout() {
        isAdminLoggedIn = false;
    }
    
    public static boolean isLoggedIn() {
        return isAdminLoggedIn;
    }
}