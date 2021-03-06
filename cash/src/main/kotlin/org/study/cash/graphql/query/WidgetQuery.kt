/*
 * Copyright 2019 Expedia, Inc
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
import org.study.cash.graphql.model.Widget
import org.springframework.stereotype.Component

/**
 * Simple widget query.
 */
@Component
class WidgetQuery : Query {

    @GraphQLDescription("creates new widget for given ID")
    fun widgetById(@GraphQLDescription("The special ingredient") id: Int): Widget? = Widget(id)
}
