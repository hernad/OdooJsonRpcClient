package ba.out.bring.odoo.mc1.core.web.dataset.load

import ba.out.bring.odoo.mc1.core.entities.dataset.load.Load
import ba.out.bring.odoo.mc1.core.entities.dataset.load.LoadReqBody
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoadRequest {

    @POST("/web/dataset/load")
    fun load(
            @Body loadReqBody: LoadReqBody
    ): Observable<Response<Load>>
}