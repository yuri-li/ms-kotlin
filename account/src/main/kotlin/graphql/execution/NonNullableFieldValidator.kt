package graphql.execution

import graphql.Internal
import org.study.account.exception.ErrorCodeException

/**
 * This will check that a value is non null when the type definition says it must be and it will throw [NonNullableFieldWasNullException]
 * if this is not the case.
 *
 * See: http://facebook.github.io/graphql/#sec-Errors-and-Non-Nullability
 */
@Internal
class NonNullableFieldValidator(
    private val executionContext: ExecutionContext,
    private val executionStepInfo: ExecutionStepInfo
) {

    /**
     * Called to check that a value is non null if the type requires it to be non null
     *
     * @param path   the path to this place
     * @param result the result to check
     * @param <T>    the type of the result
     *
     * @return the result back
     *
     * @throws NonNullableFieldWasNullException if the value is null but the type requires it to be non null
    </T> */
    @Throws(NonNullableFieldWasNullException::class)
    fun <T> validate(path: ExecutionPath?, result: T?): T? {
        if (result == null) {
            if (executionStepInfo.isNonNullType) {
                // see http://facebook.github.io/graphql/#sec-Errors-and-Non-Nullability
                //
                //    > If the field returns null because of an error which has already been added to the "errors" list in the response,
                //    > the "errors" list must not be further affected. That is, only one error should be added to the errors list per field.
                //
                // We interpret this to cover the null field path only.  So here we use the variant of addError() that checks
                // for the current path already.
                //
                // Other places in the code base use the addError() that does not care about previous errors on that path being there.
                //
                // We will do this until the spec makes this more explicit.
                //
                val nonNullException = NonNullableFieldWasNullException(executionStepInfo, path)
                if(executionContext.errors.isEmpty() || !(executionContext.errors.first() is ErrorCodeException)){
                    executionContext.addError(NonNullableFieldWasNullError(nonNullException), path)
                }

                throw nonNullException
            }
        }
        return result
    }

}