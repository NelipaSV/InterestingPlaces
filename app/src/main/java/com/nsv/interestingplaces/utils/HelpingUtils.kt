package com.nsv.interestingplaces.utils

class HelpingUtils {
    fun <K, V> getKey(map: Map<K, V>, value: V): K? {
        for ((key, value1) in map) {
            if (value == value1) {
                return key
            }
        }
        return null
    }
}