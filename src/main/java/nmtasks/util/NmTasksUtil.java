package nmtasks.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.core.io.Resource;

public class NmTasksUtil {

  // converts a spring.core.io.Resource into a java String
  public static final String resourceToString(Resource resource) throws IOException {
    InputStream istream = resource.getInputStream();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int length;
    while ((length = istream.read(buffer)) != -1) {
      baos.write(buffer,0,length);
    }
    return baos.toString("UTF-8");
  }

  // returns a Base64 encoded SHA-512 password hash for storing and comparing against database values
  public static final String getSHA512Hash(String password, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest md = MessageDigest.getInstance("SHA-512");
    md.update(salt.getBytes("UTF-8"));
    byte[] bytes = md.digest(password.getBytes("UTF-8"));
    return Base64.getEncoder().encodeToString(bytes);
  }
  public static final String generateSalt() {
    return new BigInteger(130, new SecureRandom()).toString(32);
  }
}