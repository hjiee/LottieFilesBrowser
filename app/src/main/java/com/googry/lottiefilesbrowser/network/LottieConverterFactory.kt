package com.googry.lottiefilesbrowser.network

import com.dino.library.ext.extractNumber
import com.dino.library.ext.logE
import com.dino.library.ext.toJsonString
import com.googry.lottiefilesbrowser.network.model.LottieInfoResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.text.DecimalFormat

class LottieConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<out Annotation>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody, *> {
        return STRING_RESPONSE
    }

    override fun requestBodyConverter(
        type: Type?,
        parameterAnnotations: Array<out Annotation>?,
        methodAnnotations: Array<out Annotation>?,
        retrofit: Retrofit?
    ): Converter<*, RequestBody> {
        throw IllegalStateException("It doesn't make request body")
    }

    companion object {

        val decimalFormat = DecimalFormat("#,###")

        val STRING_RESPONSE = Converter<ResponseBody, List<LottieInfoResponse>> {
            Jsoup.parse(it.string())
                .select("#app > div.bg-white.pb-5 > section.lf-padding.container.mx-auto > div > div > div > div")
                .map {
                    val (id, heartCount) = it.select("likes").run {
                        attr("id") to attr("count").extractNumber()
                    }
                    val author = it.select("h3.text-sm.font-normal.px-2.text-grey-dark").text()
                    val imgUrl = it.select("figure > a > img").attr("src")
                    val downloadCount = it.select("span").text().extractNumber()
                    val dataUrl = it.select("lottie").attr("path")
                    val data = LottieInfoResponse(
                        id = id,
                        dataUrl = dataUrl,
                        author = author,
                        authorProfileUrl = imgUrl,
                        heartCount = decimalFormat.format(heartCount.toInt()),
                        downloadCount = decimalFormat.format(downloadCount.toInt())
                    )

                    logE("${data.toJsonString()}")
                    data
                }
        }

    }
}