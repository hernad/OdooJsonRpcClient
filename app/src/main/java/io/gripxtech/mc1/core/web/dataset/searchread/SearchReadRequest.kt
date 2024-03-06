package ba.out.bring.odoo.mc1.core.web.dataset.searchread

import ba.out.bring.odoo.mc1.core.entities.dataset.searchread.SearchRead
import ba.out.bring.odoo.mc1.core.entities.dataset.searchread.SearchReadReqBody
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SearchReadRequest {

    @POST("/web/dataset/search_read")
    fun searchRead(
            @Body searchReadReqBody: SearchReadReqBody
    ): Observable<Response<SearchRead>>
}
