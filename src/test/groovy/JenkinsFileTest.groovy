import com.lesfurets.jenkins.unit.MethodCall
import com.lesfurets.jenkins.unit.declarative.DeclarativePipelineTest
import org.junit.Before
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat

class JenkinsFileTest extends DeclarativePipelineTest {

    @Before
    void setUp() throws Exception {
        super.setUp()
    }

    @Test
    void should_execute_without_errors() throws Exception {
        runScript("JenkinsFileTest")
        printCallStack()
        assertJobStatusSuccess()
    }

    @Test
    void assert_callstack_example() throws Exception {
        runScript("JenkinsFileTest")
        printCallStack()
        assertJobStatusSuccess()
        assertCallStackContains('sh(mvn -f Java/pom.xml clean test)')
    }

    @Test
    void assert_callstack_example2() throws Exception {
        runScript("JenkinsFileTest")
        printCallStack()
        assertJobStatusSuccess()
        assertCallStackContains('sh(mvn -f Java/pom.xml package)')
    }

    @Test
    void advanced_assert_callstack_example() throws Exception {
        runScript("JenkinsFileTest")
        printCallStack()
        assertJobStatusSuccess()
        assertThat(helper.callStack.stream()
                .filter { c -> c.methodName ==~ /sh/  }
                .map(MethodCall.&callArgsToString)
                .findAll { s -> s =~ /mvn.*Java\/pom.xml.*/ })
                .hasSize(2)
    }
}