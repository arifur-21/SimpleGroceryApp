package com.example.groceryapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCCustomerInfoInitializer
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCProductInitializer
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCProductInitializer.ProductProfile.TravelVertical
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCShipmentInfoInitializer
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCShipmentInfoInitializer.ShipmentDetails
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener

class CartPaymentActivity : AppCompatActivity(), SSLCTransactionResponseListener {
    private lateinit var success: TextView
    private lateinit var faild : TextView
    private lateinit var error: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_payment)

        success = findViewById(R.id.successId)
        faild = findViewById(R.id.faildId)
        error = findViewById(R.id.errorId)


        val sslCommerzInitialization = SSLCommerzInitialization(
            "sbs60605f415c440", "sbs60605f415c440@ssl", 100.0,
            SSLCCurrencyType.BDT, "123456789098765", "yourProductType", SSLCSdkType.TESTBOX
        )

        val customerInfoInitializer = SSLCCustomerInfoInitializer(
            "customer name", "customer email",
            "address", "dhaka", "1214", "Bangladesh", "phoneNumber"
        )

        val productInitializer = SSLCProductInitializer(
            "food", "food", TravelVertical(
                "Travel", "xyz",
                "A", "12", "Dhk-Syl"
            )
        )

        val shipmentInfoInitializer = SSLCShipmentInfoInitializer(
            "Courier", 2, ShipmentDetails(
                "AA", "Address 1",
                "Dhaka", "1000", "BD"
            )
        )


        IntegrateSSLCommerz
            .getInstance(this)
            .addSSLCommerzInitialization(sslCommerzInitialization) // .addCustomerInfoInitializer(customerInfoInitializer)
            //.addProductInitializer(productInitializer)
            .buildApiCall(this)


    }

    override fun transactionSuccess(p0: SSLCTransactionInfoModel?) {
        success.setText(p0!!.apiConnect +"--"+ p0!!.status)
    }

    override fun transactionFail(p0: String?) {
        faild.text = p0
    }

    override fun merchantValidationError(p0: String?) {
       error.text = p0
    }
}