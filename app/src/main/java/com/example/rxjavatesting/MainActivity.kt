package com.example.rxjavatesting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.rxjavatesting.databinding.ActivityMainBinding
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            val taskObservable: Observable<Task> = Observable
                .fromIterable(DataSource.createTaskList())
                .subscribeOn(Schedulers.io())
                .filter(Predicate {
                    Log.d(TAG, "test: " + Thread.currentThread().name)
                    Thread.sleep(2000)
                    return@Predicate it.isComplete
                })
                .observeOn(AndroidSchedulers.mainThread())

            taskObservable.subscribe(object : Observer<Task> {
                override fun onSubscribe(d: Disposable) {
                    Log.d(TAG, "onSubscribe: called.")
                }

                override fun onNext(t: Task) {
                    Log.d(TAG, "onNext: " + Thread.currentThread().name)
                    Log.d(TAG, "onNext: " + t.description)
                    Thread.sleep(1000)
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "onError: ", e)
                }

                override fun onComplete() {
                    Log.d(TAG, "onComplete: called.")
                }

            })
        }
    }


}