import com.lesfurets.jenkins.unit.MethodCall
import com.lesfurets.jenkins.unit.declarative.DeclarativePipelineTest
import org.junit.Before
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat

class JenkinsFileTest extends DeclarativePipelineTest {

    @Before
    @Override
    void setUp() throws Exception {
        super.setUp()
        helper.registerAllowedMethod('git', [Map], stringInterceptor)
    }

    @Test
    void checkExecutingWithoutErrors() throws Exception {
        runScript('JenkinsFileTest')
        printCallStack()
        assertJobStatusSuccess()
    }

    @Test
    void checkCallStackContainsMvnCleanTest() throws Exception {
        runScript('JenkinsFileTest')
        printCallStack()
        assertJobStatusSuccess()
        assertCallStackContains('sh(mvn -f Java/pom.xml clean test)')
    }

    @Test
    void checkCallStackContainsMvnInstall() throws Exception {
        runScript('JenkinsFileTest')
        printCallStack()
        assertJobStatusSuccess()
        assertCallStackContains('sh(mvn -f Java/pom.xml install)')
    }

    @Test
    void checkCallStackContainsGradlew() throws Exception {
        runScript('JenkinsFileTest')
        printCallStack()
        assertJobStatusSuccess()
        assertCallStackContains('sh(./gradlew clean check)')
    }

    @Test
    void checkCallStackContainsMvnPomXml() throws Exception {
        runScript('JenkinsFileTest')
        printCallStack()
        assertJobStatusSuccess()
        assertThat(helper.callStack.stream()
                .filter { c -> c.methodName ==~ /sh/  }
                .map(MethodCall.&callArgsToString)
                .findAll { s -> s =~ /mvn.*Java\/pom.xml.*/ })
                .hasSize(2)
    }
}