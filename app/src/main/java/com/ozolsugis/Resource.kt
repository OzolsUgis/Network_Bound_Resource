package com.ozolsugis.network_bound_resource

// Resource class as wrapper class , very useful to handle responses and using
// progress bars



data class Resource <out T> (val status : Status, val data : T? , val message : String?){
    companion object{
        fun <T> success(data : T?): Resource<T>{
            return Resource(Status.SUCCESS, data , null)
        }
        fun <T> error (message: String?, data: T?) : Resource<T>{
            return Resource(Status.ERROR, data, message)
        }
        fun <T> loading (data  : T?) : Resource<T>{
            return Resource(Status.LOADING, data, null)
        }
    }


    //
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}
