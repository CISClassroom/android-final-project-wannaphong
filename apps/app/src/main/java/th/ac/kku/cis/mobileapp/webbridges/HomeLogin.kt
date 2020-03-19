package th.ac.kku.cis.mobileapp.webbridges

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class HomeLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_login)
        if(checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED ){
            val permission = arrayOf(Manifest.permission.INTERNET)
            requestPermissions(permission,1)
        }
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
}
