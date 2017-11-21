/**
 * Copyright (C) 2016 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.syndesis.rest.v1.handler.connection;

import java.util.List;

import io.syndesis.connector.generator.ConnectorGenerator;
import io.syndesis.dao.manager.DataManager;
import io.syndesis.model.connection.Connector;
import io.syndesis.model.connection.ConnectorTemplate;
import io.syndesis.model.connection.CustomConnector;
import io.syndesis.rest.v1.operations.Violation;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomConnectorHandlerTest {

    private final ApplicationContext applicationContext = mock(ApplicationContext.class);

    private final DataManager dataManager = mock(DataManager.class);

    @Test
    public void shouldProvideInfoAboutCustomConnectors() {
        final CustomConnectorHandler handler = new CustomConnectorHandler("connector-template", dataManager,
            applicationContext);
        final ConnectorGenerator connectorGenerator = mock(ConnectorGenerator.class);

        final ConnectorTemplate template = new ConnectorTemplate.Builder().build();
        final CustomConnector customConnector = new CustomConnector.Builder().build();
        final Connector connector = new Connector.Builder().build();

        when(dataManager.fetch(ConnectorTemplate.class, "connector-template")).thenReturn(template);
        when(applicationContext.getBean("connector-template", ConnectorGenerator.class)).thenReturn(connectorGenerator);
        when(connectorGenerator.info(same(template), same(customConnector))).thenReturn(connector);

        final Connector connectorInfo = handler.info(customConnector);

        assertThat(connectorInfo).isSameAs(connector);
    }

    @Test
    public void shouldVlidateCustomConnectors() {
        final CustomConnectorHandler handler = new CustomConnectorHandler("connector-template", dataManager,
            applicationContext);

        final CustomConnector customConnector = new CustomConnector.Builder().build();

        final List<Violation> violations = handler.validate(customConnector);

        assertThat(violations).isEmpty();
    }
}
