package th.ac.kku.cis.mobileapp.wwwlink

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.androidisland.ezpermission.EzPermission
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PermissionListener {
    lateinit var auth: FirebaseAuth
    lateinit var googleClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EzPermission.with(this.applicationContext)
            .permissions(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CAMERA
            )
            .request { granted, denied, permanentlyDenied ->
                //Here you can check results...
                AppExit()
            }


        auth = FirebaseAuth.getInstance()

        sigin.setOnClickListener( {v->singIn()} )
        //sigout.setOnClickListener({v->singOut()})

        var gso = GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleClient = GoogleSignIn.getClient(this,gso)
        auth = FirebaseAuth.getInstance()
        checklogin()

    }
    fun AppExit() {
        finish()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

        /*int pid = android.os.Process.myPid();=====> use this if you want to kill your activity. But its not a good one to do.
    android.os.Process.killProcess(pid);*/
    }
    private fun checklogin(){
        if(auth.currentUser!=null){
            Dexter.withActivity(this)
                .withPermission(Manifest.permission.INTERNET)
                .withListener(this)
                .check()
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
          //  show.text = "No User"
        }
        else{
            //show.text = user.email.toString()
            checklogin()
        }
    }

    private fun singIn() {
        singOut()
        var signInInent = googleClient.signInIntent
        startActivityForResult(signInInent,101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                firebaseAuth(account!!)
                //FirebaseAuth(account)
            }catch (e:ApiException){
                Log.w("login",e)
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

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        val i = Intent(this, Link::class.java)
        startActivity(i)
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {
        TODO("Not yet implemented")
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        TODO("Not yet implemented")
    }
}
