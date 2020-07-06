package com.startup.startup.util

import com.startup.startup.datamodels.Order

class OrderByDateTime: Comparator<Order> {
    override fun compare(o1: Order?, o2: Order?): Int {
        if(o1 == null || o2 == null)
            return 0
        return o2.datetime.compareTo(o1.datetime)
    }
}