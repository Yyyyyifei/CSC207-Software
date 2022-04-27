package SendMail;

/**
 * A Controller Class that can call the JavaMailUtil use case class to send an email given a username.
 * @author Group0031
 */
public class EmailSender{
    /**
     * Send email to the given userName to inform the user with his temporary password.
     * @param userName A String representing the given username.
     * @throws Exception Exceptions mentioned in the sendMail method in JavaMailUtil class.
     */
    public void sendMail(String userName) throws Exception{
        JavaMailUtil.SendMail(userName);
    }


}
