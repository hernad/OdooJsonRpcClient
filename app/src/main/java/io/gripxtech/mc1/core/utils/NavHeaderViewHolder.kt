package ba.out.bring.odoo.mc1.core.utils

import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import ba.out.bring.odoo.mc1.App
//import ba.out.bring.odoo.mc1.GlideRequests
import ba.out.bring.odoo.mc1.R
import ba.out.bring.odoo.mc1.core.OdooUser
import ba.out.bring.odoo.mc1.trimFalse
import com.bumptech.glide.RequestManager

class NavHeaderViewHolder(
    view: View
) {
    private val pic: ImageView = view.findViewById(R.id.userImage)
    private val name: TextView = view.findViewById(R.id.header_name)
    private val email: TextView = view.findViewById(R.id.header_details)
    private val menuToggle: ConstraintLayout = view.findViewById(R.id.menuToggle)
    private val menuToggleImage: ImageView = view.findViewById(R.id.ivDropdown)


    fun setUser(user: OdooUser, glideRequests: RequestManager) {
        name.text = user.name
        email.text = user.login
        if (user.image512.trimFalse().isNotEmpty()) {
            val byteArray = Base64.decode(user.image512, Base64.DEFAULT)
            glideRequests
                .asBitmap()
                .load(byteArray)
                .dontAnimate()
                .circleCrop()
                .into(pic)
        } else {
            glideRequests
                .asBitmap()
                .load((pic.context.applicationContext as ba.out.bring.odoo.mc1.App).getLetterTile(user.name))
                .dontAnimate()
                .circleCrop()
                .into(pic)
        }
    }
}
