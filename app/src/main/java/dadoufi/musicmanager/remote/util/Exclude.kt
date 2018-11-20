package dadoufi.musicmanager.remote.util


@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Exclude
