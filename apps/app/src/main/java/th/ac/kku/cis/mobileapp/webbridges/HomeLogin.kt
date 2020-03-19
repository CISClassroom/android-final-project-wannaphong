package th.ac.kku.cis.mobileapp.webbridges

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_home_login.*

class HomeLogin : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var googleClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_login)

        auth = FirebaseAuth.getInstance()

        sigin.setOnClickListener( {v->singIn()} )
        sigout.setOnClickListener({v->singOut()})

        var gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleClient = GoogleSignIn.getClient(this,gso)
        auth = FirebaseAuth.getInstance()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode){
            1 -> {
            }
        }
    }
    private fun singOut() {
        auth.signOut()
        googleClient.signOut().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user==null){
            show.text = "No User"
        }
        else{
            show.text = user.email.toString()
        }
    }

    private fun singIn() {
       /* if(checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED ){
            val permission = arrayOf(Manifest.permission.INTERNET)
            requestPermissions(permission,1)
        }*/
        var signInInent = googleClient.signInIntent
        startActivityForResult(signInInent,101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                Log.w("GG","OK")
                val account = task.getResult(ApiException::class.java)
                firebaseAuth(account!!)
                //FirebaseAuth(account)
            }catch (e: ApiException){
                Log.w("GG",e)
                updateUI(null)
            }
        }
    }

    private fun firebaseAuth(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this){task->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    updateUI(user)
                }
                else{
                    updateUI(null)
                }
            }
    }
}
