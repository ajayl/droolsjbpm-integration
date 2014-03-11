/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.spring.tests;

import org.drools.decisiontable.Cheese;
import org.drools.decisiontable.Person;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class KieSpringBasicsDecisionTest {

    static ApplicationContext context = null;

    @BeforeClass
    public static void setup() {
        context = new ClassPathXmlApplicationContext("org/kie/spring/decision/decision-beans.xml");
    }

    @Test
    public void testContext() throws Exception {
        assertNotNull(context);
    }

    @Test
    public void testKieBase() throws Exception {
        KieBase kbase = (KieBase) context.getBean("decisionCSV");
        assertNotNull(kbase);
    }

    @Test
    public void testStatelessKieSession() throws Exception {
        StatelessKieSession ksession = (StatelessKieSession) context.getBean("ksession1");
        assertNotNull(ksession);
    }

    @Test
    public void testDecisionTableRules() throws Exception {
        StatefulKnowledgeSession ksession = (StatefulKnowledgeSession) context.getBean("ksession2");

        assertNotNull(ksession.getGlobals().get("persons"));
        List<?> list = (List<?>) ksession.getGlobals().get("persons");

        ksession.insert(new Cheese("cheddar",
                42));
        ksession.insert(new Person("michael",
                "stilton",
                25));
        ksession.insert(new Person("Jane",
                "stilton",
                55));

        ksession.fireAllRules();
        ksession.dispose();

        assertEquals(2, list.size());

        assertTrue(list.contains("Young man cheddar"));
        assertTrue(list.contains("Old man stilton"));
    }

    @AfterClass
    public static void tearDown() {

    }

}