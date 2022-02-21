package com.hhk.formstatetest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class AccCreateViewModel:ViewModel() {

    val firstName:MutableStateFlow<String> = MutableStateFlow("")
    val lastName:MutableStateFlow<String> = MutableStateFlow("")
    val email:MutableStateFlow<String> = MutableStateFlow("")
    val gender:MutableStateFlow<GENDER> = MutableStateFlow(GENDER.Male)
    val date:MutableStateFlow<String> = MutableStateFlow(INIT_DATE)
    val nationality:MutableStateFlow<String> = MutableStateFlow("")
    val country:MutableStateFlow<String> = MutableStateFlow("")
    val mobile:MutableStateFlow<Int> = MutableStateFlow(0)



    private val errorMessage:MutableStateFlow<String?> = MutableStateFlow(null)

    private val user:MutableStateFlow<User?> = MutableStateFlow(null)

    var isValid = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            errorMessage.collectLatest {
                isValid.value = it.isNullOrEmpty()
            }
        }
    }

    private fun validate() {
        viewModelScope.launch {

            if (firstName.value.isBlank())
                errorMessage.value = "First Name Required"
            else if (lastName.value.isBlank())
                errorMessage.value = "Last Name Required"
            else if (email.value.isBlank())
                errorMessage.value = "E-Mail Required"
            else if (date.value== INIT_DATE)
                errorMessage.value = "Date Required"
            else if (nationality.value.isBlank())
                errorMessage.value = "Nationality Required"
            else if (country.value.isBlank())
                errorMessage.value = "Country of Residence Required"
            else
                errorMessage.value = null

            val fName = firstName.value
            val lName = lastName.value
            val mail = email.value
            val dt = date.value
            val gend = gender.value
            val nation = nationality.value
            val count = country.value
            val mob = mobile.value

           user.value = User(fName,lName,mail,dt,gend,nation,count,mob)


        }
    }


    fun onCreateAcc(onSuccess:()->Unit,onError:(String)->(Unit))
    {

        validate()
        if (isValid.value) onSuccess.invoke()
        else errorMessage.value?.let { onError.invoke(it) }



    }

    companion object {
        const val INIT_DATE = "DD/MM/YYYY"
    }

}

enum class GENDER {
    Male,Female
}

data class User(
    val firstName:String,
    val lastName:String,
    val mail:String,
    val dob:String,
    val gender: GENDER,
    val nationality:String,
    val country_of_reside:String,
    val mobileNo:Int
)



