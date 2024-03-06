package ba.out.bring.odoo.mc1.core.web.session.modules

import ba.out.bring.odoo.mc1.core.entities.session.modules.Modules
import ba.out.bring.odoo.mc1.core.entities.session.modules.ModulesReqBody
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ModulesRequest {

    @POST("/web/session/modules")
    fun modules(
            @Body modulesReqBody: ModulesReqBody
    ): Observable<Response<Modules>>
}
