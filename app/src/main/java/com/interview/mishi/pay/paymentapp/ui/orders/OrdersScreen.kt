package com.interview.mishi.pay.paymentapp.ui.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.interview.mishi.pay.paymentapp.domain.model.OrderItem
import com.interview.mishi.pay.paymentapp.ui.components.CardLayout
import com.interview.mishi.pay.paymentapp.ui.components.LabelHeading
import com.interview.mishi.pay.paymentapp.ui.components.NetworkImage
import com.interview.mishi.pay.paymentapp.ui.components.OrderSummaryLayout
import com.interview.mishi.pay.paymentapp.ui.components.ProgressDialog
import com.interview.mishi.pay.paymentapp.ui.components.SnackBarLayout
import com.interview.mishi.pay.paymentapp.ui.theme.getColors
import com.interview.mishi.pay.paymentapp.utils.UIState
import com.interview.mishi.pay.paymentapp.utils.toAmount
import com.interview.mishi.pay.paymentapp.utils.value

@Composable
fun OrdersScreen(viewModel: OrdersViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.getOrders()
    }
    when (val result = viewModel.orderItems.collectAsState().value) {
        is UIState.Success -> {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(
                    count = result.data.size,
                    key = { index -> result.data[index].invoiceNo.value() }
                ) { index ->
                    OrdersView(result.data[index])
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        is UIState.Loading -> {
            ProgressDialog()
        }

        is UIState.Error -> {
            SnackBarLayout(result.error, getColors().error, getColors().errorContainer)
            viewModel.reset()
        }

        else -> {}
    }
}


@Composable
fun OrdersView(item: OrderItem) {
    CardLayout {
        LabelHeading(
            item.invoiceNo.value()
        )
        item.itemsList?.let { orders ->
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp) // Space between items
            ) {
                items(orders.size) { index ->
                    NetworkImage(
                        orders[index].image.value(),
                        contentDescription = orders[index].name,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }
        OrderSummaryLayout("Subtotal", item.subTotal.toAmount())
        OrderSummaryLayout("Shipping", item.shipping.toAmount())
        OrderSummaryLayout("Estimated Tax", item.tax.toAmount())
        OrderSummaryLayout("Total", item.total.toAmount())
    }
}