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
import de.psdev.licensesdialog.LicensesDialog
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20
import de.psdev.licensesdialog.licenses.GnuLesserGeneralPublicLicense21
import de.psdev.licensesdialog.licenses.GnuLesserGeneralPublicLicense3
import de.psdev.licensesdialog.licenses.MITLicense
import de.psdev.licensesdialog.model.Notice
import de.psdev.licensesdialog.model.Notices
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
        var licensebtn = root.findViewById<Button>(R.id.Licenses_btn) as Button
        licensebtn.setOnClickListener { onMultipleProgrammaticClick() }
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
    fun onMultipleProgrammaticClick() {
        val notices = Notices()
        notices.addNotice(
            Notice(
                "LicensesDialog",
                "https://github.com/PSDev/LicensesDialog",
                "Philip Schiffer",
                ApacheSoftwareLicense20()
            )
        )
        notices.addNotice(
            Notice(
                "Material Chip View",
                "https://github.com/robertlevonyan/materialChipView",
                "Robert Levonyan",
                ApacheSoftwareLicense20()
            )
        )
        notices.addNotice(
            Notice(
                "LoadingView",
                "https://github.com/samigehi/LoadingView",
                "Sumeet Kumar",
                GnuLesserGeneralPublicLicense3()
            )
        )
        notices.addNotice(
            Notice(
                "GButtons",
                "https://github.com/TutorialsAndroid/GButton",
                "Akshay Sunil Masram",
                MITLicense()
            )
        )
        notices.addNotice(
            Notice(
                "RichLink-Preview",
                "https://github.com/PonnamKarthik/RichLinkPreview",
                "Karthik Ponnam",
                ApacheSoftwareLicense20()
            )
        )
        notices.addNotice(
            Notice(
                "Simple Dialog",
                "https://github.com/BROUDING/SimpleDialog",
                "John Lee",
                ApacheSoftwareLicense20()
            )
        )
        notices.addNotice(
            Notice(
                "Dexter",
                "https://github.com/Karumi/Dexter",
                "Karumi",
                ApacheSoftwareLicense20()
            )
        )
        LicensesDialog.Builder(requireContext())
            .setNotices(notices)
            .setIncludeOwnLicense(true)
            .build()
            .show()
    }

}
