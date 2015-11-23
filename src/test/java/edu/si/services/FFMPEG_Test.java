package edu.si.services;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import static org.apache.camel.component.exec.ExecBinding.EXEC_EXIT_VALUE;
import static org.apache.camel.component.exec.ExecEndpoint.NO_TIMEOUT;


/**
 * Created by jbirkhimer on 11/19/15.
 */
public class FFMPEG_Test extends CamelTestSupport {
    private File stagingDir;
    private final String stagingDirName = "staging";

    String EXEC_COMMAND_EXECUTABLE = "CamelExecCommandExecutable";
    String EXEC_COMMAND_TIMEOUT = "CamelExecCommandTimeout";
    String EXEC_COMMAND_ARGS = "CamelExecCommandArgs";
    String EXEC_USE_STDERR_ON_EMPTY_STDOUT = "CamelExecUseStderrOnEmptyStdout";

    @Produce(uri = "direct:input")
    ProducerTemplate producerTemplate;

    @EndpointInject(uri = "mock:output")
    MockEndpoint output;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        stagingDir = new File("staging");
        FileUtils.cleanDirectory(stagingDir);
    }

    @Test
    public void testFFMPEG() throws Exception {
        output.setExpectedMessageCount(1);
        output.expectedHeaderReceived(EXEC_EXIT_VALUE, 0);

        sendExchange(0, NO_TIMEOUT);
        output.assertIsSatisfied();
    }

    protected Exchange sendExchange(final String endpoint, final Object commandArgument, final long timeout) {
        return sendExchange(endpoint, commandArgument, timeout, "testBody", false);
    }

    protected Exchange sendExchange(final Object commandArgument, final long timeout) {
        return sendExchange(commandArgument, timeout, "testBody", false);
    }

    protected Exchange sendExchange(final Object commandArgument, final long timeout, final String body, final boolean useStderrOnEmptyStdout) {
        return sendExchange("direct:input", commandArgument, timeout, body, useStderrOnEmptyStdout);
    }

    protected Exchange sendExchange(final String endpoint, final Object commandArgument, final long timeout, final String body, final boolean useStderrOnEmptyStdout) {
        final List<String> args = buildArgs(commandArgument);
        final String javaAbsolutePath = buildJavaExecutablePath();

        return producerTemplate.send(endpoint, new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(body);
                exchange.getIn().setHeader(EXEC_COMMAND_EXECUTABLE, javaAbsolutePath);
                exchange.getIn().setHeader(EXEC_COMMAND_TIMEOUT, timeout);
                exchange.getIn().setHeader(EXEC_COMMAND_ARGS, args);
                exchange.getIn().setHeader("whereTo", "exec:java");
                if (useStderrOnEmptyStdout) {
                    exchange.getIn().setHeader(EXEC_USE_STDERR_ON_EMPTY_STDOUT, true);
                }
            }
        });
    }

    List<String> buildArgs(Object commandArgument) {
        String classpath = System.getProperty("java.class.path");
        List<String> args = new ArrayList<String>();
        args.add("-cp");
        args.add(classpath);
        args.add("/usr/local/ffmpeg");
        args.add(commandArgument.toString());
        return args;
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:input").to("exec:java").to("mock:output");
            }
        };
    }

    /**
     * @return the java executable in a system independent way.
     */
    public static String buildJavaExecutablePath() {
        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome == null) {
            throw new IllegalStateException("The Exec component tests will fail, because the environment variable JAVA_HOME is not set!");
        }
        File java = new File(javaHome + File.separator + "bin" + File.separator + "java");
        return java.getAbsolutePath();
    }

}
