package ba.out.bring.odoo.mc1.core.entities.webclient.versionInfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ba.out.bring.odoo.mc1.core.entities.odooError.OdooError

data class VersionInfo(

        @field:Expose
        @field:SerializedName("result")
        var result: VersionInfoResult = VersionInfoResult(),

        @field:Expose
        @field:SerializedName("error")
        var odooError: OdooError = OdooError()
) {
    val isSuccessful get() = !isOdooError
    val isOdooError get() = odooError.message.isNotEmpty()
    val errorCode get() = odooError.code
    val errorMessage get() = odooError.data.message
}
