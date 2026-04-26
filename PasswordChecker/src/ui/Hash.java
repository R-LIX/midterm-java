package ui;


public class Hash {
    public static String hashPassword(String password) {
        try {
            return model.Password.getMD5(password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
