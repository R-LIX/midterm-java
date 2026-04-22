import model.Password;

import java.security.MessageDigest;
import java.math.BigInteger;



public class passwordHash {

    static void main() throws Exception {
        Password pHash = new Password("WIllixet","1q2ase36fh6898");
        String passwordHash = pHash.getPassword();
        System.out.println(getMD5(passwordHash));
        System.out.println(PasswordH());
    }

    public static String getMD5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        // Convert to hex string
        return new BigInteger(1, messageDigest).toString(16);
    }

    public static String PasswordH() throws Exception {
        System.out.println("Enter password");
        String newPassword = "1q2ase36fh6898";
        System.out.println(getMD5(newPassword));

        return getMD5(newPassword);
    }

}

