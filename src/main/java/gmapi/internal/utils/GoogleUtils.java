package gmapi.internal.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class GoogleUtils {

    public static final String MCLIENTS_URL = "https://mclients.googleapis.com/";
    public static final String MCLIENTS_SJ_URL = GoogleUtils.MCLIENTS_URL + "sj/v2.5";
    public static final String ANDROID_CLIENT_URL = "https://android.clients.google.com/";

    public static final Charset UTF8 = Charset.forName( "UTF-8" );

    private static final String KEY_7_3_29_B64;
    private static final byte[] KEY_7_3_29_SHA1;
    private static final PublicKey KEY_7_3_29;

    private static final String KEY_HMAC_B64;
    private static final SecretKeySpec KEY_HMAC;

    public static String gpsOauthSignature( String email, String pass ) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream( );

            baos.write( 0 );
            baos.write( KEY_7_3_29_SHA1, 0, 4 );
            baos.write( rsaEncrypt( email, pass ) );

            return Base64.encodeBase64URLSafeString( baos.toByteArray( ) );
        }
        catch( IOException ioe ) {
            throw new AssertionError( "IOException while creating signature.", ioe );
        }
    }

    public static String getSongSignature( String songID, String salt ) {
        try {
            Mac hmac = Mac.getInstance( "HmacSHA1" );
            hmac.init( KEY_HMAC );

            hmac.update( songID.getBytes( UTF8 ) );
            hmac.update( salt.getBytes( UTF8 ) );

            return Base64.encodeBase64URLSafeString( hmac.doFinal( ) );
        }
        catch( GeneralSecurityException gse ) {
            throw new AssertionError( "Error creating song signature.", gse );
        }
    }

    private static byte[] rsaEncrypt( String email, String pass ) {
        try {
            byte[] ep = ( email + '\0' + pass ).getBytes( UTF8 );

            Cipher cipher = Cipher.getInstance( "RSA/NONE/OAEPPadding" );
            cipher.init( Cipher.ENCRYPT_MODE, KEY_7_3_29 );

            return cipher.doFinal( ep );
        }
        catch( GeneralSecurityException gse ) {
            throw new AssertionError( "Error encrypting message with RSA.", gse );
        }
    }

    private static byte[] sha1( byte[] key ) {
        try {
            MessageDigest md = MessageDigest.getInstance( "SHA-1" );

            return md.digest( key );
        }
        catch( NoSuchAlgorithmException excep ) {
            throw new AssertionError( "Error initializing Google RSA Key.", excep );
        }
    }

    private static PublicKey decodeKey( byte[] key ) {
        ByteBuffer encodedKey = ByteBuffer.wrap( key ).order( ByteOrder.BIG_ENDIAN );

        byte[] mod = new byte[ encodedKey.getInt( ) ];
        encodedKey.get( mod );
        byte[] exp = new byte[ encodedKey.getInt( ) ];
        encodedKey.get( exp );

        try {
            RSAPublicKeySpec spec = new RSAPublicKeySpec( new BigInteger( 1, mod ), new BigInteger( 1, exp ) );
            return KeyFactory.getInstance( "RSA" ).generatePublic( spec );
        }
        catch( GeneralSecurityException gse ) {
            throw new AssertionError( "Error initializing Google RSA Key.", gse );
        }
    }

    static {
        Security.addProvider( new BouncyCastleProvider( ) );

        KEY_7_3_29_B64 = "AAAAgMom/1a/v0lblO2Ubrt60J2gcuXSljGFQXgcyZWveWLEwo6prwgi3iJIZdodyhKZQrNWp5nKJ3srRXcUW+F1BD3baEVGcmEgqaLZUNBjm057pKRI16kB0YppeGx5qIQ5QjKzsR8ETQbKLNWgRY0QRNVz34kMJR3P/LgHax/6rmf5AAAAAwEAAQ==";

        byte[] encodedKey = Base64.decodeBase64( KEY_7_3_29_B64 );
        KEY_7_3_29_SHA1 = sha1( encodedKey );
        KEY_7_3_29 = decodeKey( encodedKey );

        KEY_HMAC_B64 = "MzRlZTc5ODMtNWVlNi00MTQ3LWFhODYtNDQzZWEwNjJhYmY3NzQ0OTNkNmEtMmExNS00M2ZlLWFhY2UtZTc4NTY2OTI3NTg1Cg==";

        encodedKey = Base64.decodeBase64( KEY_HMAC_B64 );
        KEY_HMAC = new SecretKeySpec( encodedKey, "HmacSHA1" );
    }

}
