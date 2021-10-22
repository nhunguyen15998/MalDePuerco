package payments.momo.mservice.allinone;

import payments.momo.mservice.allinone.models.*;
import payments.momo.mservice.allinone.processor.allinone.CaptureMoMo;
import payments.momo.mservice.allinone.processor.allinone.PayATM;
import payments.momo.mservice.allinone.processor.allinone.PaymentResult;
import payments.momo.mservice.allinone.processor.allinone.QueryStatusTransaction;
import payments.momo.mservice.shared.sharedmodels.Environment;
import payments.momo.mservice.shared.utils.LogUtils;
/**
 * Demo
 */
public class AllInOne {

    /***
     * Select environment
     * You can load config from file
     * MoMo only provide once endpoint for each envs: dev and prod
     * @param args
     * @throws
     */

    public static void main(String... args) throws Exception {
        //LogUtils.init();
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());
        long amount = 50000;

        String orderInfo = "Pay With MoMo";
        String returnURL = "https://google.payments.momo.vn";
        String notifyURL = "https://google.payments.momo.vn";
        String extraData = "";
        String bankCode = "SML";

        Environment environment = Environment.selectEnv("dev", Environment.ProcessType.PAY_GATE);


//      Remember to change the IDs at enviroment.properties file

//        Payment Method- Phương thức thanh toán
        CaptureMoMoResponse captureMoMoResponse = CaptureMoMo.process(environment, orderId, requestId, Long.toString(amount), orderInfo, returnURL, notifyURL, "");

//        Transaction Query - Kiểm tra trạng thái giao dịch
        QueryStatusTransactionResponse queryStatusTransactionResponse = QueryStatusTransaction.process(environment, orderId, requestId);

//      Process Payment Result - Xử lý kết quả thanh toán
        PayGateResponse payGateResponse = PaymentResult.process(environment,new PayGateResponse());

    }

}
