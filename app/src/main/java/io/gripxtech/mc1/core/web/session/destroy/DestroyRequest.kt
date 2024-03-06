package ba.out.bring.odoo.mc1.core.web.session.destroy

import ba.out.bring.odoo.mc1.core.entities.session.destroy.Destroy
import ba.out.bring.odoo.mc1.core.entities.session.destroy.DestroyReqBody
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DestroyRequest {

    @POST("/web/session/destroy")
    fun destroy(
            @Body destroyReqBody: DestroyReqBody
    ): Observable<Response<Destroy>>
}
