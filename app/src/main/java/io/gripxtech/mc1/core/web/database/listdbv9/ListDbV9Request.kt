package ba.out.bring.odoo.mc1.core.web.database.listdbv9

import ba.out.bring.odoo.mc1.core.entities.database.listdb.ListDb
import ba.out.bring.odoo.mc1.core.entities.database.listdb.ListDbReqBody
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ListDbV9Request {

    @POST("/jsonrpc")
    fun listDb(
            @Body listDbReqBody: ListDbReqBody
    ): Observable<Response<ListDb>>
}