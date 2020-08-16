package graphql.execution

import graphql.Internal
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLScalarType
import org.study.account.exception.ErrorCodeException

/**
 * This will check that a value is non null when the type definition says it must be and it will throw [NonNullableFieldWasNullException]
 * if this is not the case.
 *
 * See: http://facebook.github.io/graphql/#sec-Errors-and-Non-Nullability
 */
@SuppressWarnings("UnusedDeclaration")
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
    @SuppressWarnings("UnusedDeclaration")
    @Throws(NonNullableFieldWasNullException::class)
    fun <T> validate(path: ExecutionPath?, result: T?): T? {
        if (result == null) {
            if (executionStepInfo.isNonNullType) {
                // see http://facebook.github.io/graphql/#sec-Errors-and-Non-Nullability
                val originalWrappedType = executionStepInfo.type
                if (originalWrappedType is GraphQLNonNull) {
                    val scalarType = originalWrappedType.wrappedType
                    if (scalarType is GraphQLScalarType && scalarType.name == "Unit") {
                        return null
                    }
                }
                val nonNullException = NonNullableFieldWasNullException(executionStepInfo, path)
                if (executionContext.errors.isEmpty()) {
//                    || executionContext.errors.first() !is ErrorCodeException
                    executionContext.addError(NonNullableFieldWasNullError(nonNullException), path)
                }

                throw nonNullException
            }
        }
        return result
    }

}