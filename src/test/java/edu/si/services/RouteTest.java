package edu.si.services;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class RouteTest extends CamelBlueprintTestSupport {

    private File stagingDir;
    private final String stagingDirName = "staging";

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        stagingDir = new File("staging");
        FileUtils.cleanDirectory(stagingDir);
    }

    @Override
    protected String getBlueprintDescriptor() {
        return "/OSGI-INF/blueprint/blueprint.xml";
    }

    //si:3144
    //si:3275 // mkv not creating TN
    //si:3281 // m4v

    //si:3184 // m4a
    //si:3210 // mov has problems with thumbnail just black image
    //si:3142 // qt

    //si:3213 //avi
    //si:3045 // mp4
    //si:3120 // mp4


    //si:389320 // mp4
    //si:388897 // mp4
    //si:389285 // avi
    //si:389296 // qt

    @Test
    public void test_MP4() throws Exception {
        // we should then expect at least one message
        getMockEndpoint("mock:result").expectedMinimumMessageCount(1);

        template.sendBodyAndHeader("direct:start", "MP4", "CamelFedoraPid", "si:3120");

        // assert expectations
        assertMockEndpointsSatisfied();
    }

    @Test
    public void test_AVI() throws Exception {
        // we should then expect at least one message
        getMockEndpoint("mock:result").expectedMinimumMessageCount(1);

        template.sendBodyAndHeader("direct:start", "AVI", "CamelFedoraPid", "si:3213");

        // assert expectations
        assertMockEndpointsSatisfied();
    }

    @Test
    public void test_MKV() throws Exception {
        // we should then expect at least one message
        getMockEndpoint("mock:result").expectedMinimumMessageCount(1);

        template.sendBodyAndHeader("direct:start", "MKV", "CamelFedoraPid", "si:3275");

        // assert expectations
        assertMockEndpointsSatisfied();
    }

    //@Test
    public void test_M4A() throws Exception {
        // we should then expect at least one message
        getMockEndpoint("mock:result").expectedMinimumMessageCount(1);

        template.sendBodyAndHeader("direct:start", "M4A", "CamelFedoraPid", "si:3184");

        // assert expectations
        assertMockEndpointsSatisfied();
    }

    /* NOTE: NEED TO LOOK AT CONVERTING THIS TO MP4
     */
    @Test
    public void test_M4V() throws Exception {
        // we should then expect at least one message
        getMockEndpoint("mock:result").expectedMinimumMessageCount(1);

        template.sendBodyAndHeader("direct:start", "M4V", "CamelFedoraPid", "si:3281");

        // assert expectations
        assertMockEndpointsSatisfied();


    }

    @Test
    public void test_MOV() throws Exception {
        // we should then expect at least one message
        getMockEndpoint("mock:result").expectedMinimumMessageCount(1);

        template.sendBodyAndHeader("direct:start", "MOV", "CamelFedoraPid", "si:3210");

        // assert expectations
        assertMockEndpointsSatisfied();
    }

    @Test
    public void test_QT() throws Exception {
        // we should then expect at least one message
        getMockEndpoint("mock:result").expectedMinimumMessageCount(1);

        template.sendBodyAndHeader("direct:start", "QT", "CamelFedoraPid", "si:3142");

        // assert expectations
        assertMockEndpointsSatisfied();
    }

}
