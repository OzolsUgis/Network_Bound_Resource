package com.ozolsugis.network_bound_resource

import kotlinx.coroutines.flow.*


// ResultType - DAO function that returns List<>
// RequestType - Response what comes from Server

// Flow is a coroutine that can return different values at different times

inline fun <ResultType, RequestType>networkBoundResource (
    // Returns cached entries from the local database
    crossinline query : () -> Flow<ResultType>,
    // Gets up-to-date entries from the API
    crossinline fetch : suspend () -> RequestType,
    // Caches up-to-date entries
    crossinline saveFetchResult : suspend(RequestType)-> Unit,
    // Handles errors occurring while getting up-to-date data
    crossinline onFetchFailed : (Throwable)-> Unit = {Unit},
    // Determines, if we load data from the API or from the local cache
    crossinline shouldFetch : (ResultType)-> Boolean = {true}
)= flow<Resource<ResultType>>{
    emit(Resource.loading(null))
    val data = query().first()

    val flow = if(shouldFetch(data)){
        emit(Resource.loading(data))
        try {
            val fetchedResult = fetch()
            saveFetchResult(fetchedResult)
            query().map {
                Resource.success(it)
            }
        }catch(t : Throwable){
            onFetchFailed(t)
            query().map {
                Resource.error("Could not reach server", it)
            }
        }
    }else{
        query().map{Resource.success(it)}
    }
    emitAll(flow)
}



