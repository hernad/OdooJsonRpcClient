package ba.out.bring.odoo.mc1.core.web.session.check

import ba.out.bring.odoo.mc1.core.entities.session.check.Check
import ba.out.bring.odoo.mc1.core.entities.session.check.CheckReqBody
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CheckRequest {

    @POST("/web/session/check")
    fun check(
            @Body checkReqBody: CheckReqBody
    ): Observable<Response<Check>>
}
