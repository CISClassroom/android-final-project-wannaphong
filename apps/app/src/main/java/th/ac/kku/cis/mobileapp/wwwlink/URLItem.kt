package th.ac.kku.cis.mobileapp.wwwlink

class URLItem {
    companion object Factory{ // สร้างเมดทอนแบบย่อ ๆ
        fun create():URLItem = URLItem()
    }
    var objID:String? = null
    var URLtitle:String? = null
    var URLtag:String? = null
    var url:String? = null
}