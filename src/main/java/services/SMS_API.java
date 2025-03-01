package services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SMS_API {
    public static final String account_sid = System.getenv("twilio_account_sid");
    public static final String token = System.getenv("twilio_token");

    static {
        Twilio.init(account_sid,token);
    }

    public static void sendSMS(){

        Message message = Message.creator(new PhoneNumber("+21640660220"),
                new PhoneNumber("+12563718967"),"vous aves un rendez-vous").create();
        System.out.println(message.getSid());
    }
}

