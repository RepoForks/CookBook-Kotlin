package com.simoncherry.cookbookkotlin.util

import com.simoncherry.cookbookkotlin.api.APIException
import com.simoncherry.cookbookkotlin.model.MobAPIResult
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * <pre>
 *     author : Donald
 *     e-mail : xxx@xx
 *     time   : 2017/05/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class RxUtils {
    companion object {
        fun <T> rxSchedulerHelper(): FlowableTransformer<T, T> {
            return FlowableTransformer { observable ->
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun <T> createData(t: T): Flowable<T> {
            return Flowable.create({ emitter ->
                try {
                    emitter.onNext(t)
                    emitter.onComplete()
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            }, BackpressureStrategy.BUFFER)
        }

        fun <T> handleMobResult(): FlowableTransformer<MobAPIResult<T>, T> = //compose判断结果
                FlowableTransformer { httpResponseFlowable ->
                    httpResponseFlowable.flatMap { response: MobAPIResult<T> ->
                        if (response.result != null) {
                            createData(response.result)
                        } else {
                            Flowable.error(APIException(500, "服务器返回error"))
                        }
                    }
                }
    }
}