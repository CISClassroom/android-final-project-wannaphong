package th.ac.kku.cis.mobileapp.wwwlink

class Cleanurl {
    fun clean(s:String):String{
        var text = s.split("?fbclid")[0]
        return text
    }
}