package com.codangcoding.elmandroid

import android.util.Log
import com.codangcoding.elmandroid.AbstractMsg.Msg
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class Program(private val outputScheduler: Scheduler) {

    private val msgRelay: BehaviorRelay<Pair<Msg, State>> = BehaviorRelay.create()

    private lateinit var component: TEAComponent

    lateinit var state: State

    fun init(initialState: State, component: TEAComponent): Disposable {
        state = initialState
        this.component = component

        return msgRelay
                .map { (msg, state) ->
                    Log.d("biji", "$msg")
                    component.update(msg, state)
                }
                .observeOn(outputScheduler)
                .map { (state, cmd) ->
                    this.state = state
                    component.view(state)
                    (state to cmd)
                }
                .filter { (_, cmd) -> cmd !is AbstractCmd.None }
                .observeOn(Schedulers.io())
                .flatMap { (_, cmd) ->
                    component.call(cmd)
                            .onErrorResumeNext { error -> Single.just(AbstractMsg.ErrorMsg(error, cmd)) }
                            .toObservable()
                }
                .observeOn(outputScheduler)
                .subscribe { msg ->
                    when (msg) {
                        is AbstractMsg.Idle -> {
                            /** do nothing */
                        }
                        else -> msgRelay.accept(msg to this.state)
                    }
                }
    }

    fun accept(msg: Msg) {
        msgRelay.accept(msg to state)
    }
}