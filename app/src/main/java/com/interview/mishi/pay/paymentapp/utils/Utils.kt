package com.interview.mishi.pay.paymentapp.utils

import android.annotation.SuppressLint
import com.interview.mishi.pay.paymentapp.domain.model.CartItem
import com.interview.mishi.pay.scanner.Product
import java.util.Date

fun Product.toProductItem(): CartItem = CartItem(
    this.id,
    this.name,
    this.desc,
    this.price,
    this.image,
    updatedTime = Date()
)

fun String?.value() = this ?: ""
fun Int?.value() = this ?: 0
fun Double?.value() = this ?: 0.0

@SuppressLint("DefaultLocale")
fun Double?.toAmount(): String = String.format("%.2f", this.value())