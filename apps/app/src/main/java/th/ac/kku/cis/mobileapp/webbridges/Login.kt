package th.ac.kku.cis.mobileapp.webbridges

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var googleClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (supportActionBar != null)
            supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        var gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleClient = GoogleSignIn.getClient(this,gso)
        auth = FirebaseAuth.getInstance()
    }
    private fun log(){
        if(auth.getCurrentUser() ==null){
            updateUI(null)
            singIn()
        }
        else{
            updateUI(auth.getCurrentUser())
            singOut()
        }
    }
    private fun singIn() {
        var signInInent = googleClient.signInIntent
        startActivityForResult(signInInent,101)
    }
    private fun singOut() {
        auth.signOut()
        googleClient.signOut().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun updateUI(user: FirebaseUser?) {

    }
}
