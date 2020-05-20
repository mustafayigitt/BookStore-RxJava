package com.mustafayigit.bookstore.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mustafayigit.bookstore.data.model.Book
import com.mustafayigit.bookstore.data.remote.Resource
import com.mustafayigit.bookstore.data.remote.Status
import com.mustafayigit.bookstore.data.repository.BookRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created By MUSTAFA
 * on 20/05/2020
 */
class BookViewModel : ViewModel() {

    private val repository: BookRepository = BookRepository()
    private val bookList: MutableLiveData<Resource<List<Book>>> = MutableLiveData()
    private val isLoading = MutableLiveData<Boolean>()

    init {
        fetchBooks()
    }

    fun getBooks() = bookList
    fun isLoading() = if (isLoading.value!!) View.VISIBLE else View.GONE
    fun getErrorMessage(): String? = bookList.value?.message ?: ""

    private fun fetchBooks() {
        bookList.postValue(Resource(Status.LOADING, null, null))
        repository.fetchBooks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.v("TEST", "SUCCESS $it")
                    bookList.postValue(Resource(Status.SUCCESS, it, null))
                    isLoading.value = false
                },
                {
                    Log.v("TEST", "FAIL $it")
                    bookList.postValue(Resource(Status.ERROR, null, it.toString()))
                    isLoading.value = false
                }
            )
    }


}