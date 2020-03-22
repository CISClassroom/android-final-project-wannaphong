package th.ac.kku.cis.mobileapp.wwwlink

class UserLink {
    companion object Factory{ // สร้างเมดทอนแบบย่อ ๆ
        fun create():UserLink = UserLink()
    }
    var objID:String? = null
    var URL:String? = null
    var URLobj:String? = null
    var Note:String? = null
    var status:Boolean? = null
    var TimeSave:String? = null
}