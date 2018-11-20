package dadoufi.musicmanager.remote.util

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

class AnnotationExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false

    }

    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        return (f?.getAnnotation(Exclude::class.java)) != null
    }

}