package payments.momo.mservice.pay;

import com.google.gson.Gson;

import javafx.application.Platform;
import payments.momo.mservice.pay.models.*;
import payments.momo.mservice.pay.processor.notallinone.*;
import payments.momo.mservice.shared.constants.Parameter;
import payments.momo.mservice.shared.constants.RequestType;
import payments.momo.mservice.shared.sharedmodels.Environment;
import payments.momo.mservice.shared.sharedmodels.PartnerInfo;
import payments.momo.mservice.shared.utils.Encoder;
import payments.momo.mservice.shared.utils.LogUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Pay {

	private Executor executor = Executors.newSingleThreadExecutor();

	private Webcam webcam = null;
	private WebcamPanel panel = null;
	
    public void payAction() throws Exception {

    	Dimension size = WebcamResolution.QVGA.getSize();

		webcam = Webcam.getWebcams().get(0);
		webcam.setViewSize(size);

		panel = new WebcamPanel(webcam);
		panel.setPreferredSize(size);
		panel.setFPSDisplayed(true);
		
		

        String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAz4r6huNXk2zNk1/TC/e8SlSIaFhKI5A0Ef89anlBgvUG79Z03d05ItJXHtX49SjqicygOtRKdfJ43XBgtN7PpeZX8fnwMo/D//z39SgacfdeaCBgRVpiupcuPtvuGXqZUsRwr62Fh3qkjSjyUqqk7O8hdi0UoOs+WQU1HGHuVIwG0uINOVJHuIlMjBx0+2qoFtyME3MP3i+la4XL2MW7Su3X+DJjbpoBgSRGtGnNsUCKOiF5W9a2K+QKY0UDxRTp9YGqhYX8B3RgbtU6whlP2Y+0+fAJKX0JP0KrCDVWvNP7u3ekc45Ulg3th3WxMF2n2piGQnpU/y/aEyWzgzqAQUsnRX5WljFTlE4bwdAHuLVKISsUzDImojJkRKcRcmFFgtrAljjfiRWX6pFRz0Y29LzrQYXMEfFukmmCOeAxcXGaHILM+SPSLx8bfYa+2MT1oYsbM3KtTt9ANixyIkWwNJ/PWMaqLIJpDtXuF05HzCij8ff3X168oascrBoB3plj22RF87Ot4kiYnBolrXuz4iLP1xSWFaumawyJepVqLQIt0ukVC8+S4KQK/sspd3mRVE/bWkTRTij+1ZANEJCaJQu08RPNzSx8/4UmWEc/UIhIOiq8a1H3of59DJFfvCzxlRI2XMvJOWDp5H8XolEk1OlQlFyV86b3yjKeQInBcgMCAwEAAQ==";

        PartnerInfo devInfo = new PartnerInfo("MOMOCIST20211013", "EnBd6YB0K5kT1Mf1", "waLJp9tJBW5C6R6bsn0L1GlNR9lQI6no",publicKey);
        //change the endpoint according to the appropriate processes

        String commit = RequestType.CONFIRM_APP_TRANSACTION;
        String rollback = RequestType.CANCEL_APP_TRANSACTION;
        long amount = 1000;
        String partnerRefId = String.valueOf(System.currentTimeMillis());
        String partnerTransId = "1561046083186";
        String requestId = String.valueOf(System.currentTimeMillis());
        String momoTransId = "147938695";
        String customerNumber = "0975923945";
        String partnerName = "MalDePuerco";
        String storeId = "ABCD1234";
        String storeName = "MalDePuerco";
        String appData = "1561046083186";//data from MoMo app
        String description = "Pay with MoMo";
        String paymentCode = "MM393782749752157086";



        // Uncomment to use the processes you need
        // Make sure you are using the correct environment for each processes
        // Change to valid IDs and information to use AppPay, POS, Refund processes.

        //
//		 AppPayResponse appProcessResponse = AppPay.process(Environment.selectEnv("dev", Environment.ProcessType.APP_IN_APP), partnerRefId, partnerTransId, amount, partnerName,
//		         storeId, storeName, publicKey, customerNumber, appData, description, Parameter.VERSION, Parameter.APP_PAY_TYPE);
//		System.out.println(appProcessResponse);
        
       
         	POSPayResponse posProcessResponse = POSPay.process(Environment.selectEnv(Environment.EnvTarget.DEV, Environment.ProcessType.PAY_POS), partnerRefId, amount, storeId, storeName, publicKey, description, paymentCode, Parameter.VERSION, Parameter.APP_PAY_TYPE);
         	System.out.println(posProcessResponse);
//
//        PayConfirmationResponse payConfirmationResponse = PayConfirmation.process(Environment.selectEnv("dev", Environment.ProcessType.PAY_CONFIRM), "35646", rollback, requestId, "2305062978", "", "");
//
//        TransactionQueryResponse transactionQueryResponse = TransactionQuery.process(Environment.selectEnv("dev", Environment.ProcessType.PAY_QUERY_STATUS), "1562298553079", "1562299067267", publicKey, "", Parameter.VERSION);
//        TransactionQuery.process(Environment.selectEnv("dev", Environment.ProcessType.PAY_QUERY_STATUS), "1562299067267", "1562299177871", publicKey, "", Parameter.VERSION);
//
//        TransactionRefundResponse transactionRefundResponse = TransactionRefund.process(Environment.selectEnv("dev", Environment.ProcessType.PAY_QUERY_STATUS), "1562298553079", "", publicKey, "2305062760", amount, "", "1562299067267", Parameter.VERSION);
    }
    //generate RSA signature from given information
    public static String generateRSA(String phoneNumber, String billId, String transId, String username, String partnerCode, long amount, String publicKey) throws Exception {
        // current version of Parameter key name is 2.0
        Map<String, Object> rawData = new HashMap<String, Object>();
        rawData.put(Parameter.CUSTOMER_NUMBER, phoneNumber);
        rawData.put(Parameter.PARTNER_REF_ID, billId);
        rawData.put(Parameter.PARTNER_TRANS_ID, transId);
        rawData.put(Parameter.USERNAME, username);
        rawData.put(Parameter.PARTNER_CODE, partnerCode);
        rawData.put(Parameter.AMOUNT, amount);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(rawData);
        byte[] testByte = jsonStr.getBytes(StandardCharsets.UTF_8);
        String hashRSA = Encoder.encryptRSA(testByte, publicKey);

        return hashRSA;
    }
}