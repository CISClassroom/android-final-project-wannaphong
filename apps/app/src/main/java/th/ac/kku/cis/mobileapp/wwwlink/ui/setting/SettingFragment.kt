package th.ac.kku.cis.mobileapp.wwwlink.ui.setting

import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.setting_fragment.*
import th.ac.kku.cis.mobileapp.wwwlink.MainActivity

import th.ac.kku.cis.mobileapp.wwwlink.R

class SettingFragment : Fragment() {

    companion object {
        fun newInstance() = SettingFragment()
    }

    private lateinit var viewModel: SettingViewModel
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root= inflater.inflate(R.layout.setting_fragment, container, false)
        val NameSetting: TextView = root.findViewById(R.id.NameSetting)
        val Profile:ImageView = root.findViewById(R.id.imageProfile)
        auth = FirebaseAuth.getInstance()
        val xx: Uri? = auth.currentUser!!.photoUrl
        NameSetting.text = auth.currentUser!!.displayName.toString()
        val LogoutBtn:Button = root.findViewById(R.id.logoutSetting)
        LogoutBtn.setOnClickListener {
            singOut()
        }
        Picasso.get().load(xx).into(Profile)
        return root
    }
    private fun singOut() {
        auth.signOut()
        this.getActivity()?.finish();
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
