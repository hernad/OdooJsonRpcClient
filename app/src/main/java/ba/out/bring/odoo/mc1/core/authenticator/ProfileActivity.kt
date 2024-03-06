package ba.out.bring.odoo.mc1.core.authenticator

import android.os.Bundle
import android.util.Base64
import ba.out.bring.odoo.mc1.*
import ba.out.bring.odoo.mc1.core.utils.BaseActivity
//import kotlinx.android.synthetic.main.activity_profile.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.Glide
import ba.out.bring.odoo.mc1.databinding.ActivityProfileBinding


class ProfileActivity : BaseActivity() {

    // https://bumptech.github.io/glide/doc/generatedapi.html

    private lateinit var app: ba.out.bring.odoo.mc1.App
    lateinit var glideRequests: RequestManager
    lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityProfileBinding.inflate(layoutInflater)
      val view = binding.root
      setContentView(view)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        app = application as ba.out.bring.odoo.mc1.App
        glideRequests = Glide.with(this)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val user = getActiveOdooUser()
        if (user != null) {
            val image512 = user.image512.trimFalse()
            val name = user.name.trimFalse()

            glideRequests.asBitmap().load(
                if (image512.isNotEmpty())
                    Base64.decode(image512, Base64.DEFAULT)
                else
                    app.getLetterTile(if (name.isNotEmpty()) name else "X")
            ).circleCrop().into(binding.ivProfile)

            binding.ctl.title = name
            binding.tvName.text = name
            binding.tvLogin.text = user.login
            binding.tvServerURL.text = user.host
            binding.tvDatabase.text = user.database
            binding.tvVersion.text = user.serverVersion
            binding.tvTimezone.text = user.timezone
        }
    }
}
