/*
 * Copyright 2020 Expedia, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.study.cash.graphql.query

import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component

@Suppress("FunctionOnlyReturningConstant")
@Component
class SimpleQuery : Query {
    fun dataFromCash() = "hello from cash-service"

    @Deprecated(message = "old deprecated query", replaceWith = ReplaceWith("dataFromBaseApp"))
    fun deprecatedBaseAppQuery() = "this is deprecated"

    @GraphQLDescription("Comment with pattern: `^\\+[1-9]\\d{7,14}$")
    fun commentsWithEscapeCharacters() = "escaping \\"
}
