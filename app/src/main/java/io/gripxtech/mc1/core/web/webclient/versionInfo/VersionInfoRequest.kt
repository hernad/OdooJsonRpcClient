package ba.out.bring.odoo.mc1.core.web.webclient.versionInfo

import ba.out.bring.odoo.mc1.core.entities.webclient.versionInfo.VersionInfo
import ba.out.bring.odoo.mc1.core.entities.webclient.versionInfo.VersionInfoReqBody
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface VersionInfoRequest {

    @POST("/web/webclient/version_info")
    fun versionInfo(
            @Body versionInfoReqBody: VersionInfoReqBody
    ): Observable<Response<VersionInfo>>
}