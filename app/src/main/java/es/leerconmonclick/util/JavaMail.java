package es.leerconmonclick.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.leeconmonclick.HelpActivity;
import com.example.leeconmonclick.HomeProfesionalActivity;
import com.example.leeconmonclick.ProfilesActivity;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMail extends AsyncTask<Void,Void,Void> {

    private Context mContext;
    private Session mSession;

    private String mEmail;
    private String mSubject;
    private String mMessage;
    private String pass;
    private String userPatient;

    private ProgressDialog mProgressDialog;



    public JavaMail(Context mContext, String mEmail, String mSubject, String mMessage,String userPatient, String pass) {
        this.mContext = mContext;
        this.mEmail = mEmail;
        this.mSubject = mSubject;
        this.mMessage = mMessage;
        this.userPatient = userPatient;
        this.pass = pass;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(mContext,"Creando Usuario", "Por favor espere...",false,false);
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        mProgressDialog.dismiss();


        Toast.makeText(mContext,"Usuario Paciente Creado",Toast.LENGTH_SHORT).show();
        Intent goHomeProfesional = new Intent(mContext, HomeProfesionalActivity.class);
        mContext.startActivity(goHomeProfesional);
    }



    @Override
    protected Void doInBackground(Void... voids) {

        Properties props = new Properties();


        props.put("mail.smtp.host", "smtp.googlemail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        mSession = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(MailUtils.EMAIL, MailUtils.PASSWORD);
                    }
                });

        String messageHTML = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<body>\n"
                +"<p>\n"
                +"Se ha creado nuevo usuario de paciente: <b>" + userPatient + "</b>\n"
                +"</p> \n"
                +"<p>\n"
                +"Contraseña del paciente: <b>" + pass + "</b>\n"
                +"</p> \n"
                + "</body>\n"
                + "</html>";

        try {

            MimeMessage message = new MimeMessage(mSession);

            message.setFrom(new InternetAddress(MailUtils.EMAIL));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mEmail));

            message.setSubject(mSubject);

            message.setContent(messageHTML,"text/html; charset=utf-8");

            Transport.send(message);


        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
